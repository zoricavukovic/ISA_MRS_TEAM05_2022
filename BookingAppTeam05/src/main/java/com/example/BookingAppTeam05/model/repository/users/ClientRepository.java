package com.example.BookingAppTeam05.model.repository.users;

import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.users.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Override
    @Query("select u from Client u left join fetch u.place p left join fetch u.role m join fetch u.reservations r left join fetch u.watchedEntities w where u.id=?1")
    Optional<Client> findById(Long aLong);

    @Query("select u from Client u join fetch u.place p join fetch u.role r where u.id=?1")
    Client findByIdWithoutReservationsAndWatchedEntities(Long id);
}
