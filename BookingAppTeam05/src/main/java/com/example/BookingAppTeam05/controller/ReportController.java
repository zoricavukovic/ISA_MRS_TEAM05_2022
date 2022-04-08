package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReportController {
    @Autowired
    private ReportService reportService;
}
