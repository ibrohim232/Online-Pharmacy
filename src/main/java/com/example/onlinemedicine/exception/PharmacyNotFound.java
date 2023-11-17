package com.example.onlinemedicine.exception;

public class PharmacyNotFound extends RuntimeException{

    public PharmacyNotFound(String message) {
        super(message);
    }
}
