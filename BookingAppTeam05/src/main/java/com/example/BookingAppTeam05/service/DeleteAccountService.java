package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.DeleteAccountRequestDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.DeleteAccountRequest;
import com.example.BookingAppTeam05.model.repository.DeleteAccountRepository;
import com.example.BookingAppTeam05.service.users.UserService;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
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

    public DeleteAccountRequest save(DeleteAccountRequest deleteAccountRequest) {
        return this.deleteAccountRepository.save(deleteAccountRequest);
    }

    public DeleteAccountRequest findById(Long id) {
        return this.deleteAccountRepository.findById(id).orElse(null);
    }

    @Transactional
    public String giveResponse(DeleteAccountRequestDTO d) {
        try {
            DeleteAccountRequest deleteAccountRequest = deleteAccountRepository.findById(d.getId()).orElse(null);
            if (deleteAccountRequest == null)
                return "Can't find delete account request with id: " + d.getId();

            if (deleteAccountRequest.isProcessed())
                return "This request is already processed";

            deleteAccountRequest.setProcessed(true);
            deleteAccountRepository.save(deleteAccountRequest);
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
        catch (ObjectOptimisticLockingFailureException e) {
            return "Conflict seems to have occurred, another admin has reviewed this request before you. Please refresh page and try again";
        }
    }
}
