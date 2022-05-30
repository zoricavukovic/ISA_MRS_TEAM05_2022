package com.example.BookingAppTeam05.controller.users;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.dto.users.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.users.*;
import com.example.BookingAppTeam05.service.PlaceService;
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
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private PlaceService placeService;

    @Autowired
    public UserController(UserService userService, PlaceService placeService) {

        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping(value="/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        User u = userService.findUserById(id);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO(u);
        userDTO.setPlace(u.getPlace());
        if (u.getRole().getName().equals("ROLE_CLIENT")) {
            userDTO.setPenalties(((Client) u).getPenalties());
        }
        if (u.getRole().getName().equals("ROLE_SHIP_OWNER")) {
            userDTO.setCaptain(((ShipOwner) u).isCaptain());
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long userId, @RequestBody UserDTO userDTO)  {
        User updatedUser = null;
        try {
            updatedUser = userService.updateUser(userId, userDTO);
        }
        catch(Exception e)
        {
            return (ResponseEntity<UserDTO>) ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }

    @PostMapping(value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        User user = userService.findUserById(changePasswordDTO.getId());
        if (userService.passwordIsCorrect(user, changePasswordDTO.getNewPassword()))
            return new ResponseEntity<String>("Please choose different password", HttpStatus.BAD_REQUEST);
        if (userService.passwordIsCorrect(user, changePasswordDTO.getCurrPassword())) {
            userService.setNewPasswordForUser(user, changePasswordDTO.getNewPassword());
            return new ResponseEntity<String>("Successfully changed password", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<String>("Entered password is incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@Valid  @RequestBody UserDTO userDTO) {
        String created = userService.createUser(userDTO);
        if (created == null){
            return new ResponseEntity<String>("Successfully created user.", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(created + "", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value="/activateAccount/{email}")
    public ResponseEntity<String> activateAccount(@PathVariable String email) {
        return userService.activateAccount(email);
    }

    @GetMapping(value="/checkIfEmailAlreadyExist/{email}")
    public ResponseEntity<String> checkIfCanEdit(@PathVariable String email) {
        User user = userService.findUserByEmail(email);
        if (user != null)
            return new ResponseEntity<String>("This email is already taken.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Email is unique.", HttpStatus.OK);
    }

    @GetMapping(value="/getAllNewAccountRequests")
    public ResponseEntity<List<NewAccountRequestDTO>> getAllNewAccountRequests() {
        List<NewAccountRequestDTO> retVal = userService.getAllNewAccountRequestDTOs();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value="/giveResponseForNewAccountRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> giveResponse(@RequestBody NewAccountRequestDTO d) {
        String errorCode = userService.giveResponseForNewAccountRequest(d);
        if (errorCode != null)
            return new ResponseEntity<>(errorCode, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("sve ok", HttpStatus.OK);
    }
}

