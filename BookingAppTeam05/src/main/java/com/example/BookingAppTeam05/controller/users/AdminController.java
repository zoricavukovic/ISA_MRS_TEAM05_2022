package com.example.BookingAppTeam05.controller.users;

import com.example.BookingAppTeam05.dto.AllRequestsNumsDTO;
import com.example.BookingAppTeam05.dto.users.NewAdminDTO;
import com.example.BookingAppTeam05.service.users.AdminService;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/admins")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> createAdmin(@Valid @RequestBody NewAdminDTO newAdminDTO) {
        String errorMessage = adminService.createAdminAndReturnErrorMessage(newAdminDTO);
        if (errorMessage != null)
            return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<String>("Successfully created new admin", HttpStatus.OK);
    }


    @GetMapping(value="/allRequestsNums")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<AllRequestsNumsDTO> getAllRequestsNums() {
        AllRequestsNumsDTO a = adminService.getAllRequestsNumsDTO();
        return new ResponseEntity<>(a, HttpStatus.OK);
    }

}
