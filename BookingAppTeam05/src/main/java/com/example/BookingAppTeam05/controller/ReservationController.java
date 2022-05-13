package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.service.entities.CottageService;
import com.example.BookingAppTeam05.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
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

    @GetMapping(value="/fast/{bookingEntityId}")
    public ResponseEntity<List<ReservationDTO>> getFastReservationsByBookingEntityId(@PathVariable Long bookingEntityId) {
        List<ReservationDTO> reservationDTOs = getFastReservationDTOS(bookingEntityId);
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
        return getReservationDTOS(reservationsFound);
    }

    private List<ReservationDTO> getFastReservationDTOS(Long bookingEntityId) {
        List<Reservation> reservationsFound = reservationService.getFastReservationsByBookingEntityId(bookingEntityId);
        return getReservationDTOS(reservationsFound);
    }

    private List<ReservationDTO> getReservationDTOS(List<Reservation> reservationsFound) {
        List<ReservationDTO> reservationDTOs = new ArrayList<>();

        for (Reservation reservation: reservationsFound) {
            ReservationDTO rDTO = new ReservationDTO(reservation);
            rDTO.setBookingEntity(new BookingEntityDTO(reservation.getBookingEntity()));
            if (reservation.getClient() != null)
                rDTO.setClient(new ClientDTO(reservation.getClient()));
            reservationDTOs.add(rDTO);
        }
        return reservationDTOs;
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createFastReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = reservationService.addFastReservation(reservationDTO);
        if (reservation == null){
            return new ResponseEntity<>("Cannot create fast reservation.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reservation.getId().toString(), HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(value = "/addReservation", consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        Reservation tempReservation = reservationService.addReservation(reservationDTO);
        if (tempReservation == null)
            return new ResponseEntity<>("Cannot create temporary reservation.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(tempReservation.getId().toString(), HttpStatus.CREATED);
    }

}
