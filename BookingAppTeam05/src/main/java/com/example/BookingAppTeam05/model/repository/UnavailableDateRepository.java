package com.example.BookingAppTeam05.model.repository;

import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.UnavailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Long> {

    @Query(value="select * from unavailable_dates where entity_id=?1 order by start_time", nativeQuery = true)
    List<UnavailableDate> findAllSortedUnavailableDatesForEntityId(Long id);


    @Modifying
    @Query(value="delete from UnavailableDate u where u.bookingEntity.id=?1 and u.startTime =?2 and u.endTime =?3")
    Integer deleteUnavailableDateByEntityIdAndDateRange(Long id, LocalDateTime start, LocalDateTime finish);
}
