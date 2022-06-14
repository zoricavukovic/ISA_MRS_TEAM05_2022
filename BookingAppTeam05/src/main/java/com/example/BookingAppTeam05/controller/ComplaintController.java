package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.ComplaintDTO;
import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.model.Complaint;
import com.example.BookingAppTeam05.dto.ComplaintReviewDTO;
import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/complaints")
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

    @GetMapping(value="/all/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ComplaintReviewDTO>> getListOfCreatedReports(@PathVariable String type) {
        List<ComplaintReviewDTO> retVal = null;
        if (type.equals("processed"))
            retVal = complaintService.getAllProcessedComplaintReviewDTOs();
        else if (type.equals("unprocessed"))
            retVal = complaintService.getAllUnprocessedComplaintReviewDTOs();

        if (retVal == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value="/giveResponse", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> giveResponse(@RequestBody ComplaintReviewDTO c) {
        complaintService.giveResponse(c);
        return new ResponseEntity<>("sve ok", HttpStatus.OK);
    }

}
