package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.entities.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.SimpleSearchForBookingEntityOwnerDTO;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookingEntities")
public class BookingEntityController {
    private BookingEntityService bookingEntityService;

    @Autowired
    public BookingEntityController(BookingEntityService bookingEntityService) {
        this.bookingEntityService = bookingEntityService;
    }

    public BookingEntityController(){}

    @GetMapping(value = "/byId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingEntityDTO> getBookingEntityById(@PathVariable Long id) {
        BookingEntityDTO entity = bookingEntityService.findById(id);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping(value = "/allByOwner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER', 'ROLE_INSTRUCTOR', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<SearchedBookingEntityDTO>> getAllBookingEntitiesByOwnerId(@PathVariable Long id) {
        List<SearchedBookingEntityDTO> entities = bookingEntityService.getSearchedBookingEntitiesDTOsByOwnerId(id);
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @GetMapping(value = "/view/{id}")
    public ResponseEntity<SearchedBookingEntityDTO> getBookingEntityInfoForView(@PathVariable Long id) {
        SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(id);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @GetMapping(value = "/subscribedEntities/{clientId}")
    public ResponseEntity<List<SearchedBookingEntityDTO>> getSubscribedBookingEntityForClient(@PathVariable Long clientId) {
        List<SearchedBookingEntityDTO> retVal = bookingEntityService.getSubscribedEntitiesForClient(clientId);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PostMapping(value="/simpleSearch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getSearchedBookingEntitiesForOwner(@RequestBody SimpleSearchForBookingEntityOwnerDTO s) {
        List<SearchedBookingEntityDTO> entities = bookingEntityService.getSearchedBookingEntitiesDTOsByOwnerId(s.getOwnerId());
        entities = bookingEntityService.simpleFilterSearchForBookingEntities(entities, s);
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @GetMapping(value="/checkIfCanEdit/{entityId}")
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<String> checkIfCanEdit(@PathVariable Long entityId) {
        bookingEntityService.checkIfCanEdit(entityId);
        return new ResponseEntity<>("Entity can edit.", HttpStatus.OK);
    }

    @DeleteMapping(value="/{entityId}/{ownerId}")
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER', 'ROLE_INSTRUCTOR', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> logicalDeleteEntityById(@PathVariable Long entityId, @PathVariable  Long ownerId, @RequestBody String confirmPass){
        bookingEntityService.tryToLogicalDeleteBookingEntity(entityId, ownerId, confirmPass);
        return new ResponseEntity<>("Entity successfully deleted.", HttpStatus.OK);
    }

    @GetMapping(value="/getAllForOwnerId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER', 'ROLE_INSTRUCTOR', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<BookingEntityDTO>> getBookingEntitiesForOwnerId(@PathVariable Long id) {
        List<BookingEntityDTO> bookingEntityDTOs = bookingEntityService.getBookingEntitiesDTOsForOwnerId(id);
        return new ResponseEntity<>(bookingEntityDTOs, HttpStatus.OK);
    }
}
