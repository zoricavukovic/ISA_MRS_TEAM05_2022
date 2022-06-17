package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.RatingDTO;
import com.example.BookingAppTeam05.dto.RatingReviewDTO;
import com.example.BookingAppTeam05.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/ratings")
public class RatingController {
    private RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public RatingController(){}

    @GetMapping("/byReservationId/{id}")
    public ResponseEntity<RatingDTO> getRatingByReservationId(@PathVariable Long id) {
        RatingDTO ratingDTO = ratingService.findRatingByReservationId(id);
        return new ResponseEntity<>(ratingDTO, HttpStatus.OK);
    }

    @PostMapping("/createRating")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<RatingDTO> createRating(@RequestBody RatingDTO ratingDTO) {
        ratingService.createRating(ratingDTO);
        return new ResponseEntity<>(ratingDTO, HttpStatus.CREATED);
    }

    @GetMapping(value="/all/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<RatingReviewDTO>> getListOfCreatedRatingReview(@PathVariable String type) {
        List<RatingReviewDTO> retVal = ratingService.getListOfRatingReviewDTO(type);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @GetMapping(value="/ProcessedByEntityId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatingReviewDTO>> ProcessedByEntityId(@PathVariable Long id) {
        List<RatingReviewDTO> retVal = ratingService.getProcessedRatingsForEntity(id);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value = "/putForPublication", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> updateClientReviewForPublication(@Valid @RequestBody RatingReviewDTO rating) {
        ratingService.updateClientReviewForPublication(rating);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> deleteReviewById(@PathVariable Long id) {
        ratingService.deleteRatingById(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
