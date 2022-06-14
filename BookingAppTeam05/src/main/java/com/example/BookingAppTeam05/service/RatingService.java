package com.example.BookingAppTeam05.service;
import com.example.BookingAppTeam05.dto.RatingDTO;
import com.example.BookingAppTeam05.dto.RatingReviewDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Rating;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.repository.RatingRepository;
import com.example.BookingAppTeam05.model.repository.ReservationRepository;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {
    private RatingRepository ratingRepository;
    private BookingEntityService bookingEntityService;
    private ReservationRepository reservationRepository;
    private EmailService emailService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, @Lazy BookingEntityService bookingEntityService, ReservationRepository reservationRepository, EmailService emailService) {
        this.ratingRepository = ratingRepository;
        this.bookingEntityService = bookingEntityService;
        this.reservationRepository = reservationRepository;
        this.emailService = emailService;
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
        rating.setReviewDate(LocalDateTime.now());
        rating.setProcessed(false);
        rating.setApproved(false);
        Reservation res = reservationRepository.findById(ratingDTO.getReservation().getId()).orElse(null);
        rating.setReservation(res);
        rating.setVersion(0L);
        ratingRepository.save(rating);
        return rating;
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
        try {
            Rating r = findById(rating.getId());
            if (r == null)
                return "Can't find rating with id: " + rating.getId();
            if (r.isProcessed())
                return "This rating is already processed.";

            r.setProcessed(true);
            this.ratingRepository.save(r);

            try {
                emailService.notifyOwnerAboutApprovedReviewOnHisEntity(rating);
                return null;
            } catch (Exception e) {
                return "Error happened while sending email to owner";
            }
        }
        catch (ObjectOptimisticLockingFailureException e) {
            return "Conflict seems to have occurred, another admin has reviewed this rating before you. Please refresh page and try again";
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

    public List<RatingReviewDTO> getProcessedRatingsForEntity(Long id) {
        List<Rating> ratings = ratingRepository.findByEntityId(id);
        List<RatingReviewDTO> ratingReviewDTOS = new ArrayList<>();
        for (Rating rating : ratings) {
            ratingReviewDTOS.add(new RatingReviewDTO(rating, new ReservationDTO(rating.getReservation()), new ClientDTO(rating.getReservation().getClient())) );
        }
        return ratingReviewDTOS;
    }

    public Rating save(Rating rating) {
        return this.ratingRepository.save(rating);
    }

}
