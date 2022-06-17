package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.FishingEquipment;
import com.example.BookingAppTeam05.model.NavigationEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NavigationEquipmentRepository extends JpaRepository<NavigationEquipment, Long> {

    @Query(value = "select * from navigation_equipments navEqu left join ships ship on navEqu.ship_id = ship.id where ship.id =?2 and navEqu.name=?1", nativeQuery = true)
    NavigationEquipment findNavigationEquipmentByNameAndShipId(String equipmentName, Long shipId);


}
