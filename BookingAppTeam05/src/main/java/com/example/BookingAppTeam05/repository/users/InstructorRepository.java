package com.example.BookingAppTeam05.repository.users;

import com.example.BookingAppTeam05.model.users.Instructor;
import com.example.BookingAppTeam05.model.users.ShipOwner;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    @Query("select u from Instructor u left join fetch u.place p left join fetch u.role r left join fetch u.adventures a where u.id=?1")
    Instructor getInstructorWithAdventuresById(Long id);
}
