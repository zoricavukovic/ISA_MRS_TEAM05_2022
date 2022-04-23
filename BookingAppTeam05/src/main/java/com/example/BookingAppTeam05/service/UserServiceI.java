package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.users.User;

public interface UserServiceI {
    public User getUserById(Long id);
    public User save(User user);
    public User findByUsername(String username);
}
