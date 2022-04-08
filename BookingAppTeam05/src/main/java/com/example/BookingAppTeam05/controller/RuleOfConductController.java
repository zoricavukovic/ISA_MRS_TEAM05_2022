package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.repository.RuleOfConductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RuleOfConductController {
    @Autowired
    private RuleOfConductRepository ruleOfConductRepository;
}
