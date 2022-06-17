package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.RatingDTO;
import com.example.BookingAppTeam05.dto.RatingReviewDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.exception.ConflictException;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.NotificationByEmailException;
import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.exception.database.DeleteItemException;
import com.example.BookingAppTeam05.model.Rating;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.repository.RatingRepository;
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
    private ReservationService reservationService;
    private EmailService emailService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, @Lazy BookingEntityService bookingEntityService,
                         ReservationService reservationService, EmailService emailService) {
        this.ratingRepository = ratingRepository;
        this.bookingEntityService = bookingEntityService;
        this.reservationService = reservationService;
        this.emailService = emailService;
    }

    public RatingService(){}

    public Float getAverageRatingForEntityId(Long id) {
        return this.ratingRepository.getAverageRatingForEntityId(id);
    }

    public RatingDTO findRatingByReservationId(Long id) {
        Rating rating = ratingRepository.findByReservationId(id).orElse(null);
        if(rating == null)
            throw new ItemNotFoundException("Can't find rating for this reservation.");
        return new RatingDTO(rating);
    }

    public void createRating(RatingDTO ratingDTO) {
        Rating rating = new Rating();
        rating.setComment(ratingDTO.getComment());
        rating.setValue(ratingDTO.getValue());
        rating.setReviewDate(LocalDateTime.now());
        rating.setProcessed(false);
        rating.setApproved(false);
        if (ratingDTO.getReservation() == null)
            throw new CreateItemException("Can't create rating without set reservation");
        Reservation res = reservationService.findById(ratingDTO.getReservation().getId());
        if (res == null)
            throw new ItemNotFoundException("Can't find reservation for rate.");
        rating.setReservation(res);
        rating.setVersion(0L);
        ratingRepository.save(rating);
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
    public void setRatingForPublicationAndNotifyOwner(RatingReviewDTO rating) {
        try {
            Rating r = findById(rating.getId());
            if (r == null)
                throw new ItemNotFoundException("Error. Can't find rating with id: " + rating.getId());
            if (r.isProcessed())
                throw new ItemNotFoundException("This rating is already processed.");
            r.setProcessed(true);
            this.ratingRepository.save(r);
            try {
                emailService.notifyOwnerAboutApprovedReviewOnHisEntity(rating);
            } catch (InterruptedException e) {
                throw new NotificationByEmailException("Error happened while sending email to owner.");
            }
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException("Conflict seems to have occurred, another admin has reviewed this rating before you. Please refresh page and try again");
        }
    }

    @Transactional
    public void deleteRatingById(Long id) {
        Rating r = findById(id);
        if (r == null)
            throw new ItemNotFoundException("Error. Can't find rating with id: " + id);
        try {
            ratingRepository.deleteById(id);
        } catch (Exception e) {
            throw new DeleteItemException("Error happened while trying to delete rating with id: " + id);
        }
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

    public List<RatingReviewDTO> getListOfRatingReviewDTO(String type) {
        List<RatingReviewDTO> retVal = new ArrayList<>();
        if (type.equals("processed"))
            retVal = getAllProcessedRatingReviewDTOs();
        else if (type.equals("unprocessed"))
            retVal = getAllUnprocessedRatingReviewDTOs();
        if (retVal == null)
            throw new ItemNotFoundException("Can't find created rating reviews for type " + type);
        return retVal;
    }

    public void updateClientReviewForPublication(RatingReviewDTO rating) {
        Rating r = findById(rating.getId());
        if (r == null)
            throw new ItemNotFoundException("Error. Can't find rating with id: " + rating.getId());
        setRatingForPublicationAndNotifyOwner(rating);
    }
}
