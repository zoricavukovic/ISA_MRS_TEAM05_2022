package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.UnavailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Long> {


//    @Query("select distinct p from Pricelist p left join fetch p.additionalServices ad where p.bookingEntity.id=?1 order by p.startDate desc")
//    List<Pricelist> getCurrentPricelistByBookingEntityId(Long id);

    @Query("select distinct u from UnavailableDate u order by u.startTime desc ")
    List<UnavailableDate> findAll();
}
