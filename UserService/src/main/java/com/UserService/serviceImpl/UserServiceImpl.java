package com.UserService.serviceImpl;

import com.UserService.dto.LoginForm;
import com.UserService.dto.LoginResponse;
import com.UserService.dto.UserDto;
import com.UserService.entity.User;
import com.UserService.exception.InvalidPasswordException;
import com.UserService.exception.ResourceNotFoundException;
import com.UserService.exception.UserAlreadyExistsException;
import com.UserService.mapper.UserMapper;
import com.UserService.repository.UserRepository;
import com.UserService.service.UserService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    public UserRepository userRepo;
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwtExpiration}")
    private int jwtExpiration;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @param user - userDto
     */
    public void registerUser(User user) {
        logger.info("Registering user: {}", user.getUserName());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        Optional<User> optionalUser = userRepo.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("Customer already registered with given email address "
                    + user.getEmail());
        }
        userRepo.save(user);
    }


    /**
     * @param identifier - this can be either email or the username
     * @return the details of the user that is found with the entered param
     */
    @Override
    public UserDto fetchUser(String identifier, FetchType fetchType) {
        logger.info("Finding user by {}: {}", fetchType, identifier);
        Optional<User> userOptional = switch (fetchType) {
            case USERNAME -> userRepo.findByUserName(identifier);
            case EMAIL -> userRepo.findByEmail(identifier);
        };
        User user = userOptional.orElseThrow(() ->
                new ResourceNotFoundException("User", fetchType.name(), identifier));

        return UserMapper.mapToUserDto(user);
    }

    /**
     * @param userDto - input userDto
     * @return true or false, indicating if update is successful or not
     */
    @Override
    @Transactional
    public boolean updateUser(UserDto userDto) {
        boolean isUpdated = false;

        if (userDto != null) {
            String email = userDto.getEmail();
            User existingUser = userRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
            UserMapper.mapToUser(userDto, existingUser);
            existingUser.setUpdatedAt(new Timestamp(new Date().getTime()));
            userRepo.save(existingUser);
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * @param email - input email
     * @return true or false, indicating if deletion is successful or not
     */
    @Override
    public boolean deleteUser(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "emailID", email)
        );
        userRepo.deleteByEmail(user.getEmail());
        logger.info("Deleting user with email : {}", email);
        return true;
    }

    /**
     * @return - list of all the users
     */
    @Override
    public List<UserDto> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepo.findAll();
        List<UserDto> userDTOs = new ArrayList<UserDto>();

        for (User user : users) {
            userDTOs.add(UserMapper.mapToUserDto(user));
        }

        return userDTOs;
    }

    @Override
    public LoginResponse login(LoginForm loginForm) throws ResourceNotFoundException {
        Optional<User> optionalUser = userRepo.findByEmail(loginForm.getEmail());
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User", "email", loginForm.getEmail());
        }
        User user = optionalUser.get();
        if (!passwordMatches(loginForm.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
        return createJwtToken(user);
    }

    private LoginResponse createJwtToken(User user) {
        logger.info("The jwtSecret is : {}", jwtSecret);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(user.getEmail()) // Use a non-sensitive subject
                 // Handle potential null role
                .claim("email", user.getEmail()) // Add email claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret);

        String token = jwtBuilder.compact();
        logger.info("The jwt token is {}",token);
        return new LoginResponse(user.getUserName(), user.getEmail(), user.getAddress());
    }

    private boolean passwordMatches(String enteredPassword, String storedHash) {
        return passwordEncoder.matches(enteredPassword, storedHash);
    }

}

