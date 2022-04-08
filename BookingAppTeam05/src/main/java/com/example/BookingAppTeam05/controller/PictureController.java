package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PictureController {
    @Autowired
    private PictureService pictureService;
}
