package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAlreadyExistsException extends IllegalArgumentException {
    private final String email;

    public UserAlreadyExistsException(String email) {
        super(String.format("User with email = %s already exists", email));
        this.email = email;
    }
}
