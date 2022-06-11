package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.RatingDTO;
import com.example.BookingAppTeam05.dto.RatingReviewDTO;
import com.example.BookingAppTeam05.model.Rating;
import com.example.BookingAppTeam05.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
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

    @GetMapping("/byReservationId/{id}")
    public ResponseEntity<RatingDTO> getRatingByOwnerId(@PathVariable Long id) {
        RatingDTO ratingDTO = ratingService.findByReservationId(id);
        if(ratingDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(ratingDTO);
    }

    @PostMapping("/createRating")
    public ResponseEntity<RatingDTO> createRating(@RequestBody RatingDTO ratingDTO) {
        Rating rating = ratingService.createRating(ratingDTO);
        if (rating == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(ratingDTO);
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

    @GetMapping(value="/ProcessedByEntityId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatingReviewDTO>> ProcessedByEntityId(@PathVariable Long id) {
        List<RatingReviewDTO> retVal = ratingService.getProcessedRatingsForEntity(id);
        if (retVal == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value = "/putForPublication", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putClientReviewForPublication(@Valid @RequestBody RatingReviewDTO rating) {
        Rating r = ratingService.findById(rating.getId());
        if (r == null)
            return new ResponseEntity<String>("Error. Can't find rating with id: " + rating.getId(), HttpStatus.BAD_REQUEST);
        String error = ratingService.setRatingForPublicationAndNotifyOwner(rating);
        if (error != null)
            return new ResponseEntity<String>(error, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<String>("sve ok", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable Long id) {
        String error = ratingService.deleteRatingById(id);
        if (error != null)
            return new ResponseEntity<String>(error, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<String>("sve ok", HttpStatus.OK);
    }
}
