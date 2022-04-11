package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.User;
import com.example.BookingAppTeam05.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public User save(User user){return userRepository.save(user);}

}
