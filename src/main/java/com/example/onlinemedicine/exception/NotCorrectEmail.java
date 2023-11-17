package com.example.onlinemedicine.exception;

public class NotCorrectEmail extends RuntimeException{

    public NotCorrectEmail(String message) {
        super(message);
    }
}
