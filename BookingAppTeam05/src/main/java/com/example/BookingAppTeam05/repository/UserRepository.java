package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u left join fetch u.place where u.id=?1")
    public User getUserById(Long id);
}

