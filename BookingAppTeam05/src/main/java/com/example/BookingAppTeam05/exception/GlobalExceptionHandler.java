package com.example.BookingAppTeam05.exception;

import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.exception.database.DeleteItemException;
import com.example.BookingAppTeam05.exception.database.EditItemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<String> itemNotFoundException(ItemNotFoundException itemNotFoundException) {
        return new ResponseEntity<>(itemNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ItemAlreadyExistException.class)
    public ResponseEntity<String> itemAlreadyExistException(ItemAlreadyExistException itemAlreadyExistException) {
        return new ResponseEntity<>(itemAlreadyExistException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EditItemException.class)
    public ResponseEntity<String> editItemException(EditItemException editItemException) {
        return new ResponseEntity<>(editItemException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = CreateItemException.class)
    public ResponseEntity<String> createItemException(CreateItemException createItemException) {
        return new ResponseEntity<>(createItemException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = DeleteItemException.class)
    public ResponseEntity<String> deleteItemException(DeleteItemException deleteItemException) {
        return new ResponseEntity<>(deleteItemException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = RequestAlreadyProcessedException.class)
    public ResponseEntity<String> requestAlreadyProcessed(RequestAlreadyProcessedException requestAlreadyProcessedException) {
        return new ResponseEntity<>(requestAlreadyProcessedException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<String> conflictException(ConflictException conflictException) {
        return new ResponseEntity<>(conflictException.getMessage(), HttpStatus.CONFLICT);
    }

}
