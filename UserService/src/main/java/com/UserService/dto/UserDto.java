package com.UserService.dto;

import com.UserService.entity.Role;
import lombok.Data;

@Data
public class UserDto {

    //private String id;
    private String email;
    private String userName;
    //private String password;
    private String address;
}
