package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.UserRequestDTO;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.users.Role;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService implements UserServiceI{
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private RoleService roleService;

    public UserService() {}

    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public User save(User user){return userRepository.save(user);}

    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public boolean passwordIsCorrect(User user, String checkPass) {
        return passwordEncoder.matches(checkPass, user.getPassword());
//        String encodePass = passwordEncoder.encode(checkPass);
//        System.out.println("----");
//        System.out.println(encodePass);
//        System.out.println(user.getPassword());
//        System.out.println("----");
//        return user.getPassword().equals(encodePass);
    }

    public User save(UserRequestDTO userRequest) {
        // TODO::

//        User u = new Client();
//        u.setEmail("klijent@gmail.com");
//        u.setFirstName("Klijent");
//        u.setLastName("Klijent");
//        u.setAddress("adresa1");
//        u.setPhoneNumber("0434434");
//        u.setNotYetActivated(true);
//        u.setEnabled(true);
//
//        // pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
//        // treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
////        u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
//
//        // u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
//        List<Role> roles = roleService.findByName("ROLE_USER");
//        u.setRoles(roles);
//
//        return this.userRepository.save(u);
        return null;
    }
}
