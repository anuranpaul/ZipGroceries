package com.UserService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Invalid password");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}

