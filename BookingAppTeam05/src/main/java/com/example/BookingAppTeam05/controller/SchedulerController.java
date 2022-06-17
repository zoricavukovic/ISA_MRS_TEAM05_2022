package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.service.users.ClientService;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class SchedulerController {

    private ClientService clientService;

    @Autowired
    public SchedulerController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Scheduled(cron = "${scheduler.cron}")
    public void resetAllUsersPointsEveryMonth() {
        clientService.resetPenaltyPointsForAllClients();
    }


}
