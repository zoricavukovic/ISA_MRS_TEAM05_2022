package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.calendar.UnavailableDateDTO;
import com.example.BookingAppTeam05.service.UnavailableDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/unavailableDates")
public class UnavailableDateController {

    private UnavailableDateService unavailableDateService;

    @Autowired
    public UnavailableDateController(UnavailableDateService unavailableDateService) {
        this.unavailableDateService = unavailableDateService;
    }

    @PostMapping(value = "/checkForOverlapUnavailableDate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnavailableDateDTO> checkIfThereExistOverlapBetweenUnavailablePeriods(@Valid @RequestBody UnavailableDateDTO unavailableDateDTO) {
        UnavailableDateDTO retVal = unavailableDateService.checkIfThereExistOverlapBetweenUnavailablePeriodsForEntity(unavailableDateDTO, unavailableDateDTO.getEntityId());
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnavailableDateDTO> addNewUnavailableDate(@Valid @RequestBody UnavailableDateDTO unavailableDateDTO) {
        UnavailableDateDTO retVal = unavailableDateService.addNewUnavailableDateForEntityId(unavailableDateDTO);
        if (retVal == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(retVal, HttpStatus.CREATED);
    }

    @PostMapping(value = "/setAvailable/", consumes = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Integer> deleteUnavailablePeriodForEntityId(@Valid @RequestBody UnavailableDateDTO unavailableDateDTO) {
        Integer res = unavailableDateService.setUnavailableDateAsAvaialbeForEntityId(unavailableDateDTO);
        if (res != null && res != 0)
            return new ResponseEntity<>(res, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}