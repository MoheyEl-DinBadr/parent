package com.mohey.resourceserver.exceptions;

public class UserScopeMustBeReactive extends RuntimeException {
    public UserScopeMustBeReactive() {
        super("UserScope Annotation must be used with a reactive type");
    }
}
