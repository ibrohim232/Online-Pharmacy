package com.example.onlinemedicine.exception;

public class DataAlreadyExistsException extends RuntimeException{
    public DataAlreadyExistsException(String formatted) {
        super(formatted);
    }
}
