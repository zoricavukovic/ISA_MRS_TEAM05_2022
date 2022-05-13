package com.example.BookingAppTeam05.repository.users;

import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u join fetch u.place p join fetch u.role r where u.id=?1")
    public User findUserById(Long id);

    public User findByEmail(String email);
}

