package com.example.BookingAppTeam05.repository.entities;

import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface CottageRepository extends JpaRepository<Cottage, Long> {
    @Query(value="select c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.rulesOfConduct d left join fetch c.pictures s left join fetch c.cottageOwner where c.id=?1 and c.deleted = false")
    Cottage getCottageById(Long id);

    @Query(value="select c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.rulesOfConduct d left join fetch c.pictures s left join fetch c.cottageOwner where c.id=?1")
    Cottage getCottageByIdCanBeDeleted(Long id);

    @Query(value="select distinct c from Cottage c left join fetch c.place p left join fetch c.pictures s left join fetch c.cottageOwner WHERE c.cottageOwner.id=?1 and c.deleted = false")
    List<Cottage> getCottagesByOwnerId(Long id);

    @Query(value="select distinct c from Cottage c left join fetch c.cottageOwner g left join fetch c.rulesOfConduct r left join fetch c.place p " +
            "left join fetch c.rooms room left join fetch c.pictures s left join fetch c.reservations res " +
            "left join fetch c.unavailableDates unaD where c.deleted = false")
    List<Cottage> findAll();


    @Query(value="select c from Cottage c left join c.cottageOwner owner where c.id=?1 and c.deleted = false ")
    Cottage findCottageByCottageIdWithOwner(Long cottageId);


    @Query(value="select distinct c.cottageOwner from Cottage c where c.id=?1 and c.deleted=false")
    User getCottageOwnerOfCottageId(Long id);

    @Override
    //@Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value="select c from Cottage c left join fetch c.place p left join fetch c.pictures pic " +
            "left join fetch c.rulesOfConduct roc left join fetch c.reservations res left join fetch c.unavailableDates unD " +
            "left join fetch c.pricelists priceList left join fetch priceList.additionalServices " +
            "left join fetch c.subscribedClients subClients " +
            "left join fetch c.rooms room left join fetch c.cottageOwner owner " +
            "where c.id=?1 and c.deleted = false ")
    Optional<Cottage> findById(Long id);

}
