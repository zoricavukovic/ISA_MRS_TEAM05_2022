package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Pricelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PricelistRepository extends JpaRepository<Pricelist, Long> {
    List<Pricelist> getPricelistByBookingEntity_IdOrderByStartDateDesc(Long id);

    @Query("select distinct p from Pricelist p left join fetch p.additionalServices ad where p.bookingEntity.id=?1 order by p.startDate desc")
    List<Pricelist> getCurrentPricelistByBookingEntityId(Long id);

}

