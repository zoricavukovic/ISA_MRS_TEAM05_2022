package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class InstructorController {
    @Autowired
    private InstructorService instructorService;
}
