package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select avg(r.value) from Rating r where r.reservation.bookingEntity.id =?1 and r.processed=true")
    Float getAverageRatingForEntityId(Long id);

    @Query("select r from Rating r join fetch r.reservation res where res.id =?1")
    Optional<Rating> findByReservationId(Long id);

    @Query(value="select distinct r from Rating r left join fetch r.reservation s left join fetch s.bookingEntity b left join fetch s.client c where r.processed=true")
    List<Rating> getAllProcessedRatingsWithReservation();

    @Query(value="select distinct r from Rating r left join fetch r.reservation s left join fetch s.bookingEntity b left join fetch s.client c where r.processed=false")
    List<Rating> getAllUnprocessedRatingsWithReservation();

    @Query(value="select distinct r from Rating r left join fetch r.reservation s left join fetch s.bookingEntity b left join fetch s.client c where r.processed=true and b.id=?1")
    List<Rating> findByEntityId(Long id);
}
