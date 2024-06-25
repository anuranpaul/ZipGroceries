package com.UserService.controller;

import com.UserService.constants.UserConstants;
import com.UserService.dto.LoginForm;
import com.UserService.dto.LoginResponse;
import com.UserService.dto.ResponseDto;
import com.UserService.dto.UserDto;
import com.UserService.entity.User;
import com.UserService.exception.InvalidPasswordException;
import com.UserService.exception.ResourceNotFoundException;
import com.UserService.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addCustomer(@RequestBody User user) {
        logger.info("Registering user: {}", user.getEmail());
        userService.registerUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginForm loginForm) throws ResourceNotFoundException, InvalidPasswordException {
        LoginResponse response = userService.login(loginForm);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/fetch")
    public ResponseEntity<UserDto> getUser(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username
    ) {
        if (email != null) {
            logger.info("Fetching user by email: {}", email);
            UserDto userDto = userService.fetchUser(email, UserService.FetchType.EMAIL);
            if (userDto != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(userDto);
            } else {
                logger.warn("User with EmailID {} not found", email);
                throw new ResourceNotFoundException("User", "email", email);
            }
        } else if (username != null) {
            logger.info("Fetching user by username: {}", username);
            UserDto userDto = userService.fetchUser(username, UserService.FetchType.USERNAME);
            if (userDto != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(userDto);
            } else {
                logger.warn("User with Username {} not found", username);
                throw new ResourceNotFoundException("User", "username", username);
            }
        } else {
            throw new IllegalArgumentException("Either email or username must be provided.");
        }
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<ResponseDto> updateAccountDetails(@PathVariable String email, @RequestBody UserDto userDto) {
        boolean isUpdated = userService.updateUser(userDto);
        if (isUpdated) {
            logger.info("Updating user with Email: {}", email);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
        } else {
            logger.warn("User with Email {} not found for update", email);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(UserConstants.STATUS_500, UserConstants.MESSAGE_500));
        }
    }


    @GetMapping("/showAll")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        logger.info("Fetching all users");
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam String email) {
        boolean isDeleted = userService.deleteUser(email);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(UserConstants.STATUS_417, UserConstants.MESSAGE_417_DELETE));
        }
    }
}
