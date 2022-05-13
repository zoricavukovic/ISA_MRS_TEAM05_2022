package com.example.BookingAppTeam05.repository.entities;

import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.util.Optional;

public interface BookingEntityRepository extends JpaRepository<BookingEntity, Long> {

    @Query("select b from BookingEntity b left join fetch b.place p left join fetch b.pictures c where b.id =?1 and b.deleted = false")
    BookingEntity getEntityById(Long id);

    @Query("select b from BookingEntity b left join fetch b.unavailableDates p where b.id =?1 and b.deleted = false")
    BookingEntity getEntityWithUnavailableDatesById(Long id);

    @Query(value = "update BookingEntity b SET b.deleted = true where b.id = ?1")
    @Modifying
    void logicalDeleteBookingEntityById(Long id);

    @Query("select b.entityType from BookingEntity b where b.id =?1 and b.deleted = false")
    Optional<EntityType> findEntityTypeById(Long id);
}
