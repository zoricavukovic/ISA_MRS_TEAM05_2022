package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.AdditionalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AdditionalServiceController {
    @Autowired
    private AdditionalServiceService additionalServiceService;
}