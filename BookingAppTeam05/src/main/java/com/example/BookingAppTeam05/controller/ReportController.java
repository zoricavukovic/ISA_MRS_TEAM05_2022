package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    public ReportController(){}

    @GetMapping(value="/{reservationId}")
    public ResponseEntity<ReportDTO> getReportByReservationId(@PathVariable Long reservationId) {
        ReportDTO reportDTO = reportService.getReportByReservationId(reservationId);
        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }

    @GetMapping(value="/reported/{reservationId}")
    public ResponseEntity<Boolean> isReportedResByReservationId(@PathVariable Long reservationId) {
        return new ResponseEntity<>(reportService.isReportedResByReservationId(reservationId), HttpStatus.OK);
    }

    @PostMapping( consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createReportForFinishedReservation(@Valid @RequestBody ReportDTO reportDTO) {
        reportService.addReportAndNotifyClientIfHeDidNotCome(reportDTO);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @GetMapping(value="/all/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<CreatedReportReviewDTO>> getListOfCreatedReports(@PathVariable String type) {
        List<CreatedReportReviewDTO> retVal = reportService.getListOfCreatedReports(type);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value="/giveResponse", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> giveResponse(@RequestBody CreatedReportReviewDTO c) {
        reportService.giveResponse(c);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
