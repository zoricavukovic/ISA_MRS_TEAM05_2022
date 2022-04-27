package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.entities.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CottageRepository extends JpaRepository<Cottage, Long> {
    @Query(value="select c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.rulesOfConduct d left join fetch c.pictures s where c.id=?1 and c.deleted = false")
    Cottage getCottageById(Long id);

    @Query(value="select c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.pictures s where c.id=?1 and c.deleted=false")
    Cottage getCottageByIdWithRooms(Long id);

    @Query(value="select distinct c from Cottage c left join fetch c.place p left join fetch c.pictures s WHERE c.cottageOwner.id=?1 and c.deleted = false")
    List<Cottage> getCottagesByOwnerId(Long id);

    @Query(value="select c from Cottage c left join fetch c.rooms r left join fetch c.pictures s where c.id=?2 and r.roomNum=?1")
    Cottage getCottageRoomByNum(int roomNum, Long id);

    @Query(value="select distinct c from Cottage c left join fetch c.rulesOfConduct r left join fetch c.place p left join fetch c.rooms room left join fetch c.pictures s where c.deleted = false")
    List<Cottage> findAll();

    @Query(value="select distinct c from Cottage c left join fetch c.place p left join fetch c.pictures s left join fetch c.rooms room left join fetch c.rulesOfConduct d " +
            "where c.cottageOwner.id=?1 and c.name=?2 and p.cityName =?3 and c.entityCancelationRate =?4 and c.deleted = false")
    List<Cottage> searchCottagesOfOwnerAndAnd(Long ownerId, String cottageName, String city, float rate);

    @Query(value="select distinct c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.rulesOfConduct d " +
            "where c.cottageOwner.id=?1 and (c.name=?2 and p.cityName =?3) or c.entityCancelationRate =?4 and c.deleted = false")
    List<Cottage> searchCottagesOfOwnerAndOr(Long ownerId, String cottageName, String city, float rate);

    @Query(value="select distinct c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.rulesOfConduct d " +
            "where c.cottageOwner.id=?1 and (c.name=?2 or p.cityName =?3) and c.entityCancelationRate =?4 and c.deleted = false")
    List<Cottage> searchCottagesOfOwnerOrAnd(Long ownerId, String cottageName, String city, float rate);

    @Query(value="select distinct c from Cottage c left join fetch c.place p left join fetch c.rooms room left join fetch c.rulesOfConduct d " +
            "where (c.name=?2 or p.cityName =?3 or c.entityCancelationRate =?4) and c.cottageOwner.id=?1 and c.deleted = false")
    List<Cottage> searchCottagesOfOwnerOrOr(Long ownerId, String cottageName, String city, float rate);

    @Query(value = "update cottages as c set deleted = true where c.id = ?1 and c.deleted = false", nativeQuery = true)
    @Modifying
    void logicalDeleteById(Long id);

    @Query(value="select c from Cottage c left join c.cottageOwner owner where c.id=?1 and c.deleted = false ")
    Cottage findCottageByCottageIdWithOwner(Long cottageId);
}
