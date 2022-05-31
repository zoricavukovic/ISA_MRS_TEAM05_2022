package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.repository.users.ClientRepository;
import com.example.BookingAppTeam05.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public String penalizeClientFromReportAndReturnErrorMessage(Report r) {
        Client client = clientRepository.findById(r.getReservation().getClient().getId()).orElse(null);
        if (client == null)
            return "Cant find client with id: " + r.getReservation().getClient().getId();
        int currPenalties = client.getPenalties();
        client.setPenalties(currPenalties + 1);
        clientRepository.save(client);
        return null;
    }

    public List<Long> findSubscribedEntitiesByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        List<Long> idEntities = new ArrayList<>();
        for (BookingEntity entity : client.getWatchedEntities()) {
            idEntities.add(entity.getId());
        }
        return idEntities;
    }


    public Client findById(Long clientId) {
        return clientRepository.findById(clientId).orElse(null);
    }

    public void save(Client client) {
        clientRepository.save(client);
    }
}
