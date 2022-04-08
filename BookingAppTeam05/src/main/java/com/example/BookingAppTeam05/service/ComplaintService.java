package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {
    @Autowired
    private ComplaintRepository complaintRepository;
}
