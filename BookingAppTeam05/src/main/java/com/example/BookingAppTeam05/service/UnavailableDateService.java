package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.calendar.UnavailableDateDTO;
import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.exception.database.DeleteItemException;
import com.example.BookingAppTeam05.model.UnavailableDate;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.repository.UnavailableDateRepository;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnavailableDateService {

    private UnavailableDateRepository unavailableDateRepository;
    private BookingEntityService bookingEntityService;

    @Autowired
    public UnavailableDateService(UnavailableDateRepository unavailableDateRepository, BookingEntityService bookingEntityService) {
        this.unavailableDateRepository = unavailableDateRepository;
        this.bookingEntityService = bookingEntityService;
    }

    public UnavailableDateService(){}

    public List<UnavailableDateDTO> getActiveUnavailableDateDTOsForEntityId(Long id) {
        List<UnavailableDate> all = unavailableDateRepository.findAllSortedUnavailableDatesForEntityId(id);
        List<UnavailableDateDTO> retVal = new ArrayList<>();
        for (UnavailableDate u : all) {
            if (u.getEndTime().isAfter(LocalDateTime.now()))
                retVal.add(new UnavailableDateDTO(u.getId(), id, u.getStartTime(), u.getEndTime()));
        }
        return retVal;
    }

    @Transactional
    public Integer setUnavailableDateAsAvailableForEntityId(UnavailableDateDTO u) {
        Integer res = unavailableDateRepository.deleteUnavailableDateByEntityIdAndDateRange(u.getEntityId(), u.getStartDate(), u.getEndDate());
        if (res != null && res != 0)
            return res;
        throw new DeleteItemException("Can't set unavailable date as available.");
    }

    public UnavailableDateDTO checkIfThereExistOverlapBetweenUnavailablePeriodsForEntity(UnavailableDateDTO newPeriod, Long id) {
        List<UnavailableDateDTO> all = getActiveUnavailableDateDTOsForEntityId(id);
        LocalDateTime npStart = newPeriod.getStartDate();
        LocalDateTime npEnd = newPeriod.getEndDate();

        LocalDateTime newStart = npStart;
        LocalDateTime newEnd = npEnd;
        boolean newStartSetted = false;
        boolean newEndSetted = false;

        for (UnavailableDateDTO u : all) {
            LocalDateTime uStart = u.getStartDate();
            LocalDateTime uEnd = u.getEndDate();

            if ((npStart.equals(uStart) || npStart.isAfter(uStart)) && (npEnd.equals(uEnd) || npEnd.isBefore(uEnd)))
                return u;

            if (unavailableDateIsBetween(u, npStart)) {
                newStart = uStart;
                newStartSetted = true;
            }
            if (unavailableDateIsBetween(u, npEnd)) {
                newEnd = uEnd;
                newEndSetted = true;
            }
            if (newEndSetted && newStartSetted)
                return new UnavailableDateDTO(id, newStart, newEnd);
        }
        if (newStartSetted || newEndSetted)
            return new UnavailableDateDTO(id, newStart, newEnd);
        return null;
    }

    public UnavailableDateDTO addNewUnavailableDateForEntityId(UnavailableDateDTO newPeriod) {
        List<UnavailableDateDTO> all = getActiveUnavailableDateDTOsForEntityId(newPeriod.getEntityId());

        LocalDateTime npStart = newPeriod.getStartDate();
        LocalDateTime npEnd = newPeriod.getEndDate();

        BookingEntity bookingEntity = bookingEntityService.getBookingEntityById(newPeriod.getEntityId());
        if (bookingEntityService.checkExistReservationInPeriodForEntityId(newPeriod.getEntityId(), npStart, npEnd))
            throw new CreateItemException("Can't add unavailable date cause has reservations.");

        LocalDateTime newStart = npStart;
        LocalDateTime newEnd = npEnd;
        boolean newStartSetted = false;
        boolean newEndSetted = false;
        Long newStartDateId = null;
        Long endStartDateId = null;

        for (UnavailableDateDTO u : all) {
            LocalDateTime uStart = u.getStartDate();
            LocalDateTime uEnd = u.getEndDate();

            if ((npStart.equals(uStart) || npStart.isAfter(uStart)) && (npEnd.equals(uEnd) || npEnd.isBefore(uEnd)))
                return u;
            if (uStart.isAfter(npStart) && uEnd.isBefore(npEnd))
                unavailableDateRepository.deleteById(u.getId());

            if (unavailableDateIsBetween(u, npStart)) {
                newStart = uStart;
                newStartDateId = u.getId();
                newStartSetted = true;
            }
            if (unavailableDateIsBetween(u, npEnd)) {
                endStartDateId = u.getId();
                newEnd = uEnd;
                newEndSetted = true;
            }

            if (newEndSetted && newStartSetted) {
                UnavailableDate created = new UnavailableDate(newStart, newEnd);
                created.setBookingEntity(bookingEntity);
                unavailableDateRepository.save(created);
                unavailableDateRepository.deleteById(newStartDateId);
                unavailableDateRepository.deleteById(endStartDateId);
                return new UnavailableDateDTO(created.getId(), newPeriod.getEntityId(), newStart, newEnd);
            }
        }
        UnavailableDate created = new UnavailableDate(newStart, newEnd);
        created.setBookingEntity(bookingEntity);
        unavailableDateRepository.save(created);
        if (newStartSetted) {
            unavailableDateRepository.deleteById(newStartDateId);
        } else if (newEndSetted) {
            unavailableDateRepository.deleteById(endStartDateId);
        }
        return new UnavailableDateDTO(created.getId(), newPeriod.getEntityId(), newStart, newEnd);
    }

    private boolean unavailableDateIsBetween(UnavailableDateDTO cmp, LocalDateTime date) {
        return (date.isAfter(cmp.getStartDate()) || date.equals(cmp.getStartDate())) &&
                (date.isBefore(cmp.getEndDate()) || date.equals(cmp.getEndDate()));
    }
}
