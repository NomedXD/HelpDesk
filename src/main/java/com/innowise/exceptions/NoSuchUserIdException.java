package com.innowise.exceptions;

public class NoSuchUserIdException extends IllegalArgumentException {
    private final Integer userId;

    public NoSuchUserIdException(Integer userId) {
        super(String.format("User with id %d is not found in DB", userId));
        this.userId = userId;
    }
}
