package com.example.onlinemedicine.exceptionHandling;

import com.example.onlinemedicine.dto.base.ExceptionDto;
import com.example.onlinemedicine.exception.*;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IncorrectPassword.class)
    public ResponseEntity<String> handleException(IncorrectPassword ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(TimeOut.class)
    public ResponseEntity<String> handleException(TimeOut ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<String> handleException(RedisConnectionFailureException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ExceptionDto> dataNotFound(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDto(e.getMessage(), 401));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionDto> bindExceptionHandler(BindException e) {
        StringBuilder massages = new StringBuilder();
        e.getAllErrors().forEach(error -> {
            massages.append(error.getDefaultMessage());
            massages.append(" , ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(massages.toString(), 404));
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<ExceptionDto> dataAlreadyExistsExceptionHandler(DataAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(e.getMessage(), 404));
    }

    @ExceptionHandler(WrongInputException.class)
    public ResponseEntity<ExceptionDto> wrongInputExceptionHandler(WrongInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(e.getMessage(), 404));
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionDto> tokenExpiredException(TokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(e.getMessage(), 404));
    }
}
