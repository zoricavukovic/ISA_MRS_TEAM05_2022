package com.example.BookingAppTeam05.exception;

public class ItemNotFoundException extends BookingAppException {
    public ItemNotFoundException() {}
    public ItemNotFoundException(String message) {
        super(message);
    }
}
