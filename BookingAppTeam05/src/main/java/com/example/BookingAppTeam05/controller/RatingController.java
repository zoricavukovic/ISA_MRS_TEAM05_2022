package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.RatingReviewDTO;
import com.example.BookingAppTeam05.model.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ratings")
public class RatingController {

    private RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping(value="/all/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatingReviewDTO>> getListOfCreatedReports(@PathVariable String type) {
        List<RatingReviewDTO> retVal = new ArrayList<>();
        if (type.equals("processed"))
            retVal = ratingService.getAllProcessedRatingReviewDTOs();
        else if (type.equals("unprocessed"))
            retVal = ratingService.getAllUnprocessedRatingReviewDTOs();
        if (retVal == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }
}
