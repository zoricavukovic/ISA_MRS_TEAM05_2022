package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "UPDATE Room r SET r.deleted = true WHERE r.id = ?1")
    @Modifying
    public void deleteById(Long id);
}
