package com.UserService.service;

import com.UserService.dto.LoginForm;
import com.UserService.dto.LoginResponse;
import com.UserService.dto.UserDto;
import com.UserService.entity.User;

import java.util.List;

public interface UserService {

    enum FetchType {
        USERNAME,
        EMAIL
    }
    /**
     *
     * @param userDto - enter user details
     */
    void registerUser(User user);

    /**
     *
     * @param identifier - email or username
     * @param fetchType - EMAIL or USERNAME
     * @return - User
     */
    UserDto fetchUser(String identifier, FetchType fetchType);

    /**
     *
     * @param userDto - input userDto
     * @return true or false, indicating if update is successful or not
     */
    boolean updateUser(UserDto userDto);

    /**
     *
     * @param email - input username
     * @return true or false, indicating if deletion is successful or not
     */
    boolean deleteUser(String email);

    /**
     *
     * @return - list of all the users
     */
    List<UserDto> getAllUsers();

    /**
     *
     * @param loginForm - login details
     * @return - returns the JWT token response
     */
    LoginResponse login(LoginForm loginForm);

}

