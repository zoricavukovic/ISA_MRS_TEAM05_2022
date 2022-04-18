package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Instructor;
import com.example.BookingAppTeam05.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstructorService {

    private InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public Instructor findById(Long id) {
        Optional<Instructor> instructorResult =  instructorRepository.findById(id);
        return instructorResult.orElse(null);
    }
}
