package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CottageRepository extends JpaRepository<Cottage, Long> {
    @Query(value="select c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.rulesOfConduct d left join fetch c.pictures s where c.id=?1 and c.deleted = false")
    Cottage getCottageById(Long id);

    @Query(value="select distinct c from Cottage c left join fetch c.place p left join fetch c.pictures s WHERE c.cottageOwner.id=?1 and c.deleted = false")
    List<Cottage> getCottagesByOwnerId(Long id);

    @Query(value="select distinct c from Cottage c left join fetch c.rulesOfConduct r left join fetch c.place p left join fetch c.rooms room left join fetch c.pictures s where c.deleted = false")
    List<Cottage> findAll();

    @Query(value = "update cottages as c set deleted = true where c.id = ?1 and c.deleted = false", nativeQuery = true)
    @Modifying
    void logicalDeleteById(Long id);

    @Query(value="select c from Cottage c left join c.cottageOwner owner where c.id=?1 and c.deleted = false ")
    Cottage findCottageByCottageIdWithOwner(Long cottageId);


    @Query(value="select distinct c.cottageOwner from Cottage c where c.id=?1 and c.deleted=false")
    User getCottageOwnerOfCottageId(Long id);
}
