package com.UserService.dto;

import com.UserService.constants.UserRole;
import lombok.Data;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.Collection;
import java.util.List;

@Data
public class UserDto {

    //private String id;
    private String email;
    private String username;
    //private String password;
    private String address;
    private String role;
}
