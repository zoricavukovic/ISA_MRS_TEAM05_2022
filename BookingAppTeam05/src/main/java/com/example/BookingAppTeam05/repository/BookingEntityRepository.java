package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;

public interface BookingEntityRepository extends JpaRepository<BookingEntity, Long> {

    @Query("select b from BookingEntity b left join fetch b.place p left join fetch b.pictures c where b.id =?1 and b.deleted = false")
    BookingEntity getEntityById(Long id);

    @Query("select b from BookingEntity b left join fetch b.unavailableDates p where b.id =?1 and b.deleted = false")
    BookingEntity getEntityWithUnavailableDatesById(Long id);
}
