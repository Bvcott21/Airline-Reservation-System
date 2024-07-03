package com.bvcott.airlines.exception;

public class CrewMemberNotFoundException extends RuntimeException {
    public CrewMemberNotFoundException(String message) {
        super(message);
    }
    
}
