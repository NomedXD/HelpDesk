package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotFoundException extends IllegalArgumentException {
    private final String email;

    public UserNotFoundException(String email) {
        super(String.format("User with email = %s doesn't exists", email));
        this.email = email;
    }
}
