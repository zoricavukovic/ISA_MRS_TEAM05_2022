package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.dto.ReservationDTO;
import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.Reservation;
import com.example.BookingAppTeam05.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
//    @GetMapping(value="/{reservationId}")
//    public ResponseEntity<ReportDTO> getReportByReservationId(@PathVariable Long reservationId) {
//        Report report = reportService.getReportByReservationId(reservationId);
//
//        return ResponseEntity.ok(new ReportDTO());
//    }

    @GetMapping(value="/reported/{reservationId}")
    public ResponseEntity<Boolean> isReportedResByReservationId(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reportService.isReportedResByReservationId(reservationId));
    }

    @PostMapping( consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createReportForFinishedReservation(@Valid @RequestBody ReportDTO reportDTO) {
        Report createdReport = reportService.addReport(reportDTO);
        if (createdReport == null)
            return new ResponseEntity<>("Cannot create report.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(createdReport.getId().toString(), HttpStatus.CREATED);
    }


}
