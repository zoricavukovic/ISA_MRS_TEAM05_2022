package com.example.BookingAppTeam05.controller;

import javax.servlet.http.HttpServletResponse;
import com.example.BookingAppTeam05.dto.JwtAuthenticationRequestDTO;
import com.example.BookingAppTeam05.dto.UserDTO;
import com.example.BookingAppTeam05.dto.UserRequestDTO;
import com.example.BookingAppTeam05.dto.UserTokenStateDTO;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.service.UserService;
import com.example.BookingAppTeam05.springSecurity.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    @PostMapping("/login")
    public ResponseEntity<UserTokenStateDTO> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequestDTO authenticationRequest, HttpServletResponse response) {

        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn, new UserDTO(user)));
    }

    // Endpoint za registraciju novog korisnika
    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody UserRequestDTO userRequest) {

        User existUser = this.userService.findByUsername(userRequest.getUsername());

        if (existUser != null) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        User user = this.userService.save(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}