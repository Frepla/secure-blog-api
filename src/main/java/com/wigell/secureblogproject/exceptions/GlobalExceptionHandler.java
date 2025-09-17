package com.wigell.secureblogproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().format(formatter));
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("object", ex.getObject());
        body.put("field", ex.getField());
        body.put("value", ex.getValue());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedActionException ex) {
        Map<String, Object> body = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        body.put("timestamp", LocalDateTime.now().format(formatter));
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("object", ex.getObject());
        body.put("action", ex.getAction());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(InvalidPostException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPost(InvalidPostException ex) {
        Map<String, Object> body = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        body.put("timestamp", LocalDateTime.now().format(formatter));
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("object", ex.getObject());
        body.put("field", ex.getField());
        body.put("value", ex.getValue());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
