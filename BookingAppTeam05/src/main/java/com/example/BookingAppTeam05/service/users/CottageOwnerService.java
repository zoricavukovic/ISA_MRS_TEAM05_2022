package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.model.repository.users.CottageOwnerRepository;
import com.example.BookingAppTeam05.model.users.CottageOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CottageOwnerService {
    private CottageOwnerRepository cottageOwnerRepository;
    @Autowired
    private CottageOwnerService(CottageOwnerRepository cottageOwnerRepository){
        this.cottageOwnerRepository = cottageOwnerRepository;
    }

    public CottageOwnerService(){}

    public CottageOwner getCottageOwnerWithCottagesById(Long id) {
        return this.cottageOwnerRepository.getCottageOwnerWithCottagesById(id);
    }
}
