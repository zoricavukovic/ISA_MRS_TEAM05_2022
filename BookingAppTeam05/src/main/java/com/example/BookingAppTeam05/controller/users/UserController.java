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
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        User u = userService.findUserById(id);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UserDTO userDTO = new UserDTO(u);
        userDTO.setPlace(u.getPlace());
        if (u.getRole().getName().equals("ROLE_CLIENT")) {
            Client client = (Client) u;
            userDTO.setPenalties(client.getPenalties());
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @RequestBody UserDTO userDTO)  {
        User user = userService.findUserById(userId);

        user.setAddress(userDTO.getAddress());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        Place place = placeService.getPlaceByZipCode(userDTO.getPlace().getZipCode());
        user.setPlace(place);
        final User updatedUser;
        try {
            updatedUser = userService.save(user);
        }
        catch(Exception e)
        {
            return (ResponseEntity<User>) ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping(value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
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


    @GetMapping(value="/checkIfEmailAlreadyExist/{email}")
    public ResponseEntity<String> checkIfCanEdit(@PathVariable String email) {
        User user = userService.findUserByUsername(email);
        if (user != null)
            return new ResponseEntity<String>("This email is already taken.", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Email is unique.", HttpStatus.OK);
    }
}

