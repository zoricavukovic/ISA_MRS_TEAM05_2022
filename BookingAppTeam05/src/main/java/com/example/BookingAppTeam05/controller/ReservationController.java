package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.ReservationForClientDTO;
import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.model.users.Client;
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

    @GetMapping(value="/owner/{ownerId}/{type}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByOwnerId(@PathVariable Long ownerId,@PathVariable String type) {
        List<ReservationDTO> reservationDTOs = getReservationDTOS(ownerId, type);
        return ResponseEntity.ok(reservationDTOs);
    }

    @GetMapping(value="/client/{clientId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByClientId(@PathVariable Long clientId) {
        List<ReservationDTO> reservationDTOs = reservationService.getReservationsByClientId(clientId);
        return ResponseEntity.ok(reservationDTOs);
    }

    @GetMapping(value="/fast/{bookingEntityId}")
    public ResponseEntity<List<ReservationDTO>> getFastReservationsByBookingEntityId(@PathVariable Long bookingEntityId) {
        List<ReservationDTO> reservationDTOs = getFastReservationDTOS(bookingEntityId);
        return ResponseEntity.ok(reservationDTOs);
    }

    @GetMapping(value="/fastAvailable/{bookingEntityId}")
    public ResponseEntity<List<ReservationDTO>> getFastAvailableReservationsByBookingEntityId(@PathVariable Long bookingEntityId) {
        List<ReservationDTO> reservationDTOs = reservationService.getFastAvailableReservationsDTO(bookingEntityId);
        return ResponseEntity.ok(reservationDTOs);
    }

    @GetMapping(value="/owner/{ownerId}/{type}/filter/name/{name}/time/{time}")
    public ResponseEntity<List<ReservationDTO>> filterReservationsByOwnerId(@PathVariable Long ownerId, @PathVariable String type, @PathVariable String name, @PathVariable String time) {

        List<ReservationDTO> reservationDTOs = getReservationDTOS(ownerId, type);
        List<ReservationDTO> filteredReservationDTOs = reservationService.filterReservation(reservationDTOs, name, time);

        return ResponseEntity.ok(filteredReservationDTOs);
    }

    private List<ReservationDTO> getReservationDTOS(Long ownerId, String type) {
        List<Reservation> reservationsFound = reservationService.getReservationsByOwnerId(ownerId, type);
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

    @GetMapping(value="/bookingEntity/{bookingEntityId}")
    public ResponseEntity<List<String>> findAllClientsWithActiveReservations(@PathVariable Long bookingEntityId) {
        List<String> clients = reservationService.findAllClientsWithActiveReservations(bookingEntityId);
        return ResponseEntity.ok(clients);
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
    @PostMapping(value = "/reserveFastReservation", consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> reserveFastReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        Reservation tempReservation = reservationService.reserveFastReservation(reservationDTO);
        if (tempReservation == null)
            return new ResponseEntity<>("Cannot create temporary reservation.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(tempReservation.getId().toString(), HttpStatus.CREATED);
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

    @Transactional
    @PostMapping(value = "/addReservationForClient", consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createReservationForClient(@Valid @RequestBody ReservationForClientDTO reservationDTO) {
        Reservation tempReservation = reservationService.addReservationForClient(reservationDTO);
        if (tempReservation == null)
            return new ResponseEntity<>("Cannot create temporary reservation.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(tempReservation.getId().toString(), HttpStatus.CREATED);
    }

    @PostMapping(value="/cancel/{Id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> cancelReservation(@PathVariable Long Id) {
        reservationService.cancelReservation(Id);
        return ResponseEntity.ok("Sve je dobro");
    }

}
