package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;
}
