package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.DeleteAccountRequestDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.exception.ConflictException;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.NotificationByEmailException;
import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.exception.database.EditItemException;
import com.example.BookingAppTeam05.model.DeleteAccountRequest;
import com.example.BookingAppTeam05.model.repository.DeleteAccountRepository;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    public DeleteAccountService(){}

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
    public void giveResponse(DeleteAccountRequestDTO d) {
        try {
            DeleteAccountRequest deleteAccountRequest = deleteAccountRepository.findById(d.getId()).orElse(null);
            if (deleteAccountRequest == null)
                throw new ItemNotFoundException("Can't find delete account request with id: " + d.getId());
            if (deleteAccountRequest.isProcessed())
                throw new EditItemException("This request is already processed");
            deleteAccountRequest.setProcessed(true);
            deleteAccountRepository.save(deleteAccountRequest);
            deleteAccountRequest.setAccepted(d.isAccepted());
            deleteAccountRepository.delete(deleteAccountRequest);
            if (d.isAccepted())
                userService.logicalDeleteUserById(d.getUser().getId());
            try {
                emailService.sendEmailAsAdminResponseFromDeleteAccountRequest(d);
            } catch ( InterruptedException e) {
                throw new NotificationByEmailException("Error happened while sending email about deleting account to user.");
            }
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException("Conflict seems to have occurred, another admin has reviewed this request before you. Please refresh page and try again");
        }
    }

    @Transactional
    public void createDeleteRequest(DeleteAccountRequestDTO deleteRequest) {
        try{
            DeleteAccountRequest deleteAccountRequest = new DeleteAccountRequest();
            deleteAccountRequest.setProcessed(false);
            deleteAccountRequest.setAccepted(false);
            deleteAccountRequest.setAdminResponse(null);
            deleteAccountRequest.setReason(deleteRequest.getReason());
            User user = userService.findUserById(deleteRequest.getUser().getId());
            deleteAccountRequest.setUser(user);
            deleteAccountRequest.setVersion(0L);
            deleteAccountRepository.save(deleteAccountRequest);
        }catch (Exception e){
            throw new CreateItemException("Failed to create request");
        }
    }

    public Boolean hasUserRequest(Long id) {
        DeleteAccountRequest dar = deleteAccountRepository.findByUserId(id);
        if(dar == null)
            return false;
        return true;
    }
}
