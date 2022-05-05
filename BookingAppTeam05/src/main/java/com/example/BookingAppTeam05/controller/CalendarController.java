package com.example.BookingAppTeam05.controller;


import com.example.BookingAppTeam05.dto.CalendarEntryDTO;
import com.example.BookingAppTeam05.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calendar")
@CrossOrigin
public class CalendarController {

    private CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }


    @GetMapping(value="/entity/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalendarEntryDTO>> getCalendarEntriesForBookingEntity(@PathVariable  Long id) {
        List<CalendarEntryDTO> retVal = calendarService.getCalendarEntriesDTOByEntityId(id);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

}
