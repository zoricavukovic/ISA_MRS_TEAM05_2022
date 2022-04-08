package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PictureService {
    @Autowired
    private PictureRepository pictureRepository;
}
