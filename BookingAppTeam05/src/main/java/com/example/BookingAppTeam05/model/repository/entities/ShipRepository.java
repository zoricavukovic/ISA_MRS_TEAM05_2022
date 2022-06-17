package com.example.BookingAppTeam05.model.repository.entities;

import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    @Query("select distinct s from Ship s left join fetch s.place p left join fetch s.rulesOfConduct r " +
            "left join fetch s.shipOwner owner left join fetch s.reservations res left join fetch s.unavailableDates unaD " +
            "where s.deleted = False")
    List<Ship> findAll();

    @Query("select distinct s from Ship s left join fetch s.place p left join fetch s.pictures t where s.shipOwner.id=?1 and s.deleted = false")
    List<Ship> getShipsByOwnerId(Long id);

    @Query(value="select distinct c.shipOwner from Ship c where c.id=?1 and c.deleted=false")
    User getShipOwnerOfShipId(Long id);

    @Query(value="select s from Ship s left join fetch s.place p left join fetch s.rulesOfConduct d left join fetch s.pictures pic left join fetch s.fishingEquipment fe left join fetch s.navigationalEquipment ne left join fetch s.shipOwner where s.id=?1 and s.deleted = false")
    Ship getShipById(Long id);

    @Query(value="select s from Ship s left join fetch s.place p left join fetch s.rulesOfConduct d left join fetch s.pictures pic left join fetch s.fishingEquipment fe left join fetch s.navigationalEquipment ne left join fetch s.shipOwner where s.id=?1")
    Ship getShipByIdCanBeDeleted(Long id);

    @Override
    @Query("select s from Ship s left join fetch s.place p left join fetch s.pictures c " +
            "left join fetch s.rulesOfConduct left join fetch s.reservations res left join fetch s.unavailableDates unD " +
            "left join fetch s.pricelists priceList left join fetch priceList.additionalServices left join fetch s.subscribedClients subClients " +
            "left join fetch s.fishingEquipment feq left join fetch s.navigationalEquipment naveq " +
            "left join fetch s.shipOwner owner "+
            " where s.id=?1 and s.deleted = false")
    Optional<Ship> findById(Long id);

}
