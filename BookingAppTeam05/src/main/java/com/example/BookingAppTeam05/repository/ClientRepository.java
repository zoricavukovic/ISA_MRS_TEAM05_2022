package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.users.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Override
    @Query("select u from Client u join fetch u.place p join fetch u.role r join fetch u.reservations join fetch u.watchedEntities where u.id=?1")
    Optional<Client> findById(Long aLong);
}
