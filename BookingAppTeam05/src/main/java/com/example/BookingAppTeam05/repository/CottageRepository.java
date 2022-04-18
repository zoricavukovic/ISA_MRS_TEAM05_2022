package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CottageRepository extends JpaRepository<Cottage, Long> {
    @Query("select c from Cottage c join fetch c.place p join fetch c.rulesOfConduct r join fetch c.rooms room where c.id=?1")
    Cottage getCottageById(Long id);

    @Query("select c from Cottage c join fetch c.place p join fetch c.rulesOfConduct r join fetch c.rooms room join fetch c.cottageOwner owner")
    List<Cottage> findAll();
}
