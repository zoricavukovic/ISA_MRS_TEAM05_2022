package com.example.BookingAppTeam05.exception;

public class ItemAlreadyExistException extends BookingAppException{
    public ItemAlreadyExistException() {}
    public ItemAlreadyExistException(String message) {
        super(message);
    }
}
