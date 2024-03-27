package com.innowise.exceptions;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public record ApiErrorResponse (
     HttpStatus status,
     LocalDateTime timestamp,
     String message
    ) {

}
