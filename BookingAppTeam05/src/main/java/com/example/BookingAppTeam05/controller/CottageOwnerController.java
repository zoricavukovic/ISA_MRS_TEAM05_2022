package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.CottageOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CottageOwnerController {
    @Autowired
    private CottageOwnerService cottageOwnerService;
}
