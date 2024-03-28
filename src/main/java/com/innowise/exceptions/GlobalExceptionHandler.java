package com.innowise.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchEntityIdException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchEntityException(NoSuchEntityIdException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler(NotOwnerTicketException.class)
    public ResponseEntity<ApiErrorResponse> handleNotOwnerTicketException(NotOwnerTicketException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), exception.getMessage()));    }

    @ExceptionHandler(TicketNotDoneException.class)
    public ResponseEntity<ApiErrorResponse> handleTicketNotDoneException(TicketNotDoneException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), exception.getMessage()));
    }

    // TODO забыли про handleTicketNotDraftException *URGENT*

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), exception.getConstraintViolations().toString()));
    } // TODO подумать, как лучше возвращать ошибку при валидации, когда будет готов фронт (NomedXD) *NOT URGENT*

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), exception.getMessage()));    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler(NoSuchRoleNameException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchRoleNameException(NoSuchRoleNameException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler(AttachedFileReadException.class)
    public ResponseEntity<ApiErrorResponse> handleAttachedFileReadException(AttachedFileReadException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler(WrongCurrentPasswordException.class)
    public ResponseEntity<ApiErrorResponse> handleWrongCurrentPasswordException(WrongCurrentPasswordException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiErrorResponse(HttpStatus.FORBIDDEN, LocalDateTime.now(), exception.getMessage()));
    }
}
