package com.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class LoginForm {
    String email;
    String password;
}
