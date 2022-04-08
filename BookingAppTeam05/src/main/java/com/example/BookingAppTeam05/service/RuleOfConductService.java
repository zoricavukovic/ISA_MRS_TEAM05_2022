package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.RuleOfConductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleOfConductService {
    @Autowired
    private RuleOfConductRepository ruleOfConductRepository;
}
