package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.users.NewAccountRequestDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.dto.users.UserRequestDTO;
import com.example.BookingAppTeam05.model.LoyaltyProgramEnum;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.Ship;
import com.example.BookingAppTeam05.model.repository.users.UserRepository;
import com.example.BookingAppTeam05.model.users.*;
import com.example.BookingAppTeam05.service.EmailService;
import com.example.BookingAppTeam05.service.LoyaltyProgramService;
import com.example.BookingAppTeam05.service.PlaceService;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.entities.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private PlaceService placeService;
    private ClientService clientService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private EmailService emailService;
    private RoleService roleService;
    private LoyaltyProgramService loyaltyProgramService;

    private InstructorService instructorService;
    private CottageOwnerService cottageOwnerService;
    private ShipOwnerService shipOwnerService;


    @Autowired
    public UserService(UserRepository userRepository, ClientService clientService, PlaceService placeService, EmailService emailService, RoleService roleService, LoyaltyProgramService loyaltyProgramService, InstructorService instructorService, ShipOwnerService shipOwnerService, CottageOwnerService cottageOwnerService) {

        this.userRepository = userRepository;
        this.placeService = placeService;
        this.clientService = clientService;
        this.emailService = emailService;
        this.roleService = roleService;
        this.loyaltyProgramService = loyaltyProgramService;
        this.instructorService = instructorService;
        this.shipOwnerService = shipOwnerService;
        this.cottageOwnerService = cottageOwnerService;
    }

    public User findUserById(Long id) {
        User user = userRepository.findUserById(id);
        LoyaltyProgramEnum loyaltyProgramType = loyaltyProgramService.getLoyaltyProgramTypeFromUserPoints(user.getLoyaltyPoints());
        user.setLoyaltyProgramEnum(loyaltyProgramType);
        return user;
    }

    public User updateUser(Long userId, UserDTO userDTO){
        User user = userRepository.findUserById(userId);
        user.setAddress(userDTO.getAddress());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        Place place = placeService.getPlaceById(userDTO.getPlace().getId());
        user.setPlace(place);
        final User updatedUser;
        if (userDTO.getUserType().toString().equals("ROLE_SHIP_OWNER")){
            ((ShipOwner) user).setCaptain(userDTO.isCaptain());
        }
        updatedUser = userRepository.save(user);
        return user;
    }

    public User save(User user){return userRepository.save(user);}

    public User findUserByEmail(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
    public User findUserByEmailCanBeDeletedAndNotYetActivated(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailAllUser(username);
    }

    public boolean passwordIsCorrect(User user, String checkPass) {
        return passwordEncoder.matches(checkPass, user.getPassword());
    }

    public User save(UserRequestDTO userRequest) {
        // TODO::
        return null;
    }

    public void setNewPasswordForUser(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        if (user.getRole().getName().equals("ROLE_ADMIN"))
            ((Admin)user).setPasswordChanged(true);
        userRepository.save(user);
    }

    public void logicalDeleteUserById(Long id) {
        userRepository.logicalDeleteUserById(id);
    }

    public String getHashedNewUserPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    public String createUser(UserDTO userDTO) {

        Place place = placeService.getPlaceById(userDTO.getPlace().getId());
        if (place == null)
            return "Can't find place with id: " + userDTO.getPlace().getId();
        if (findUserByEmailCanBeDeletedAndNotYetActivated("bookingapp05mzr++" + userDTO.getEmail()) != null)
            return "User with email address: " + userDTO.getEmail() + " already exist.";

        String password = getHashedNewUserPassword(userDTO.getPassword());
        Role role = roleService.findByName(userDTO.getUserTypeValue());
        User u = null;
        switch (userDTO.getUserTypeValue()) {
            case "ROLE_CLIENT":
                u = new Client("bookingapp05mzr++" + userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getAddress(),
                        userDTO.getDateOfBirth(), userDTO.getPhoneNumber(), password, true, place, role, 0);
                break;
            case "ROLE_COTTAGE_OWNER":
                u = new CottageOwner("bookingapp05mzr++" + userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getAddress(),
                        userDTO.getDateOfBirth(), userDTO.getPhoneNumber(), password, true, place, role, userDTO.getReason());

                break;
            case "ROLE_SHIP_OWNER":
                u = new ShipOwner("bookingapp05mzr++" + userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getAddress(),
                        userDTO.getDateOfBirth(), userDTO.getPhoneNumber(), password, true, place, role, userDTO.isCaptain(), userDTO.getReason());
                break;
            case "ROLE_INSTRUCTOR":
                u = new Instructor("bookingapp05mzr++" + userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getAddress(),
                        userDTO.getDateOfBirth(), userDTO.getPhoneNumber(), password, true, place, role, userDTO.getReason());
                break;
        }
        if (u== null){
            return "Error happened on server. Cant create user: " + userDTO.getEmail() + " " + userDTO.getFirstName() + " " + userDTO.getLastName();
        }
        try {
            assert false;
            userRepository.save(u);
            if (userDTO.getUserTypeValue().equals("ROLE_CLIENT")){
                emailService.sendActivationMessage(u);
            }
            return null;
        } catch (Exception e) {
            return "Error happened on server. Cant create user: " + userDTO.getEmail() + " " + userDTO.getFirstName() + " " + userDTO.getLastName();
        }
    }

    public List<NewAccountRequestDTO> getAllNewAccountRequestDTOs() {
        List<User> newAccounts = userRepository.getAllNewAccountRequests();
        List<NewAccountRequestDTO> retVal = new ArrayList<>();
        for (User u : newAccounts) {
            UserDTO userDTO = new UserDTO(u);
            userDTO.setPlace(u.getPlace());
            retVal.add(new NewAccountRequestDTO(userDTO, null, false));
        }
        return retVal;
    }

    @Transactional
    public String giveResponseForNewAccountRequest(NewAccountRequestDTO d) {
        User user = userRepository.findUserById(d.getUser().getId());
        if (user == null)
            return "Cant find user with id " + d.getUser().getId();
        if (!user.isNotYetActivated())
            return "This user account is already approved.";

        if (d.isAccepted()) {
            user.setNotYetActivated(false);
            userRepository.save(user);
        } else {
            userRepository.physicalDeleteUserById(d.getUser().getId());
        }
        try {
            emailService.sendEmailAsAdminResponseFromNewAccountRequest(d);
        } catch (Exception e) {
            return "Error happened while sending email about new account request to user";
        }
        return null;

    }

    public ResponseEntity<String> activateAccount(String email) {
        try{
            User user = userRepository.findByEmailAndNotYetActivated(email);
            user.setNotYetActivated(false);
            userRepository.save(user);
            return new ResponseEntity<>("Account is activated.", HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>("Account not activated.", HttpStatus.BAD_REQUEST);
        }
    }

    public List<BookingEntity> getBookingEntitiesByOwnerId(Long id) {
        User owner = this.findUserById(id);
        switch (owner.getRole().getName()) {
            case "ROLE_COTTAGE_OWNER": {
                CottageOwner cottageOwner = cottageOwnerService.getCottageOwnerWithCottagesById(id);
                if (cottageOwner.getCottages() == null || cottageOwner.getCottages().size() == 0)
                    return new ArrayList<>();
                return new ArrayList<>(cottageOwner.getCottages());
            }
            case "ROLE_SHIP_OWNER": {
                ShipOwner shipOwner = shipOwnerService.getShipOwnerWithShipsById(id);
                if (shipOwner.getShips() == null || shipOwner.getShips().size() == 0)
                    return new ArrayList<>();
                return new ArrayList<>(shipOwner.getShips());
            }
            case "ROLE_INSTRUCTOR": {
                Instructor instructor = instructorService.getInstructorWithAdventuresById(id);
                if (instructor.getAdventures() == null || instructor.getAdventures().size() == 0)
                    return new ArrayList<>();
                return new ArrayList<>(instructor.getAdventures());
            }
            default: {
                return null;
            }
        }
    }
}
