package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.RatingReviewDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Rating;
import com.example.BookingAppTeam05.repository.RatingRepository;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {
    private RatingRepository ratingRepository;
    private BookingEntityService bookingEntityService;
    private EmailService emailService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, @Lazy BookingEntityService bookingEntityService, EmailService emailService) {
        this.ratingRepository = ratingRepository;
        this.bookingEntityService = bookingEntityService;
        this.emailService = emailService;
    }

    public Float getAverageRatingForEntityId(Long id) {
        return this.ratingRepository.getAverageRatingForEntityId(id);
    }

    public List<RatingReviewDTO> getAllProcessedRatingReviewDTOs() {
        List<Rating> ratings = ratingRepository.getAllProcessedRatingsWithReservation();
        return createRatingReviewDTOsFromRatingsList(ratings);
    }

    public List<RatingReviewDTO> getAllUnprocessedRatingReviewDTOs() {
        List<Rating> ratings = ratingRepository.getAllUnprocessedRatingsWithReservation();
        return createRatingReviewDTOsFromRatingsList(ratings);
    }

    private List<RatingReviewDTO> createRatingReviewDTOsFromRatingsList(List<Rating> ratings) {
        List<RatingReviewDTO> retVal = new ArrayList<>();
        for (Rating rating : ratings) {
            ReservationDTO reservationDTO = new ReservationDTO(rating.getReservation());
            reservationDTO.setClient(new ClientDTO(rating.getReservation().getClient()));
            reservationDTO.setBookingEntity(new BookingEntityDTO(rating.getReservation().getBookingEntity()));
            UserDTO ownerDTO = new UserDTO(bookingEntityService.getOwnerOfEntityId(rating.getReservation().getBookingEntity().getId()));
            RatingReviewDTO r = new RatingReviewDTO(rating, reservationDTO, ownerDTO);
            retVal.add(r);
        }
        return retVal;
    }

    public Rating findById(Long id) {
        return this.ratingRepository.findById(id).orElse(null);
    }

    @Transactional
    public String setRatingForPublicationAndNotifyOwner(RatingReviewDTO rating) {
        this.ratingRepository.setRatingForPublication(rating.getId());
        try {
            emailService.notifyOwnerAboutApprovedReviewOnHisEntity(rating);
            return null;
        } catch (Exception e) {
            return "Error happened while sending email to owner";
        }
    }

    @Transactional
    public String deleteRatingById(Long id) {
        Rating r = ratingRepository.findById(id).orElse(null);
        if (r == null)
            return "Can't find rating with id: " + id;
        try {
            ratingRepository.deleteById(id);
        } catch (Exception e) {
            return "Error happened while trying to delete rating with id: " + id;
        }
        return null;
    }
}
