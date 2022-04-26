package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.users.*;
import com.example.BookingAppTeam05.service.PlaceService;
import com.example.BookingAppTeam05.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin
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
        User u = userService.getUserById(id);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UserDTO userDTO = null;
        switch(u.getRole().getName()){
            case "ROLE_CLIENT": {
                Client client = (Client) u;
                userDTO = new ClientDTO(client);
                break;
            }
            case "ROLE_ADMIN": {
                Admin admin = (Admin) u;
                userDTO = new AdminDTO(admin);
                break;
            }
            case "ROLE_COTTAGE_OWNER": {
                CottageOwner cottageOwner = (CottageOwner) u;
                userDTO = new CottageOwnerDTO(cottageOwner);
                break;
            }
            case "ROLE_SHIP_OWNER": {
                ShipOwner shipOwner = (ShipOwner) u;
                userDTO = new ShipOwnerDTO(shipOwner);
                break;
            }
            case "ROLE_INSTRUCTOR": {
                Instructor instructor = (Instructor) u;
                userDTO = new InstructorDTO(instructor);
                break;
            }
            default:
                break;
        }
        assert userDTO != null;
        userDTO.setPlace(u.getPlace());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN','ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @RequestBody UserDTO userDTO)  {
        User user = userService.getUserById(userId);
        user.setAddress(userDTO.getAddress());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        Place place = placeService.getPlaceByZipCode(userDTO.getPlace().getZipCode());
        user.setPlace(place);

        final User updatedUser = userService.save(user);

        return ResponseEntity.ok(updatedUser);
    }
}

