package com.UserService.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user")
public class User {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_name")
    private String userName;

    @Column(name = "password",length = 100)
    private String password;

    @Id
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at", updatable=false)
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(new Date().getTime());
        updatedAt=createdAt;
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(new Date().getTime());
    }

}
