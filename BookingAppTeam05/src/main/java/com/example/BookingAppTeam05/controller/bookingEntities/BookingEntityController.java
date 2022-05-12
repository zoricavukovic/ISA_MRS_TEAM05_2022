package com.example.BookingAppTeam05.controller.bookingEntities;

import com.example.BookingAppTeam05.dto.BookingEntityDTO;
import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.SimpleSearchForBookingEntityDTO;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/bookingEntities")
public class BookingEntityController {
    private BookingEntityService bookingEntityService;
    private UserService userService;

    @Autowired
    public BookingEntityController(BookingEntityService bookingEntityService, UserService userService) {
        this.bookingEntityService = bookingEntityService;
        this.userService = userService;
    }

    @GetMapping(value = "/byId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookingEntityDTO> getBookingEntityById(@PathVariable Long id) {
        BookingEntityDTO entity = bookingEntityService.findById(id);
        if (entity == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping(value = "/allByOwner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getAllBookingEntitiesByOwnerId(@PathVariable Long id) {
        List<SearchedBookingEntityDTO> entities = bookingEntityService.getSearchedBookingEntitiesDTOsByOnwerId(id);
        if (entities == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @GetMapping(value = "/view/{id}")
    public ResponseEntity<SearchedBookingEntityDTO> getBookingEntityInfoForView(@PathVariable Long id) {
        SearchedBookingEntityDTO s = bookingEntityService.getSearchedBookingEntityDTOByEntityId(id);
        if (s == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }


    @PostMapping(value="/simpleSearch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SearchedBookingEntityDTO>> getSearchedBookingEntities(@RequestBody SimpleSearchForBookingEntityDTO s) {
        try {
            System.out.println("usao");
            System.out.println("--------------------------------");
            System.out.println(s.getMinCostPerPerson());
            System.out.println(s.getMaxCostPerPerson());
            List<SearchedBookingEntityDTO> entities = bookingEntityService.getSearchedBookingEntitiesDTOsByOnwerId(s.getOwnerId());
            if (entities == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            entities = bookingEntityService.simpleFilterSearchForBookingEntities(entities, s);
            if (entities == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(entities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/checkIfCanEdit/{entityId}")
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<String> checkIfCanEdit(@PathVariable Long entityId) {
        BookingEntity bookingEntity = bookingEntityService.getEntityById(entityId);
        if (bookingEntity == null) return new ResponseEntity<String>("Entity for editing is not found.", HttpStatus.BAD_REQUEST);
        if (bookingEntityService.checkExistActiveReservationForEntityId(entityId)){
            return new ResponseEntity<String>("Cannot edit entity cause has reservations.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Entity can edit.", HttpStatus.OK);
    }

    @DeleteMapping(value="/{entityId}")
    @Transactional
    //@PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<String> logicalDeleteEntityById(@PathVariable Long entityId, @RequestBody String confirmPass){
        BookingEntity bookingEntity = bookingEntityService.getEntityById(entityId);
        if (bookingEntity == null)
            return new ResponseEntity<String>("Entity for deleting is not found.", HttpStatus.BAD_REQUEST);

        User user = bookingEntityService.getOwnerOfEntityId(entityId);
        System.out.println("prava");
        System.out.println(user.getPassword());
        System.out.println("nova");
        System.out.println(confirmPass);
        if (!userService.passwordIsCorrect(user, confirmPass))
            return new ResponseEntity<String>("Confirmation password is incorrect.", HttpStatus.BAD_REQUEST);

        if (!bookingEntityService.logicalDeleteBookingEntityById(entityId)){
            return new ResponseEntity<String>("Entity cant be deleted  cause has reservations.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Entity is deleted.", HttpStatus.CREATED);
    }

}
