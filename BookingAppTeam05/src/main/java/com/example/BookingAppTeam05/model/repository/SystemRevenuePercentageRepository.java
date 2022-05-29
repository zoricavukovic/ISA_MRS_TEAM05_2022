package com.example.BookingAppTeam05.model.repository;

import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.SystemRevenuePercentage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemRevenuePercentageRepository extends JpaRepository<SystemRevenuePercentage, Long> {
    @Query(value = "select l from SystemRevenuePercentage l order by l.startDate desc")
    List<SystemRevenuePercentage> getAllSystemRevenuePercentagesOrderByStartDate();
}
