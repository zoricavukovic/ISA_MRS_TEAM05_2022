package com.example.BookingAppTeam05.exception;

public class RequestAlreadyProcessedException extends BookingAppException{
    public RequestAlreadyProcessedException() {}
    public RequestAlreadyProcessedException(String message) {
        super(message);
    }
}
