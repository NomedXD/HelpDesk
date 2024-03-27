package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoSuchRoleNameException extends IllegalArgumentException {
    private final String roleName;

    public NoSuchRoleNameException(String roleName) {
        super(String.format("User role %s not found", roleName));
        this.roleName = roleName;
    }
}
