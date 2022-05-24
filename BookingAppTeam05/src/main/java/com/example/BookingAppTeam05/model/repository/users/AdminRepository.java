package com.example.BookingAppTeam05.model.repository.users;

import com.example.BookingAppTeam05.model.users.Admin;
import com.example.BookingAppTeam05.model.users.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Override
    @Query("select u from Admin u join fetch u.place p join fetch u.role r where u.id=?1")
    Optional<Admin> findById(Long aLong);
}
