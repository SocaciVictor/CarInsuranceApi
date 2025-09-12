package com.example.carins.Exception;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String reason) {
        super(reason);
    }
}
