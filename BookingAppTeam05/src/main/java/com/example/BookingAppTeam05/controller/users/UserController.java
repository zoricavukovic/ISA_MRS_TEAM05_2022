package com.example.BookingAppTeam05.controller.users;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.dto.users.*;
import com.example.BookingAppTeam05.model.LoyaltyProgramEnum;
import com.example.BookingAppTeam05.service.PlaceService;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public UserController(UserService userService, BookingEntityService bookingEntityService) {
        this.userService = userService;
        this.bookingEntityService = bookingEntityService;
    }

    public UserController(){}

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsersDTO();
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        UserDTO userDTO = userService.findUserDTOById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long userId, @RequestBody UserDTO userDTO)  {
        userService.updateUser(userId, userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO);
        return new ResponseEntity<>("Successfully changed password", HttpStatus.OK);
    }

    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@Valid  @RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return new ResponseEntity<>("Successfully created user.", HttpStatus.CREATED);
    }

    @GetMapping(value="/activateAccount/{email}")
    public ResponseEntity<String> activateAccount(@PathVariable String email) {
        userService.activateAccount(email);
        return new ResponseEntity<>("Account is activated.", HttpStatus.OK);
    }

    @GetMapping(value="/checkIfEmailAlreadyExist/{email}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> checkIfCanEdit(@PathVariable String email) {
        userService.checkIfCanEditUser(email);
        return new ResponseEntity<>("Email is unique.", HttpStatus.OK);
    }

    @GetMapping(value="/getAllNewAccountRequests")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<NewAccountRequestDTO>> getAllNewAccountRequests() {
        List<NewAccountRequestDTO> retVal = userService.getAllNewAccountRequestDTOs();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value="/giveResponseForNewAccountRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> giveResponse(@RequestBody NewAccountRequestDTO d) {
        userService.giveResponseForNewAccountRequest(d);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PostMapping("/subscribe/{clientId}/{entityId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<SearchedBookingEntityDTO>> subscribeClientWithEntity(@PathVariable(value = "clientId") Long clientId, @PathVariable(value = "entityId") Long entityId)  {
        List<SearchedBookingEntityDTO> retVal = bookingEntityService.subscribeClientWithEntity(clientId, entityId);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PostMapping("/unsubscribe/{clientId}/{entityId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<SearchedBookingEntityDTO>> unsubscribeClientWithEntity(@PathVariable(value = "clientId") Long clientId, @PathVariable(value = "entityId") Long entityId)  {
        List<SearchedBookingEntityDTO> retVal  = bookingEntityService.unsubscribeClientWithEntity(clientId, entityId);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @DeleteMapping(value="/{userId}/{adminId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> logicalDeleteUserById(@PathVariable Long userId, @PathVariable  Long adminId, @RequestBody String confirmPass){
        userService.tryToLogicalDeleteUser(userId, adminId, confirmPass);
        return new ResponseEntity<>("User successfully deleted.", HttpStatus.OK);
    }


}

