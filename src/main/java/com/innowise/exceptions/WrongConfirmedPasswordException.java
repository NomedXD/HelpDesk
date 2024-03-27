package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WrongConfirmedPasswordException extends IllegalArgumentException {
    public WrongConfirmedPasswordException() {
        super("New password input and confirmed password input doesn't match");
    }
}
