package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.CottageOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CottageOwnerRepository extends JpaRepository<CottageOwner, Long> {


}
