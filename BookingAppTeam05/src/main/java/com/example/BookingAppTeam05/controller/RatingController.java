package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.RatingDTO;
import com.example.BookingAppTeam05.model.Rating;
import com.example.BookingAppTeam05.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rating")
public class RatingController {
    private RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/byReservationId/{id}")
    public ResponseEntity<RatingDTO> getReservationsByOwnerId(@PathVariable Long id) {
        RatingDTO ratingDTO = ratingService.findByReservationId(id);
        if(ratingDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(ratingDTO);
    }

    @PostMapping("/createRating")
    public ResponseEntity<RatingDTO> createRating(@RequestBody RatingDTO ratingDTO) {
        Rating rating = ratingService.createRating(ratingDTO);
        if(rating == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(ratingDTO);
    }
}
