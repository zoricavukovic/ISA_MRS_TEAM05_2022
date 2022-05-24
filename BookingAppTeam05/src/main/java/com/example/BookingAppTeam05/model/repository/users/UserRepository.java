package com.example.BookingAppTeam05.model.repository.users;

import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u join fetch u.place p join fetch u.role r where u.id=?1")
    public User findUserById(Long id);

    public User findByEmail(String email);

    @Query(value = "update User u SET u.deleted = true where u.id = ?1")
    @Modifying
    void logicalDeleteUserById(Long id);
}

