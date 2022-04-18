package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CottageRepository extends JpaRepository<Cottage, Long> {
    @Query(value="select c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.rulesOfConduct d where c.id=?1 and c.deleted = false")
    Cottage getCottageById(Long id);

    @Query(value="select c from Cottage c left join fetch c.place p left join fetch c.rooms room where c.id=?1 and c.deleted=false")
    Cottage getCottageByIdWithRooms(Long id);

    @Query(value="select distinct c from Cottage c left join fetch c.place p WHERE c.cottageOwner.id=?1 and c.deleted = false")
    List<Cottage> getCottagesByOwnerId(Long id);
}
