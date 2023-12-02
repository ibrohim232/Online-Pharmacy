package com.example.onlinemedicine.exception;


public class IncorrectPassword extends RuntimeException{
    public IncorrectPassword(String message){
        super(message);
    }
}
