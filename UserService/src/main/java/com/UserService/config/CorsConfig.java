package com.UserService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/users/**")
                .allowedOrigins("http://localhost:4200") // Allow requests from Angular application
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specified HTTP methods
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true); // Allow including credentials in requests
    }
}

