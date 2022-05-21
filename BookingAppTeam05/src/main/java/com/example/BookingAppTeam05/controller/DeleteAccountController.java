package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.DeleteAccountRequestDTO;
import com.example.BookingAppTeam05.model.DeleteAccountRequest;
import com.example.BookingAppTeam05.service.DeleteAccountService;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/deleteAccounts")
public class DeleteAccountController {

    private DeleteAccountService deleteAccountService;

    @Autowired
    public DeleteAccountController(DeleteAccountService deleteAccountService) {
        this.deleteAccountService = deleteAccountService;
    }

    @GetMapping(value="/unprocessed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeleteAccountRequestDTO>> getAllUnprocessedDeleteAccountRequests() {
        List<DeleteAccountRequestDTO> retVal = deleteAccountService.getAllUnprocessedDeleteAccountRequestDTOs();
        if (retVal == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value="/giveResponse", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> giveResponse(@RequestBody DeleteAccountRequestDTO d) {
        String errorCode = deleteAccountService.giveResponse(d);
        if (errorCode != null)
            return new ResponseEntity<>(errorCode, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("sve ok", HttpStatus.OK);
    }

}
