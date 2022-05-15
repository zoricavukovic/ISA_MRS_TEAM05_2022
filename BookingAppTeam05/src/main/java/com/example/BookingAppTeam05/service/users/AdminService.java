package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.users.Admin;
import com.example.BookingAppTeam05.model.users.NewAdminDTO;
import com.example.BookingAppTeam05.model.users.Role;
import com.example.BookingAppTeam05.repository.users.AdminRepository;
import com.example.BookingAppTeam05.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AdminService {
    private AdminRepository adminRepository;
    private PlaceService placeService;
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminService(AdminRepository adminRepository, PlaceService placeService, UserService userService, RoleService roleService) {
        this.adminRepository = adminRepository;
        this.placeService = placeService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Transactional
    public String createAdminAndReturnErrorMessage(NewAdminDTO newAdminDTO) {
        Place place = placeService.getPlaceById(newAdminDTO.getPlaceId());
        if (place == null)
            return "Can't find place with id: " + newAdminDTO.getPlaceId();
        if (userService.findUserByUsername(newAdminDTO.getAddress()) != null)
            return "User with email adress: " + newAdminDTO.getAddress() + " already exist.";

        String password = userService.getHashedNewUserPassword(newAdminDTO.getPassword());
        Role role = roleService.findByName("ROLE_ADMIN");

        Admin admin = new Admin(newAdminDTO.getEmail(), newAdminDTO.getName(), newAdminDTO.getSurname(), newAdminDTO.getAddress(), newAdminDTO.getPhoneNumber(), password, false, place, role);
        //try {
            adminRepository.save(admin);
            return null;
//        } catch (Exception e) {
//            return "Error happend on server. Cant save admin: " + newAdminDTO.getEmail() + " " + newAdminDTO.getName() + " " + newAdminDTO.getSurname();
//        }

    }
}
