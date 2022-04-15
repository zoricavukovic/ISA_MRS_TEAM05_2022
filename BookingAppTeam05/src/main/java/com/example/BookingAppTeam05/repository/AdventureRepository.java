package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Adventure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdventureRepository extends JpaRepository<Adventure, Long> {

    @Query("select a from Adventure a join fetch a.place p join fetch a.fishingEquipment f join fetch a.rulesOfConduct r join fetch a.pictures s where a.id=?1")
    public Adventure getAdventureById(Long id);
}
