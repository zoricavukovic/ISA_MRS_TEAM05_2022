package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.dto.CreatedReportReviewDTO;
import com.example.BookingAppTeam05.dto.ReportDTO;
import com.example.BookingAppTeam05.model.Report;
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
    @GetMapping(value="/{reservationId}")
    public ResponseEntity<ReportDTO> getReportByReservationId(@PathVariable Long reservationId) {
        ReportDTO reportDTO = reportService.getReportByReservationId(reservationId);
        if (reportDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(reportDTO);
    }

    @GetMapping(value="/reported/{reservationId}")
    public ResponseEntity<Boolean> isReportedResByReservationId(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reportService.isReportedResByReservationId(reservationId));
    }

    @PostMapping( consumes = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_COTTAGE_OWNER', 'ROLE_SHIP_OWNER','ROLE_INSTRUCTOR')")
    public ResponseEntity<String> createReportForFinishedReservation(@Valid @RequestBody ReportDTO reportDTO) {
        Report createdReport = reportService.addReportAndNotifyClientIfHeDidNotCome(reportDTO);
        if (createdReport == null)
            return new ResponseEntity<>("Cannot create report.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(createdReport.getId().toString(), HttpStatus.CREATED);
    }

    @GetMapping(value="/all/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreatedReportReviewDTO>> getListOfCreatedReports(@PathVariable String type) {
        List<CreatedReportReviewDTO> retVal = null;
        if (type.equals("processed"))
            retVal = reportService.getAllProcessedReportReviewDTOs();
        else if (type.equals("unprocessed"))
            retVal = reportService.getAllUnprocessedReportReviewDTOs();

        if (retVal == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PutMapping(value="/giveResponse", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> giveResponse(@RequestBody CreatedReportReviewDTO c) {
        String errorCode = reportService.giveResponse(c);
        if (errorCode != null)
            return new ResponseEntity<>(errorCode, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("sve ok", HttpStatus.OK);
    }

}
