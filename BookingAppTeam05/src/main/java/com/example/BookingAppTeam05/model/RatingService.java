package com.example.BookingAppTeam05.model;

import com.example.BookingAppTeam05.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    private RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Float getAverageRatingForEntityId(Long id) {
        return this.ratingRepository.getAverageRatingForEntityId(id);
    }
}
