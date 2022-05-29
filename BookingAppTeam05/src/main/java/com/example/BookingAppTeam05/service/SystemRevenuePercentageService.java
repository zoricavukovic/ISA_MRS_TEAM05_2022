package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.LoyaltyProgramDTO;
import com.example.BookingAppTeam05.dto.SystemRevenuePercentageDTO;
import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.SystemRevenuePercentage;
import com.example.BookingAppTeam05.model.repository.SystemRevenuePercentageRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SystemRevenuePercentageService {

    private SystemRevenuePercentageRepository systemRevenuePercentageRepository;

    public SystemRevenuePercentageService(SystemRevenuePercentageRepository systemRevenuePercentageRepository) {
        this.systemRevenuePercentageRepository = systemRevenuePercentageRepository;
    }

    public SystemRevenuePercentage getCurrentSystemRevenuePercentage() {
        return systemRevenuePercentageRepository.getAllSystemRevenuePercentagesOrderByStartDate().get(0);
    }

    @Transactional
    public SystemRevenuePercentageDTO setNewSystemRevenuePercentageDTO(SystemRevenuePercentageDTO systemRevenuePercentageDTO) {
        SystemRevenuePercentage systemRevenuePercentage = new SystemRevenuePercentage(systemRevenuePercentageDTO.getPercentage(), LocalDateTime.now());
        SystemRevenuePercentageDTO retVal = new SystemRevenuePercentageDTO(systemRevenuePercentage);
        systemRevenuePercentageRepository.save(systemRevenuePercentage);
        return retVal;
    }
}
