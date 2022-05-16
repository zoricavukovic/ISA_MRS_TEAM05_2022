package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.calendar.UnavailableDateDTO;
import com.example.BookingAppTeam05.model.UnavailableDate;
import com.example.BookingAppTeam05.repository.UnavailableDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnavailableDateService {

    private UnavailableDateRepository unavailableDateRepository;

    @Autowired
    public UnavailableDateService(UnavailableDateRepository unavailableDateRepository) {
        this.unavailableDateRepository = unavailableDateRepository;
    }

    private List<UnavailableDateDTO> getActiveUnavailableDateDTOs() {
        List<UnavailableDate> all = unavailableDateRepository.findAll();
        List<UnavailableDateDTO> retVal = new ArrayList<>();
        for (UnavailableDate u : all) {
            if (u.getEndTime().isAfter(LocalDateTime.now()))
                retVal.add(new UnavailableDateDTO(u.getStartTime(), u.getEndTime()));
        }
        return retVal;
    }


    public UnavailableDateDTO checkIfThereExistOverlapBetweenUnavailablePeriods(UnavailableDateDTO newPeriod) {
        List<UnavailableDateDTO> all = getActiveUnavailableDateDTOs();
        LocalDateTime npStart = newPeriod.getStartDate();
        LocalDateTime npEnd = newPeriod.getEndDate();

        for (UnavailableDateDTO u : all) {
            LocalDateTime uStart = u.getStartDate();
            LocalDateTime uEnd = u.getEndDate();

            if (npStart.equals(uStart) && npEnd.equals(uEnd))
                return null;
            if (npStart.isAfter(uStart) && npEnd.isBefore(uEnd))
                return null;

            LocalDateTime newStart = npStart;
            LocalDateTime newEnd = npEnd;
            boolean overlap = false;

            if (unavailableDateIsBetween(u, npStart)) {
                overlap = true;
                newStart = uStart;
            }
            if (unavailableDateIsBetween(u, npEnd)) {
                overlap = true;
                newEnd = uEnd;
            }
            if (overlap)
                return new UnavailableDateDTO(newStart, newEnd);
        }
        return null;
    }

    private boolean unavailableDateIsBetween(UnavailableDateDTO cmp, LocalDateTime date) {
        return date.isAfter(cmp.getStartDate()) &&
                (date.isBefore(cmp.getEndDate()) || date.equals(cmp.getEndDate()));
    }


}
