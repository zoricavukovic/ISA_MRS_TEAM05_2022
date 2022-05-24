package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.dto.users.UserRequestDTO;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.repository.users.UserRepository;
import com.example.BookingAppTeam05.model.users.Admin;
import com.example.BookingAppTeam05.model.users.ShipOwner;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PlaceService placeService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, PlaceService placeService) {
        this.userRepository = userRepository;
        this.placeService = placeService;
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id);
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

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
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

    public String createUser(UserDTO userDTO) {
        return "";
    }
}
