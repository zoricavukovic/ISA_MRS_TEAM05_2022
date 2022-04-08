package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;

@Controller
public class RoomController {
    @Autowired
    private RoomService roomService;
}
