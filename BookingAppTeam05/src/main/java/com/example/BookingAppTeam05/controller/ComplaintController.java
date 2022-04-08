package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ComplaintController {
    @Autowired
    private ComplaintService complaintService;
}
