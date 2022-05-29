package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.systemRevenue.SystemRevenueForPeriodDTO;
import com.example.BookingAppTeam05.dto.systemRevenue.SystemRevenuePercentageDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.SystemRevenuePercentage;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.repository.SystemRevenuePercentageRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SystemRevenuePercentageService {

    private SystemRevenuePercentageRepository systemRevenuePercentageRepository;
    private ReservationService reservationService;

    public SystemRevenuePercentageService(SystemRevenuePercentageRepository systemRevenuePercentageRepository, ReservationService reservationService) {
        this.systemRevenuePercentageRepository = systemRevenuePercentageRepository;
        this.reservationService = reservationService;
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

    public SystemRevenueForPeriodDTO getSystemRevenueDTOForPeriod(LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservations = reservationService.getAllFinishedReservations();
        double total = 0; double adventuresTotal = 0; double shipsTotal = 0; double cottagesTotal = 0;

        for (Reservation r : reservations) {
            LocalDate rEndDate = r.getEndDate().toLocalDate();
            if ((rEndDate.equals(startDate) || rEndDate.isAfter(startDate)) && (rEndDate.equals(endDate) || rEndDate.isBefore(endDate)))
            {
                double revenue =  (r.getSystemTakes() - r.getOwnerBonus() - r.getClientDiscountValue());
                total += revenue;
                if (r.getBookingEntity().getEntityType().equals(EntityType.ADVENTURE)) {
                    adventuresTotal += revenue;
                } else if (r.getBookingEntity().getEntityType().equals(EntityType.SHIP)) {
                    shipsTotal += revenue;
                } else {
                    cottagesTotal += revenue;
                }
            }
        }
        return new SystemRevenueForPeriodDTO(total, shipsTotal, adventuresTotal, cottagesTotal);
    }

    public SystemRevenueForPeriodDTO getSystemRevenueDTOForAll() {
        List<Reservation> reservations = reservationService.getAllFinishedReservations();
        double total = 0; double adventuresTotal = 0; double shipsTotal = 0; double cottagesTotal = 0;

        for (Reservation r : reservations) {
            double revenue =  (r.getSystemTakes() - r.getOwnerBonus() - r.getClientDiscountValue());
            total += revenue;
            if (r.getBookingEntity().getEntityType().equals(EntityType.ADVENTURE)) {
                adventuresTotal += revenue;
            } else if (r.getBookingEntity().getEntityType().equals(EntityType.SHIP)) {
                shipsTotal += revenue;
            } else {
                cottagesTotal += revenue;
            }
        }
        return new SystemRevenueForPeriodDTO(total, shipsTotal, adventuresTotal, cottagesTotal);
    }
}
