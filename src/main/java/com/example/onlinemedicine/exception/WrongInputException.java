package com.example.onlinemedicine.exception;

public class WrongInputException extends RuntimeException {
    public WrongInputException(String msg) {
        super(msg);
    }
}
