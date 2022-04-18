package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {


    @Query("select s from Ship s join fetch s.place p join fetch s.rulesOfConduct r join fetch s.shipOwner owner")
    List<Ship> findAll();
}
