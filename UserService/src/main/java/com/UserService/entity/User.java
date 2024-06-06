package com.UserService.entity;


import com.UserService.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String email;
    private String username;
    private String password;
    private String address;
    //private UserRole role;

}

