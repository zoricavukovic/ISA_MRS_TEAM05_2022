package com.example.BookingAppTeam05.model.repository;

import com.example.BookingAppTeam05.model.DeleteAccountRequest;
import com.example.BookingAppTeam05.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeleteAccountRepository extends JpaRepository<DeleteAccountRequest, Long> {

    @Query(value="select distinct r from DeleteAccountRequest r left join fetch r.user s where r.processed=false")
    List<DeleteAccountRequest> getAllUnprocessedDeleteAccountRequests();


    @Query(value="select r from DeleteAccountRequest r left join fetch r.user s where r.processed=false and s.id=?1")
    DeleteAccountRequest findByUserId(Long id);
}
