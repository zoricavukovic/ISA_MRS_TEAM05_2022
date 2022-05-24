package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.model.Report;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.repository.users.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
