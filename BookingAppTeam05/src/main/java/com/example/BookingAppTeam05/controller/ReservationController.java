package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.ReservationForClientDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    public ReservationController(){}

    @GetMapping(value="/owner/{ownerId}/{type}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByOwnerId(@PathVariable Long ownerId,@PathVariable String type) {
        List<ReservationDTO> reservationDTOs = reservationService.getReservationDTOs(ownerId, type);
        return new ResponseEntity<>(reservationDTOs, HttpStatus.OK);
    }

    @GetMapping(value="/client/{clientId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByClientId(@PathVariable Long clientId) {
        List<ReservationDTO> reservationDTOs = reservationService.getReservationsByClientId(clientId);
        return new ResponseEntity<>(reservationDTOs, HttpStatus.OK);
    }

    @GetMapping(value="/fast/{bookingEntityId}")
    public ResponseEntity<List<ReservationDTO>> getFastReservationsByBookingEntityId(@PathVariable Long bookingEntityId) {
        List<ReservationDTO> reservationDTOs = reservationService.getFastReservationDTOs(bookingEntityId);
        return new ResponseEntity<>(reservationDTOs, HttpStatus.OK);
    }

    @GetMapping(value="/fastAvailable/{bookingEntityId}")
    public ResponseEntity<List<ReservationDTO>> getFastAvailableReservationsByBookingEntityId(@PathVariable Long bookingEntityId) {
        List<ReservationDTO> reservationDTOs = reservationService.getFastAvailableReservationsDTO(bookingEntityId);
        return new ResponseEntity<>(reservationDTOs, HttpStatus.OK);
    }

    @GetMapping(value="/owner/{ownerId}/{type}/filter/name/{name}/time/{time}")
    public ResponseEntity<List<ReservationDTO>> filterReservationsByOwnerId(@PathVariable Long ownerId, @PathVariable String type, @PathVariable String name, @PathVariable String time) {
        List<ReservationDTO> filteredReservationDTOs = reservationService.filterReservationsByOwnerId(ownerId, type, name, time);
        return new ResponseEntity<>(filteredReservationDTOs, HttpStatus.OK);
    }

    @GetMapping(value="/bookingEntity/{bookingEntityId}")
    public ResponseEntity<List<String>> findAllClientsWithActiveReservations(@PathVariable Long bookingEntityId) {
        List<String> clients = reservationService.findAllClientsWithActiveReservations(bookingEntityId);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createFastReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = reservationService.addFastReservation(reservationDTO);
        return new ResponseEntity<>(reservation.getId().toString(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/reserveFastReservation", consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> reserveFastReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        Reservation tempReservation = reservationService.reserveFastReservation(reservationDTO);
        return new ResponseEntity<>(tempReservation.getId().toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/addReservation", consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        Reservation tempReservation = reservationService.addReservation(reservationDTO);
        return new ResponseEntity<>(tempReservation.getId().toString(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/addReservationForClient", consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createReservationForClient(@Valid @RequestBody ReservationForClientDTO reservationDTO) {
        return new ResponseEntity<>(reservationService.addReservationForClient(reservationDTO), HttpStatus.CREATED);
    }

    @PostMapping(value="/cancel/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
