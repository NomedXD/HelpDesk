package com.innowise.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(NoSuchEntityIdException.class)
    public ResponseEntity<String> handleNoSuchEntityException(NoSuchEntityIdException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NotOwnerTicketException.class)
    public ResponseEntity<String> handleNotOwnerTicketException(NotOwnerTicketException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(TicketNotDoneException.class)
    public ResponseEntity<String> handleTicketNotDoneException(TicketNotDoneException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
        return new ResponseEntity<>(exception.getConstraintViolations().toString(), HttpStatus.BAD_REQUEST);
    } //TODO подумать, как лучше возвращать ошибку при валидации, когда будет готов фронт

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoSuchRoleNameException.class)
    public ResponseEntity<String> handleNoSuchRoleNameException(NoSuchRoleNameException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AttachedFileReadException.class)
    public ResponseEntity<String> handleAttachedFileReadException(AttachedFileReadException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(WrongCurrentPasswordException.class)
    public ResponseEntity<String> handleWrongCurrentPasswordException(WrongCurrentPasswordException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
