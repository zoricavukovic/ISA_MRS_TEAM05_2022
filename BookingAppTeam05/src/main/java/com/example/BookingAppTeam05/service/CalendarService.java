package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.CalendarEntryDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.UnavailableDate;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CalendarService {

    private BookingEntityService bookingEntityService;
    private ReservationService reservationService;

    @Autowired
    public CalendarService(BookingEntityService bookingEntityService, ReservationService reservationService) {
        this.bookingEntityService = bookingEntityService;
        this.reservationService = reservationService;
    }

    public List<CalendarEntryDTO> getCalendarEntriesDTOByEntityId(Long id) {
        List<CalendarEntryDTO> retVal = new ArrayList<>();
        BookingEntity bookingEntity = bookingEntityService.getBookingEntityWithUnavailableDatesById(id);
        fillResultWithUnavailableDates(retVal, bookingEntity.getUnavailableDates());

        List<Reservation> reservations = reservationService.findAllReservationsWithClientsForEntityId(bookingEntity.getId());
        fillResultWithReservations(retVal, reservations);

        List<Reservation> fastReservations = reservationService.findAllFastReservationsForEntityid(bookingEntity.getId());
        fillResultWithFastReservations(retVal, fastReservations);
        return retVal;
    }

    private void fillResultWithFastReservations(List<CalendarEntryDTO> calendarEntryDTOS, List<Reservation> fastReservations) {
        for (Reservation r : fastReservations) {
            if (r.getEndDate().isBefore(LocalDateTime.now()))
                calendarEntryDTOS.add(new CalendarEntryDTO(r.getStartDate(), r.getEndDate(), "fast reservation", "Fast reservation expired"));
            else
                calendarEntryDTOS.add(new CalendarEntryDTO(r.getStartDate(), r.getEndDate(), "fast reservation", "Fast reservation"));
        }
    }

    private void fillResultWithUnavailableDates(List<CalendarEntryDTO> calendarEntries, Set<UnavailableDate> unavailableDates) {
        for (UnavailableDate u : unavailableDates) {
            calendarEntries.add(new CalendarEntryDTO(u.getStartTime(), u.getEndTime(), "unavailable", "Unavailable Period"));
        }
    }
    private void fillResultWithReservations(List<CalendarEntryDTO> calendarEntryDTOS, List<Reservation> reservations) {
        for (Reservation r : reservations) {
            String clientTitle = r.getClient().getFirstName() + " " + r.getClient().getLastName();
            calendarEntryDTOS.add(new CalendarEntryDTO(r.getStartDate(), r.getEndDate(), "regular reservation", clientTitle));
        }
    }



}
