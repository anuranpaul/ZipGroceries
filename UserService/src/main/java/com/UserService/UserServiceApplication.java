package com.UserService;

import com.UserService.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories
@OpenAPIDefinition(info = @Info(
		title = "User microservice REST API Documentation",
		description = "Handling users microservice REST API Documentation"))
public class UserServiceApplication {

	@Autowired
	UserRepository usersRepo;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
