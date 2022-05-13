package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.repository.users.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
}
