package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.UnavailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Long> {

    @Query(value="select * from unavailable_dates where entity_id=?1 order by start_time", nativeQuery = true)
    List<UnavailableDate> findAllSortedUnavailableDatesForEntityId(Long id);
}
