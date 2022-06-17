package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.dto.ChangePasswordDTO;
import com.example.BookingAppTeam05.dto.LoyaltyProgramDTO;
import com.example.BookingAppTeam05.dto.users.NewAccountRequestDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.dto.users.UserRequestDTO;
import com.example.BookingAppTeam05.exception.*;
import com.example.BookingAppTeam05.exception.database.CreateItemException;
import com.example.BookingAppTeam05.exception.database.DatabaseException;
import com.example.BookingAppTeam05.exception.database.DeleteItemException;
import com.example.BookingAppTeam05.exception.database.EditItemException;
import com.example.BookingAppTeam05.model.LoyaltyProgramEnum;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.entities.BookingEntity;
import com.example.BookingAppTeam05.model.repository.users.UserRepository;
import com.example.BookingAppTeam05.model.users.*;
import com.example.BookingAppTeam05.service.EmailService;
import com.example.BookingAppTeam05.service.LoyaltyProgramService;
import com.example.BookingAppTeam05.service.PlaceService;
import com.example.BookingAppTeam05.service.ReservationService;
import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private PlaceService placeService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private EmailService emailService;
    private RoleService roleService;
    private LoyaltyProgramService loyaltyProgramService;
    private BookingEntityService bookingEntityService;
    private ReservationService reservationService;
    private InstructorService instructorService;
    private CottageOwnerService cottageOwnerService;
    private ShipOwnerService shipOwnerService;

    @Autowired
    public UserService(UserRepository userRepository, PlaceService placeService, EmailService emailService,
                       RoleService roleService, LoyaltyProgramService loyaltyProgramService,
                       InstructorService instructorService, ShipOwnerService shipOwnerService,
                       CottageOwnerService cottageOwnerService, @Lazy BookingEntityService bookingEntityService,
                       ReservationService reservationService) {
        this.userRepository = userRepository;
        this.placeService = placeService;
        this.emailService = emailService;
        this.roleService = roleService;
        this.loyaltyProgramService = loyaltyProgramService;
        this.instructorService = instructorService;
        this.shipOwnerService = shipOwnerService;
        this.cottageOwnerService = cottageOwnerService;
        this.bookingEntityService = bookingEntityService;
        this.reservationService = reservationService;
    }

    public UserService(){}

    public User findUserById(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null)
            throw new ItemNotFoundException("Can't find user with id: " + id);
        LoyaltyProgramEnum loyaltyProgramType = loyaltyProgramService.getLoyaltyProgramTypeFromUserPoints(user.getLoyaltyPoints());
        user.setLoyaltyProgramEnum(loyaltyProgramType);
        return user;
    }

    public UserDTO findUserDTOById(Long id) {
        User u = findUserById(id);
        UserDTO userDTO = new UserDTO(u);
        userDTO.setPlace(u.getPlace());
        if (u.getRole().getName().equals("ROLE_CLIENT")) {
            userDTO.setPenalties(((Client) u).getPenalties());
        }
        if (u.getRole().getName().equals("ROLE_SHIP_OWNER")) {
            userDTO.setCaptain(((ShipOwner) u).isCaptain());
        }
        return userDTO;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public List<UserDTO> getAllUsersDTO() {
        List<User> users = getAllUsers();
        List<UserDTO> retVal = new ArrayList<>();
        for (User u : users) {
            retVal.add(new UserDTO(u));
        }
        return retVal;
    }

    public void updateUser(Long userId, UserDTO userDTO){
        try{
        User user = userRepository.findUserById(userId);
        user.setAddress(userDTO.getAddress());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        if(userDTO.getVersion() != user.getVersion())
            throw new OptimisticLockException("Can't update user because it's already updated with new data, please refresh page.");
        if (userDTO.getPlace() == null)
            throw new ItemNotFoundException("Can't update user without set place");
        Place place = placeService.getPlaceById(userDTO.getPlace().getId());
        if (place == null)
            throw new ItemNotFoundException("Can't find place.");
        user.setPlace(place);
        if (userDTO.getUserType().toString().equals("ROLE_SHIP_OWNER")){
            ((ShipOwner) user).setCaptain(userDTO.isCaptain());
        }
        userRepository.save(user);
        } catch (Exception ex){
            throw new EditItemException("Error happened on server. Can't update user");
        }
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
    public void createUser(UserDTO userDTO) {
        if (userDTO.getPlace() == null)
            throw new ItemNotFoundException("Can't create new user without set place.");
        Place place = placeService.getPlaceById(userDTO.getPlace().getId());
        if (place == null)
            throw new ItemNotFoundException("Can't find place with id: " + userDTO.getPlace().getId());
        try {
            if (findUserByEmailCanBeDeletedAndNotYetActivated("bookingapp05mzr++" + userDTO.getEmail()) != null)
                throw new CreateItemException("User with email address: " + userDTO.getEmail() + " already exist.");
        } catch (UsernameNotFoundException ue){
            throw new ItemNotFoundException("Can't find user.");
        }
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
        if (u == null)
            throw new CreateItemException("Error happened on server. Can't create user: " + userDTO.getEmail() + " " + userDTO.getFirstName() + " " + userDTO.getLastName());
        try {
            u.setVersion(0L);
            userRepository.save(u);
            if (userDTO.getUserTypeValue().equals("ROLE_CLIENT")){
                //emailService.sendActivationMessage(u);
            }
        } catch (Exception e) {
            throw new CreateItemException("Error happened on server. Can't create user: " + userDTO.getEmail() + " " + userDTO.getFirstName() + " " + userDTO.getLastName());
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
    public void giveResponseForNewAccountRequest(NewAccountRequestDTO d) {
        try {
            if (d.getUser() == null)
                throw new ItemNotFoundException("Can't find user.");
            User user = userRepository.findUserById(d.getUser().getId());
            if (user == null)
                throw new ItemNotFoundException("Cant find user with id " + d.getUser().getId());
            if (!user.isNotYetActivated())
                throw new EditItemException("This user account is already approved.");
            user.setNotYetActivated(false);
            userRepository.save(user);

            if (!d.isAccepted()) {
                userRepository.physicalDeleteUserById(d.getUser().getId());
            }
            try {
                emailService.sendEmailAsAdminResponseFromNewAccountRequest(d);
            } catch (InterruptedException e) {
                throw new NotificationByEmailException("Error happened while sending email about new account request to user.");
            }
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException("Conflict seems to have occurred, another admin has reviewed this request before you. Please refresh page and try again");
        }
    }

    public void activateAccount(String email) {
        User user = userRepository.findByEmailAndNotYetActivated(email);
        if (user == null) {
            throw new EditItemException("Can't find not activated user with email " + email);
        }
        user.setNotYetActivated(false);
        try {
            userRepository.save(user);
        } catch (Exception ex){
            throw new EditItemException("Account not activated.");
        }
    }

    public List<BookingEntity> getBookingEntitiesByOwnerId(Long id) {
        User owner = this.findUserById(id);
        if (owner == null)
            throw new ItemNotFoundException("Can't find user with id: " + id);
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
                throw new UnauthorisedException("Unauthorised action for user with id " + id);
            }
        }
    }

    private boolean checkIfOwnerHaveActiveReservationsForOneOfHisEntities(Long userId) {
        List<BookingEntity> bookingEntities = this.getBookingEntitiesByOwnerId(userId);
        for (BookingEntity b : bookingEntities) {
            if (bookingEntityService.checkExistActiveReservationForEntityId(b.getId()))
                return true;
        }
        return false;
    }

    @Transactional
    public void tryToLogicalDeleteUser(Long userId, Long adminId, String confirmPass) {
        User admin = this.findUserById(adminId);
        if (admin == null)
            throw new ItemNotFoundException("Admin with id: " + adminId + " is not found");

        User userToDeleted = this.findUserById(userId);
        if (userToDeleted == null)
            throw new ItemNotFoundException("Can't find user for deleting. User id: " + userId);

        if (userToDeleted.getRole().getName().equals("ROLE_ADMIN") || userToDeleted.getRole().getName().equals("ROLE_SUPER_ADMIN"))
            throw new DeleteItemException("Not allowed to delete other admins");

        if (!admin.getRole().getName().equals("ROLE_ADMIN") && !admin.getRole().getName().equals("ROLE_SUPER_ADMIN"))
            throw new DeleteItemException("You don't have permission of deleting other users");

        if (!this.passwordIsCorrect(admin, confirmPass))
            throw new DeleteItemException("Confirmation password is incorrect.");

        if (userToDeleted.getRole().getName().equals("ROLE_CLIENT")) {
            if (reservationService.getAllActiveOrFutureReservationsForClientId(userId).size() > 0)
                throw new DeleteItemException("Can't delete client with id: " + userId + " because client has active or future reservations");
            else
                userRepository.logicalDeleteUserById(userId);
        }

        if (checkIfOwnerHaveActiveReservationsForOneOfHisEntities(userId))
            throw new DeleteItemException("Can't delete owner with id: " + userId + " because he still has some active reservations");
        else {
            userRepository.logicalDeleteUserById(userId);
        }
    }

    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        User user = findUserById(changePasswordDTO.getId());
        if (user == null){
            throw new ItemNotFoundException("Can't find user with id: " + changePasswordDTO.getId());
        }
        if (passwordIsCorrect(user, changePasswordDTO.getNewPassword()))
            throw new EditItemException("Please choose different passwords.");
        if (passwordIsCorrect(user, changePasswordDTO.getCurrPassword()))
            setNewPasswordForUser(user, changePasswordDTO.getNewPassword());
        else
            throw new EditItemException("Entered password is incorrect");
    }

    public void checkIfCanEditUser(String email) {
        User user = findUserByEmail(email);
        if (user != null)
            throw new EditItemException("This email is already taken.x");
    }

    public LoyaltyProgramEnum findUsersLoyalityProgramById(Long id) {
        User user = userRepository.findUsersLoyaltyProgram(id);
        return  user.getLoyaltyProgramEnum();
    }
}
