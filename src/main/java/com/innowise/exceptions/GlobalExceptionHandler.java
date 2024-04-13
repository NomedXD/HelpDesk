package com.innowise.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoSuchEntityIdException.class, UserNotFoundException.class, NoSuchRoleNameException.class})
    public ResponseEntity<ApiErrorResponse> handleNoSuchEntityException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler({NotOwnerTicketException.class, TicketNotDoneException.class, TicketNotDraftException.class,
            UserAlreadyExistsException.class, WrongCurrentPasswordException.class, TicketStateTransferException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> error : exception.getConstraintViolations()) {
            errorMessage.append(error.getPropertyPath().toString().split("\\.")[2]);
            errorMessage.append(": ");
            errorMessage.append(error.getMessage());
            errorMessage.append("; ");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), errorMessage.toString()));
    }

    @ExceptionHandler(AttachedFileReadException.class)
    public ResponseEntity<ApiErrorResponse> handleAttachedFileReadException(AttachedFileReadException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler({AccessDeniedException.class, BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiErrorResponse(HttpStatus.FORBIDDEN, LocalDateTime.now(), exception.getMessage()));
    }
}
