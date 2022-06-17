package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.dto.AllRequestsNumsDTO;
import com.example.BookingAppTeam05.exception.BookingAppException;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.users.Admin;
import com.example.BookingAppTeam05.dto.users.NewAdminDTO;
import com.example.BookingAppTeam05.model.users.Role;
import com.example.BookingAppTeam05.model.repository.users.AdminRepository;
import com.example.BookingAppTeam05.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AdminService {
    private AdminRepository adminRepository;
    private PlaceService placeService;
    private UserService userService;
    private RoleService roleService;
    private ComplaintService complaintService;
    private ReportService reportService;
    private DeleteAccountService deleteAccountService;
    private RatingService ratingService;

    @Autowired
    public AdminService(AdminRepository adminRepository, PlaceService placeService, UserService userService, RoleService roleService, ComplaintService complaintService, ReportService reportService, DeleteAccountService deleteAccountService, RatingService ratingService) {
        this.adminRepository = adminRepository;
        this.placeService = placeService;
        this.userService = userService;
        this.roleService = roleService;
        this.complaintService = complaintService;
        this.reportService = reportService;
        this.deleteAccountService = deleteAccountService;
        this.ratingService = ratingService;
    }

    public AdminService(){}

    @Transactional
    public void createAdmin(NewAdminDTO newAdminDTO) {
        Place place = placeService.getPlaceById(newAdminDTO.getPlaceId());
        if (place == null)
            throw new ItemNotFoundException("Can't find place");
        if (userService.findUserByEmail(newAdminDTO.getEmail()) != null)
            throw new ItemNotFoundException("User with email address: " + newAdminDTO.getAddress() + " already exist.");
        String password = userService.getHashedNewUserPassword(newAdminDTO.getPassword());
        Role role = roleService.findByName("ROLE_ADMIN");

        Admin admin = new Admin(newAdminDTO.getEmail(), newAdminDTO.getName(),
                newAdminDTO.getSurname(), newAdminDTO.getAddress(), newAdminDTO.getPhoneNumber(),
                password, false, place, role);
        try {
            adminRepository.save(admin);
        } catch (Exception e) {
            throw new BookingAppException("Error happened on server. Can't save admin: " + newAdminDTO.getEmail() + ", " + newAdminDTO.getName() + " " + newAdminDTO.getSurname());
        }
    }

    public AllRequestsNumsDTO getAllRequestsNumsDTO() {
        int ratingSize = ratingService.getAllUnprocessedRatingReviewDTOs().size();
        int reportSize = reportService.getAllUnprocessedReportReviewDTOs().size();
        int complaintSize = complaintService.getAllUnprocessedComplaintReviewDTOs().size();
        int deleteAccountSize = deleteAccountService.getAllUnprocessedDeleteAccountRequestDTOs().size();
        int newRequestSize = userService.getAllNewAccountRequestDTOs().size();
        return new AllRequestsNumsDTO(reportSize, ratingSize, complaintSize, deleteAccountSize, newRequestSize);
    }
}
