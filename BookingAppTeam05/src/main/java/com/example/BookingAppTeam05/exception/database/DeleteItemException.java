package com.example.BookingAppTeam05.exception.database;

public class DeleteItemException extends DatabaseException{
    public DeleteItemException() {}
    public DeleteItemException(String message) {
        super(message);
    }
}
