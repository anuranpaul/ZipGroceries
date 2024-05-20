package com.UserService.mapper;

import com.UserService.constants.UserRole;
import com.UserService.dto.UserDto;
import com.UserService.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto(); // Consider a pre-initialized UserDto if needed
        userDto.setUsername(user.getUsername());
        // Exclude password from DTO for security reasons
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setRole(user.getRole().name()); // Assuming UserRole has a name() method
        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        user.setRole(UserRole.valueOf(userDto.getRole())); // Assuming UserRole is an enum
        return user;
    }
}