package com.example.BookingAppTeam05.repository;

import com.example.BookingAppTeam05.model.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Long> {

    @Query(value = "select * from additional_services addSer left join reservation_additional_service resAddSer on addSer.id = resAddSer.additional_service_id where resAddSer.reservation_id =?1", nativeQuery = true)
    public List<AdditionalService> findAdditionalServicesByReservationId(Long resId);
}
