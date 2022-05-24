package com.example.BookingAppTeam05.model.repository.entities;

import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdventureRepository extends JpaRepository<Adventure, Long> {

    @Query("select a from Adventure a left join fetch a.place p left join fetch a.fishingEquipment f " +
            "left join fetch a.rulesOfConduct r left join fetch a.pictures s left join fetch a.reservations res " +
            "where a.id=?1 and a.deleted = false")
    Adventure getAdventureById(Long id);

    @Query("select a from Adventure a left join fetch a.place p left join fetch a.fishingEquipment f " +
            "left join fetch a.rulesOfConduct r left join fetch a.pictures s left join fetch a.reservations res " +
            "where a.id=?1")
    Adventure getAdventureByIdCanBeDeleted(Long id);

    @Query("select distinct a from Adventure a left join fetch a.place p left join fetch a.pictures s where a.instructor.id=?1 and a.deleted = false")
    List<Adventure> getAdventuresForOwnerId(Long id);

    @Query(value="select distinct c from Adventure c left join fetch c.rulesOfConduct r left join fetch c.place p " +
            "left join fetch c.pictures s left join fetch c.reservations res left join fetch c.unavailableDates una " +
            "where c.deleted = false")
    List<Adventure> findAll();

    @Query(value="select distinct a.instructor from Adventure a where a.id=?1 and a.deleted=false")
    User getInstructorOfAdventureId(Long id);

    @Override
    @Query("select a from Adventure a left join fetch a.place p left join fetch a.pictures c " +
            "left join fetch a.rulesOfConduct left join fetch a.reservations res left join fetch a.unavailableDates unD " +
            "left join fetch a.pricelists priceList left join fetch priceList.additionalServices left join fetch a.subscribedClients subClients left join fetch a.fishingEquipment f " +
            "left join fetch a.instructor inst " +
            "where a.id=?1 and a.deleted = false")
    Optional<Adventure> findById(Long id);
}
