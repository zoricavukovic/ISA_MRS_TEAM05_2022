package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.LoyaltyProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, Long> {
    @Query(value = "select l from LoyaltyProgram l order by l.startDate desc")
    List<LoyaltyProgram> getAllSortedByTime();
}
