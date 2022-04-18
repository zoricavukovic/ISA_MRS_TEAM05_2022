package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.RuleOfConduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RuleOfConductRepository extends JpaRepository<RuleOfConduct, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE RuleOfConduct r SET r.allowed = ?2 WHERE r.id = ?1")
    public void updateAllowedById(Long id, boolean a);
}
