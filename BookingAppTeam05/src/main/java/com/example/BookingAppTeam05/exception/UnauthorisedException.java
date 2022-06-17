package com.example.BookingAppTeam05.exception;

public class UnauthorisedException extends BookingAppException{
    public UnauthorisedException() {}
    public UnauthorisedException(String message) {
        super(message);
    }
}
