package com.UserService.repository;

import com.UserService.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

}
