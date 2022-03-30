package com.mohey.resourceserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserDontHaveRequiredScope extends RuntimeException {
    public UserDontHaveRequiredScope() {
        super("User Doesn't have the required scope");
    }
}
