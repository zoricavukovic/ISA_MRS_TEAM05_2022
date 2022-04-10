package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.*;
import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        User u = userService.getUserById(id);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        UserDTO userDTO = null;
        switch(u.getUserType()){
            case CLIENT: {
                Client client = (Client) u;
                userDTO = new ClientDTO(client);
                break;
            }
            case ADMIN: {
                Admin admin = (Admin) u;
                userDTO = new AdminDTO(admin);
                break;
            }
            case COTTAGE_OWNER: {
                CottageOwner cottageOwner = (CottageOwner) u;
                userDTO = new CottageOwnerDTO(cottageOwner);
                break;
            }
            case SHIP_OWNER: {
                ShipOwner shipOwner = (ShipOwner) u;
                userDTO = new ShipOwnerDTO(shipOwner);
                break;
            }
            case INSTRUCTOR: {
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

}

