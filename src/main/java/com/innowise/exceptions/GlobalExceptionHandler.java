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
    @ExceptionHandler(NoSuchCategoryException.class)
    public ResponseEntity<String> handleNoSuchCategoryException(NoSuchCategoryException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoSuchCommentException.class)
    public ResponseEntity<String> handleNoSuchCommentException(NoSuchCommentException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoSuchAttachmentException.class)
    public ResponseEntity<String> handleNoSuchAttachmentFoundException(NoSuchAttachmentException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoSuchHistoryException.class)
    public ResponseEntity<String> handleNoSuchHistoryFoundException(NoSuchHistoryException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoSuchUserIdException.class)
    public ResponseEntity<String> handleNoSuchUserIdException(NoSuchUserIdException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoSuchTicketException.class)
    public ResponseEntity<String> handleNoSuchTicketException(NoSuchTicketException exception) {
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
}
