package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value="select r from Reservation r where r.bookingEntity.id=?1 and r.canceled=false")
    List<Reservation> findAllActiveReservationsForCottage(Long cottageOwner);

    @Query(value="select distinct r from Reservation r left join fetch r.bookingEntity b left join fetch r.client c where r.bookingEntity.id=?1")
    List<Reservation> getReservationsByCottageId(Long cottageId);

    @Query(value="select distinct r from Reservation  r left join fetch r.client c where r.bookingEntity.id=?1 and r.canceled=false and r.fastReservation=false")
    List<Reservation> findAllReservationsWithClientsForEntityId(Long id);

    @Query(value="select distinct r from Reservation  r where r.bookingEntity.id=?1 and r.canceled=false")
    List<Reservation> findAllRegularAndFastReservationsForEntityId(Long id);

    @Query(value="select distinct r from Reservation  r where r.bookingEntity.id=?1 and r.fastReservation = true and r.canceled=false")
    List<Reservation> findAllFastReservationsForEntityId(Long id);

    @Query(value="select distinct r from Reservation r left join fetch r.bookingEntity b left join fetch r.client c where r.bookingEntity.id=?1 and r.fastReservation=true and r.canceled=false")
    List<Reservation> getFastReservationsByBookingEntityId(Long bookingEntityId);
}
