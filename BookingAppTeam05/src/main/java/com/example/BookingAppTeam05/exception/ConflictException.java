package com.example.BookingAppTeam05.exception;

public class ConflictException extends BookingAppException {
    public ConflictException() {}
    public ConflictException(String message) {
        super(message);
    }
}
