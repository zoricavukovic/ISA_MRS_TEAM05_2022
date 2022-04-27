package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.SimpleSearchForBookingEntityDTO;
import com.example.BookingAppTeam05.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookingEntities")
public class BookingEntityController {
    private BookingEntityService bookingEntityService;

    @Autowired
    public BookingEntityController(BookingEntityService bookingEntityService) {
        this.bookingEntityService = bookingEntityService;
    }

    @GetMapping(value = "/allByOwner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getAllBookingEntitiesByOwnerId(@PathVariable Long id) {
        List<SearchedBookingEntityDTO> entities = bookingEntityService.getSearchedBookingEntitiesDTOsByOnwerId(id);
        if (entities == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @GetMapping(value = "/view/{id}")
    public ResponseEntity<SearchedBookingEntityDTO> getBookingEntityInfoForView(@PathVariable Long id) {
        SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(id);
        if (s == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }


    @PostMapping(value="/simpleSearch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getSearchedBookingEntities(@RequestBody SimpleSearchForBookingEntityDTO s) {
        try {
            System.out.println("usao");
            System.out.println("--------------------------------");
            System.out.println(s.getMinCostPerPerson());
            System.out.println(s.getMaxCostPerPerson());
            List<SearchedBookingEntityDTO> entities = bookingEntityService.getSearchedBookingEntitiesDTOsByOnwerId(s.getOwnerId());
            if (entities == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            entities = bookingEntityService.simpleFilterSearchForBookingEntities(entities, s);
            if (entities == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(entities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
