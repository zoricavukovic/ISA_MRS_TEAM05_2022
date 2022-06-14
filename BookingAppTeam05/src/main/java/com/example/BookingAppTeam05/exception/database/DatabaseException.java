package com.example.BookingAppTeam05.exception.database;

import com.example.BookingAppTeam05.exception.BookingAppException;

public class DatabaseException extends BookingAppException {
    public DatabaseException() {
        super();
    }
    public DatabaseException(String message) {
        super(message);
    }
}
