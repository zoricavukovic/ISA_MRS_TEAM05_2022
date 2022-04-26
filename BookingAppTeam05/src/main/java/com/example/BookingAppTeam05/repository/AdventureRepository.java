package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdventureRepository extends JpaRepository<Adventure, Long> {

    @Query("select a from Adventure a left join fetch a.place p left join fetch a.fishingEquipment f left join fetch a.rulesOfConduct r left join fetch a.pictures s where a.id=?1")
    Adventure getAdventureById(Long id);

    @Query("select distinct a from Adventure a left join fetch a.place p left join fetch a.pictures s where a.instructor.id=?1 and a.deleted = false")
    List<Adventure> getAdventuresForOwnerId(Long id);
}
