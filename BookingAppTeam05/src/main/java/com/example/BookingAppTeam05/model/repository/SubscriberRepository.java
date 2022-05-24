package com.example.BookingAppTeam05.model.repository;

import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriberRepository extends JpaRepository<User, Long> {

    @Query(value = "select s.client_id from subscribers s where s.booking_entity_id =?1", nativeQuery = true)
    List<Long> findAllSubscribersForEntityId(Long entityId);
}
