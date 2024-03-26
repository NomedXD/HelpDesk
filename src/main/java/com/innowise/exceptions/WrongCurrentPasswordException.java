package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WrongCurrentPasswordException extends IllegalArgumentException {
    public WrongCurrentPasswordException() {
        super("Wrong current password");
    }
}
