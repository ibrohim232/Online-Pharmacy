package com.example.onlinemedicine.exceptionHandling;

import com.example.onlinemedicine.exception.IncorrectPassword;
import com.example.onlinemedicine.exception.TimeOut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IncorrectPassword.class)
    public ResponseEntity<String> handleException(IncorrectPassword ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(TimeOut.class)
    public ResponseEntity<String> handleException(TimeOut ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
