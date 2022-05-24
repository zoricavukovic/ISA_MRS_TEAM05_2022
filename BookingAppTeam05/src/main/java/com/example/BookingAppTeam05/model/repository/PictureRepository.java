package com.example.BookingAppTeam05.model.repository;

import com.example.BookingAppTeam05.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    @Query(value = "SELECT p.picture_path FROM pictures p WHERE p.entity_id = ?1", nativeQuery = true)
    List<String> findAllPictureNamesForEntityId(Long id);

    void deleteByPicturePath(String path);
}
