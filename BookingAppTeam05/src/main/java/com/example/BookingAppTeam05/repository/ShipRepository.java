package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    @Query("select distinct s from Ship s left join fetch s.place p left join fetch s.rulesOfConduct r left join fetch s.shipOwner owner where s.deleted = False")
    List<Ship> findAll();

    @Query("select distinct s from Ship s left join fetch s.place p left join fetch s.pictures t where s.shipOwner.id=?1 and s.deleted = false")
    List<Ship> getShipsByOwnerId(Long id);

    @Query(value="select s from Ship s left join fetch s.place p left join fetch s.rulesOfConduct d left join fetch s.pictures pic where s.id=?1 and s.deleted = false")
    Ship getShipById(Long id);

    @Query(value="select s from Ship s left join s.shipOwner owner where s.id=?1 and s.deleted = false ")
    Ship findShipByShipIdWithOwner(Long shipId);

}
