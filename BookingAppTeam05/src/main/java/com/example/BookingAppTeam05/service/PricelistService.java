package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Cottage;
import com.example.BookingAppTeam05.model.Pricelist;
import com.example.BookingAppTeam05.repository.CottageRepository;
import com.example.BookingAppTeam05.repository.PricelistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PricelistService {
    private PricelistRepository pricelistRepository;

    @Autowired
    public PricelistService(PricelistRepository pricelistRepository) {
        this.pricelistRepository = pricelistRepository;
    }

    public List<Pricelist> getCurrentPricelistByBookingEntityId(Long id) {
        return pricelistRepository.getCurrentPricelistByBookingEntityId(id);
    }

    public Pricelist save(Pricelist pricelist) {
        return pricelistRepository.save(pricelist);

    }

}
