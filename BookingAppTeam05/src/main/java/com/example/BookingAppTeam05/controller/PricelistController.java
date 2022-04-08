package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.PricelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PricelistController {
    @Autowired
    private PricelistService pricelistService;
}
