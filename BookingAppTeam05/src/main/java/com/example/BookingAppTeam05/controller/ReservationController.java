package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.ClientDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.service.CottageService;
import com.example.BookingAppTeam05.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationService reservationService;
    private CottageService cottageService;

    @Autowired
    public ReservationController(ReservationService reservationService, CottageService cottageService){
        this.reservationService = reservationService;
        this.cottageService = cottageService;
    }

    @GetMapping(value="/owner/{ownerId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByCottageOwnerId(@PathVariable Long ownerId) {
        List<ReservationDTO> reservationDTOs = getReservationDTOS(ownerId);
        return ResponseEntity.ok(reservationDTOs);
    }

    @GetMapping(value="/owner/{ownerId}/filter/{filter}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByCottageOwnerId(@PathVariable Long ownerId, @PathVariable String filter) {

        List<ReservationDTO> reservationDTOs = getReservationDTOS(ownerId);
        List<ReservationDTO> filteredReservationDTOs = new ArrayList<>();
        for (ReservationDTO reservationDTO: reservationDTOs){
            BookingEntityDTO bookingEntity = reservationDTO.getBookingEntity();
            if (bookingEntity!= null){
                if (bookingEntity.getName().equals(filter)){
                    filteredReservationDTOs.add(reservationDTO);
                }
            }
        }
        return ResponseEntity.ok(filteredReservationDTOs);
    }

    private List<ReservationDTO> getReservationDTOS(Long ownerId) {
        List<Reservation> reservationsFound = reservationService.getReservationsByCottageOwnerId(ownerId);
        List<ReservationDTO> reservationDTOs = new ArrayList<>();

        for (Reservation reservation:reservationsFound) {
            ReservationDTO rDTO = new ReservationDTO(reservation);
            rDTO.setBookingEntity(new BookingEntityDTO(reservation.getBookingEntity()));
            rDTO.setClient(new ClientDTO(reservation.getClient()));
            reservationDTOs.add(rDTO);
        }
        return reservationDTOs;
    }
}
