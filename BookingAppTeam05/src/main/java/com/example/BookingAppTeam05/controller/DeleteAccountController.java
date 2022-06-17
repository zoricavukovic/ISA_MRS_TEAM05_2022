package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.DeleteAccountRequestDTO;
import com.example.BookingAppTeam05.service.DeleteAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/deleteAccounts")
public class DeleteAccountController {

    private DeleteAccountService deleteAccountService;

    @Autowired
    public DeleteAccountController(DeleteAccountService deleteAccountService) {
        this.deleteAccountService = deleteAccountService;
    }

    public DeleteAccountController(){}

    @GetMapping(value="/unprocessed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeleteAccountRequestDTO>> getAllUnprocessedDeleteAccountRequests() {
        List<DeleteAccountRequestDTO> retVal = deleteAccountService.getAllUnprocessedDeleteAccountRequestDTOs();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value="/giveResponse", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> giveResponse(@RequestBody DeleteAccountRequestDTO d) {
        deleteAccountService.giveResponse(d);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping(value = "/sendRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> sendRequest(@RequestBody DeleteAccountRequestDTO deleteRequest){
        deleteAccountService.createDeleteRequest(deleteRequest);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping(value="/hasRequest/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Boolean> hasRequest(@PathVariable Long id) {
        Boolean retVal = deleteAccountService.hasUserRequest(id);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

}
