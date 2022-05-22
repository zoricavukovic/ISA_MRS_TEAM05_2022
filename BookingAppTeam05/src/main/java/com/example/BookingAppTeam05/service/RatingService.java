package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.RatingDTO;
import com.example.BookingAppTeam05.model.Rating;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.repository.RatingRepository;
import com.example.BookingAppTeam05.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    private RatingRepository ratingRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, ReservationRepository reservationRepository) {
        this.ratingRepository = ratingRepository;
        this.reservationRepository = reservationRepository;
    }

    public Float getAverageRatingForEntityId(Long id) {
        return this.ratingRepository.getAverageRatingForEntityId(id);
    }

    public RatingDTO findByReservationId(Long id) {
        Rating rating = ratingRepository.findByReservationId(id).orElse(null);
        if(rating == null)
            return null;
        return new RatingDTO(rating);
    }

    public Rating createRating(RatingDTO ratingDTO) {
        Rating rating = new Rating();
        rating.setComment(ratingDTO.getComment());
        rating.setValue(ratingDTO.getValue());
        Reservation res = reservationRepository.findById(ratingDTO.getReservation().getId()).orElse(null);
        rating.setReservation(res);
        ratingRepository.save(rating);
        return rating;
    }
}
