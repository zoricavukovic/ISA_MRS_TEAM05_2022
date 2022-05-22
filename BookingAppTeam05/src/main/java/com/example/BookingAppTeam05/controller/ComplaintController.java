package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.ComplaintDTO;
import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.model.Complaint;
import com.example.BookingAppTeam05.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/complaint")
public class ComplaintController {

    private ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/getByReservationId/{id}")
    public ResponseEntity<ComplaintDTO> getComplaintByReservationId(@PathVariable Long id) {
        ComplaintDTO complaintDTO = complaintService.getComplaintByReservationId(id);
        if (complaintDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(complaintDTO);
    }

    @PostMapping(value="/createComplaint", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComplaintDTO> createComplaint(@RequestBody ComplaintDTO complaintDTO) {
        Complaint complaint = complaintService.createComplaint(complaintDTO);
        if (complaint == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(new ComplaintDTO(complaint));
    }

}
