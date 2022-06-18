package com.example.BookingAppTeam05.student1.JUnit;

import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.InstructorDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.users.Instructor;
import com.example.BookingAppTeam05.repository.users.UserRepository;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.users.Role;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.service.PlaceService;
import com.example.BookingAppTeam05.service.users.*;
import static org.junit.Assert.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private RoleService roleService;

    private @Mock List<User> users;

    @InjectMocks
    private UserService userService;

    @Test
    @Transactional
    @Rollback(true)
    void findUserById() {

        when(userRepositoryMock.findUserById(7L)).thenReturn(new Client("bookingapp05mzr++jescieMullins@gmail.com", "Jescie", "Mullins", "Ap #769-2030 Mauris. Rd.", LocalDate.of(1971,12,20), "034-33-356-88", "$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra", false, new Place(),new Role(), 0));

        User user = userService.findUserById(7L);

        assertEquals(user.getFirstName(), "Jescie");

        verify(userRepositoryMock, times(1)).findUserById(7L);

        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    @Transactional
    @Rollback(true)
    void updateUser() {
        Place place = placeService.getPlaceById(1L);
        Role role = roleService.findByName("ROLE_CLIENT");
        when(userRepositoryMock.findUserById(7L)).thenReturn(new Client("bookingapp05mzr++jescieMullins@gmail.com", "Jescie", "Mullins", "Ap #769-2030 Mauris. Rd.", LocalDate.of(1971,12,20), "034-33-356-88", "$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra", false, place,role, 0));

        User userForUpdate = userRepositoryMock.findUserById(7L);

        userForUpdate.setLastName("Janes");
        userForUpdate.setAddress("Neka nova adresa");
        //userService.updateUser(7L, userDTO);
        when(userRepositoryMock.save(userForUpdate)).thenReturn(userForUpdate);
        userService.save(userForUpdate);

        when(userRepositoryMock.findUserById(7L)).thenReturn(userForUpdate);

        userForUpdate = userRepositoryMock.findUserById(7L);

        assertThat(userForUpdate).isNotNull();
        assertThat(userForUpdate.getFirstName()).isEqualTo("Jescie");
        assertThat(userForUpdate.getLastName()).isEqualTo("Janes");
        assertThat(userForUpdate.getAddress()).isEqualTo("Neka nova adresa");

        verify(userRepositoryMock, times(2)).findUserById(7L);
        verify(userRepositoryMock,times(1)).save(userForUpdate);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    @Transactional
    @Rollback(true)
    void createUser() {


        Instructor instructor = new Instructor();
        instructor.setFirstName("Mika");
        instructor.setLastName("Mikic");
        instructor.setAddress("Bul. oslob. 27");
        instructor.setDateOfBirth(LocalDate.now());
        instructor.setPhoneNumber("043456789");
        instructor.setEmail("neki@gmail.com");
        instructor.setPassword("sifra123");
        instructor.setVersion(0L);
        Role role = roleService.findByName("ROLE_INSTRUCTOR");
        instructor.setRole(role);
        Place place = placeService.getPlaceById(1L);
        instructor.setPlace(place);

        InstructorDTO instructorDTO = new InstructorDTO(instructor);
        instructorDTO.setPlace(instructor.getPlace());
        when(userRepositoryMock.save(instructor)).thenReturn(instructor);
        when(userRepositoryMock.findByEmailAllUser("neki@gmail.com")).thenReturn(null);

        userService.save(instructor);
        when(userRepositoryMock.findUserById(7L)).thenReturn(instructor);

        User user = userRepositoryMock.findUserById(7L);

        assertEquals(user.getFirstName(), "Mika");

        verify(userRepositoryMock, times(1)).save(instructor);
        verify(userRepositoryMock, times(1)).findUserById(7L);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    @Transactional
    @Rollback(true)
    void logicalDeleteUserById() {


        Place place = placeService.getPlaceById(1L);
        Role role = roleService.findByName("ROLE_CLIENT");
        when(userRepositoryMock.findUserById(7L)).thenReturn(new Client("bookingapp05mzr++jescieMullins@gmail.com", "Jescie", "Mullins", "Ap #769-2030 Mauris. Rd.", LocalDate.of(1971,12,20), "034-33-356-88", "$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra", false, place,role, 0));

        User userForUpdate = userRepositoryMock.findUserById(7L);
        userForUpdate.setDeleted(true);
        when(userRepositoryMock.save(userForUpdate)).thenReturn(userForUpdate);

        when(userRepositoryMock.findUserById(7L)).thenReturn(userForUpdate);
        User user = userRepositoryMock.findUserById(7L);

        assertEquals(user.getFirstName(), "Jescie");

        verify(userRepositoryMock, times(2)).findUserById(7L);
        verifyNoMoreInteractions(userRepositoryMock);
    }
}