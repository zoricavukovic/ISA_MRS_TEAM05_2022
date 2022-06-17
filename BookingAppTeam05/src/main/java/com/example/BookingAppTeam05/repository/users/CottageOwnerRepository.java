package com.example.BookingAppTeam05.repository.users;

import com.example.BookingAppTeam05.model.users.CottageOwner;
import com.example.BookingAppTeam05.model.users.Instructor;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CottageOwnerRepository extends JpaRepository<CottageOwner, Long> {

    @Query("select u from CottageOwner u left join fetch u.place p left join fetch u.role r left join fetch u.cottages s where u.id=?1")
    CottageOwner getCottageOwnerWithCottagesById(Long id);
}
