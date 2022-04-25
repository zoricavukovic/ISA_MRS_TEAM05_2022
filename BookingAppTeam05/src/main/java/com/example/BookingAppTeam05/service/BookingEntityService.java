package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.BookingEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class BookingEntityService {
    private BookingEntityRepository bookingEntityRepository;
    @Autowired
    public BookingEntityService(BookingEntityRepository bookingEntityRepository) {
        this.bookingEntityRepository = bookingEntityRepository;
    }
}
