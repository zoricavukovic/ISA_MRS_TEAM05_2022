package com.example.BookingAppTeam05.controller.users;

import com.example.BookingAppTeam05.service.users.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class InstructorController {
    @Autowired
    private InstructorService instructorService;
}
