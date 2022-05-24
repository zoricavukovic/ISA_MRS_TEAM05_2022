package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.DeleteAccountRequestDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.DeleteAccountRequest;
import com.example.BookingAppTeam05.model.repository.DeleteAccountRepository;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteAccountService {

    private DeleteAccountRepository deleteAccountRepository;
    private UserService userService;
    private EmailService emailService;

    @Autowired
    public DeleteAccountService(DeleteAccountRepository deleteAccountRepository, UserService userService, EmailService emailService) {
        this.deleteAccountRepository = deleteAccountRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    public List<DeleteAccountRequestDTO> getAllUnprocessedDeleteAccountRequestDTOs() {
        List<DeleteAccountRequestDTO> retVal = new ArrayList<>();
        List<DeleteAccountRequest> requests =  deleteAccountRepository.getAllUnprocessedDeleteAccountRequests();
        for (DeleteAccountRequest r: requests) {
            UserDTO userDTO = new UserDTO(userService.findUserById(r.getUser().getId()));
            DeleteAccountRequestDTO dt = new DeleteAccountRequestDTO(r, userDTO);
            retVal.add(dt);
        }
        return retVal;
    }

    @Transactional
    public String giveResponse(DeleteAccountRequestDTO d) {
        DeleteAccountRequest deleteAccountRequest = deleteAccountRepository.findById(d.getId()).orElse(null);
        if (deleteAccountRequest == null)
            return "Can't find delete account request with id: " + d.getId();

        deleteAccountRequest.setProcessed(true);
        deleteAccountRequest.setAccepted(d.isAccepted());
        deleteAccountRepository.delete(deleteAccountRequest);

        if (d.isAccepted()) {
            userService.logicalDeleteUserById(d.getUser().getId());
        }

        try {
            emailService.sendEmailAsAdminResponseFromDeleteAccountRequest(d);
        } catch (Exception e) {
            return "Error happened while sending email about deleting account to user";
        }
        return null;
    }
}
