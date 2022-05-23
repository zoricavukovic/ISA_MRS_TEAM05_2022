package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.AnalyticsDTO;
import com.example.BookingAppTeam05.dto.calendar.CalendarEntryDTO;
import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.UnavailableDate;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class CalendarService {

    private BookingEntityService bookingEntityService;
    private ReservationService reservationService;
    private PricelistService pricelistService;

    @Autowired
    public CalendarService(BookingEntityService bookingEntityService, ReservationService reservationService, PricelistService pricelistService) {
        this.bookingEntityService = bookingEntityService;
        this.reservationService = reservationService;
        this.pricelistService = pricelistService;
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


    public List<AnalyticsDTO> getAnalyticsWeekDTOByEntityId(Long id) {
        List<Reservation> reservations = getReservations(id);
        List<AnalyticsDTO> retVal = getNumOfResPerWeek(reservations);
        retVal.sort(Comparator.comparing(AnalyticsDTO::getStartDate));
        return retVal;
    }

    public List<AnalyticsDTO> getAnalyticsMonthDTOByEntityId(Long id) {
        List<Reservation> reservations = getReservations(id);
        List<AnalyticsDTO> retVal = getNumOfResPerMonth(reservations);
        retVal.sort(Comparator.comparing(AnalyticsDTO::getStartDate));
        return retVal;
    }

    public List<AnalyticsDTO> getAnalyticsYearDTOByEntityId(Long id) {
        List<Reservation> reservations = getReservations(id);
        List<AnalyticsDTO> retVal = getNumOfResPerYear(reservations);
        retVal.sort(Comparator.comparing(AnalyticsDTO::getStartDate));
        return retVal;
    }

    private List<AnalyticsDTO> getNumOfResPerWeek(List<Reservation> reservations) {
        List<AnalyticsDTO> retVal = new ArrayList<>();
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (Reservation reservation: reservations){
            boolean found = false;
            for (AnalyticsDTO analyticsDTO: retVal) {
                if (inSameCalendarWeek(reservation.getStartDate(), analyticsDTO.getStartDate())) {
                    found = true;
                    analyticsDTO.setNumOfReservationPerWeek(analyticsDTO.getNumOfReservationPerWeek() + 1);
                    analyticsDTO.setSumCost(analyticsDTO.getSumCost() + reservation.getCost());
                }
            }
            if (!found){
                retVal.add(new AnalyticsDTO(reservation.getStartDate(), reservation.getEndDate(),
                        customFormatter.format(reservation.getStartDate()),1, 0, 0,
                        reservation.getCost()));
            }
        }
        return retVal;
    }
    private List<AnalyticsDTO> getNumOfResPerMonth(List<Reservation> reservations) {
        List<AnalyticsDTO> retVal = new ArrayList<>();
        for (Reservation reservation: reservations){
            boolean found = false;
            for (AnalyticsDTO analyticsDTO: retVal) {
                if (reservation.getStartDate().getMonth().equals(analyticsDTO.getStartDate().getMonth())) {
                    found = true;
                    analyticsDTO.setNumOfReservationPerMonth(analyticsDTO.getNumOfReservationPerMonth() + 1);
                    analyticsDTO.setSumCost(analyticsDTO.getSumCost() + reservation.getCost());
                }
            }
            if (!found){
                retVal.add(new AnalyticsDTO(reservation.getStartDate(), reservation.getEndDate(),
                        reservation.getStartDate().getMonth().toString(),0,
                        1, 0, reservation.getCost()));
            }
        }
        return retVal;
    }

    private List<AnalyticsDTO> getNumOfResPerYear(List<Reservation> reservations) {
        List<AnalyticsDTO> retVal = new ArrayList<>();
        for (Reservation reservation: reservations){
            boolean found = false;
            for (AnalyticsDTO analyticsDTO: retVal) {
                if (reservation.getStartDate().getYear() == analyticsDTO.getStartDate().getYear()) {
                    found = true;
                    analyticsDTO.setNumOfReservationPerYear(analyticsDTO.getNumOfReservationPerYear() + 1);
                    analyticsDTO.setSumCost(analyticsDTO.getSumCost() + reservation.getCost());
                }
            }
            if (!found){
                retVal.add(new AnalyticsDTO(reservation.getStartDate(), reservation.getEndDate(), Integer.toString(reservation.getStartDate().getYear()),
                        0, 0, 1, reservation.getCost()));
            }
        }
        return retVal;
    }

    private List<Reservation> getReservations(Long id) {
        List<Reservation> reservations = reservationService.findAllReservationsWithClientsForEntityId(id);
        List<Reservation> fastReservations = reservationService.findAllFastReservationsForEntityid(id);
        reservations.addAll(fastReservations);
        return reservations;
    }

    public boolean inSameCalendarWeek(LocalDateTime firstDate, LocalDateTime secondDate) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int firstDatesCalendarWeek = firstDate.get(weekFields.weekOfWeekBasedYear());
        int secondDatesCalendarWeek = secondDate.get(weekFields.weekOfWeekBasedYear());

        int firstWeekBasedYear = firstDate.get(weekFields.weekBasedYear());
        int secondWeekBasedYear = secondDate.get(weekFields.weekBasedYear());

        return firstDatesCalendarWeek == secondDatesCalendarWeek
                && firstWeekBasedYear == secondWeekBasedYear;
    }
}
