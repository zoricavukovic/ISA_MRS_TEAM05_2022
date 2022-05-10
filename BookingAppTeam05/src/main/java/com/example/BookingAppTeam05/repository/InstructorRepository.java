package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.users.Instructor;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    @Query("select u from Instructor u join fetch u.place p join fetch u.role r join u.adventures where u.id=?1")
    public User getUserById(Long id);
}
