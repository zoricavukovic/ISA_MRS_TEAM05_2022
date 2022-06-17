package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {

    private SubscriberRepository subscriberRepository;

    public SubscriberService(){}

    @Autowired
    public SubscriberService(SubscriberRepository subscriberRepository){
        this.subscriberRepository = subscriberRepository;
    }

    List<Long> findAllSubscribersForEntityId(Long entityId){
        return subscriberRepository.findAllSubscribersForEntityId(entityId);
    }
}
