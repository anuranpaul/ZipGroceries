package com.UserService;

import com.UserService.constants.UserConstants;
import com.UserService.controller.UserController;
import com.UserService.dto.ResponseDto;
import com.UserService.dto.UserDto;
import com.UserService.exception.ResourceNotFoundException;
import com.UserService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTests{

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testAddCustomer() {
//        UserDto userDto = new UserDto();
//        ResponseEntity<ResponseDto> response = userController.addCustomer(userDto);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(UserConstants.STATUS_201, response.getBody().getStatusCode());
//        assertEquals(UserConstants.MESSAGE_201, response.getBody().getStatusMsg());
//        verify(userService, times(1)).registerUser(userDto);
//    }

    @Test
    void testGetUserByEmail() {
        UserDto userDto = new UserDto();
        when(userService.fetchUser(anyString(), any())).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUser("test@example.com", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService, times(1)).fetchUser("test@example.com", UserService.FetchType.EMAIL);
    }

    @Test
    void testGetUserByUsername() {
        UserDto userDto = new UserDto();
        when(userService.fetchUser(anyString(), any())).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUser(null, "testuser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService, times(1)).fetchUser("testuser", UserService.FetchType.USERNAME);
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(userService.fetchUser(anyString(), any())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> userController.getUser("nonexistent@example.com", null));
        verify(userService, times(1)).fetchUser("nonexistent@example.com", UserService.FetchType.EMAIL);
    }

    @Test
    void testUpdateAccountDetails_Success() {

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        when(userService.updateUser(userDto)).thenReturn(true);
        ResponseEntity<ResponseDto> response = userController.updateAccountDetails("test@example.com", userDto);
        verify(userService, times(1)).updateUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserConstants.STATUS_200, response.getBody().getStatusCode());
        assertEquals(UserConstants.MESSAGE_200, response.getBody().getStatusMsg());
    }

    @Test
    void testUpdateAccountDetails_Failure() {

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        when(userService.updateUser(userDto)).thenReturn(false);
        ResponseEntity<ResponseDto> response = userController.updateAccountDetails("test@example.com", userDto);
        verify(userService, times(1)).updateUser(userDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(UserConstants.STATUS_500, response.getBody().getStatusCode());
        assertEquals(UserConstants.MESSAGE_500, response.getBody().getStatusMsg());
    }

    @Test
    void testGetAllUsers() {
        List<UserDto> userList = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testDeleteAccountDetails() {
        when(userService.deleteUser(anyString())).thenReturn(true);

        ResponseEntity<ResponseDto> response = userController.deleteAccountDetails("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserConstants.STATUS_200, response.getBody().getStatusCode());
        assertEquals(UserConstants.MESSAGE_200, response.getBody().getStatusMsg());
        verify(userService, times(1)).deleteUser("test@example.com");
    }

    @Test
    void testDeleteAccountDetailsNotFound() {
        when(userService.deleteUser(anyString())).thenReturn(false);

        ResponseEntity<ResponseDto> response = userController.deleteAccountDetails("nonexistent@example.com");

        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        assertEquals(UserConstants.STATUS_417, response.getBody().getStatusCode());
        assertEquals(UserConstants.MESSAGE_417_DELETE, response.getBody().getStatusMsg());
        verify(userService, times(1)).deleteUser("nonexistent@example.com");
    }
    // Add more test methods for other endpoints following the same pattern
}

