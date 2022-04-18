package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select p from Place p where p.zipCode=?1")
    public Place getPlaceByZipCode(String zipCode);


}
