package com.example.BookingAppTeam05.exception;

public class BookingAppException extends RuntimeException{
    private String message;

    public BookingAppException() {
        super();
    }

    public BookingAppException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
