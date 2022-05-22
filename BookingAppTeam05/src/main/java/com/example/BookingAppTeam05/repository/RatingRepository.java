package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select avg(r.value) from Rating r where r.reservation.bookingEntity.id =?1")
    Float getAverageRatingForEntityId(Long id);

    @Query("select r from Rating r join fetch r.reservation res where res.id =?1")
    Optional<Rating> findByReservationId(Long id);
}
