package com.example.BookingAppTeam05.model.repository;

import com.example.BookingAppTeam05.model.users.CottageOwner;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CottageOwnerRepository extends JpaRepository<CottageOwner, Long> {

    @Query("select u from CottageOwner u join fetch u.place p join fetch u.role r join fetch u.cottages " +
            "where u.id=?1")
    public User getUserById(Long id);
}
