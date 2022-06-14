package com.example.BookingAppTeam05.student1.Integration;


import com.example.BookingAppTeam05.controller.users.UserController;
import com.example.BookingAppTeam05.dto.users.ClientDTO;
import com.example.BookingAppTeam05.dto.users.UserDTO;
import com.example.BookingAppTeam05.model.LoyaltyProgramEnum;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.users.Role;
import com.example.BookingAppTeam05.model.users.User;
import com.example.BookingAppTeam05.service.PlaceService;
import com.example.BookingAppTeam05.service.users.RoleService;
import com.example.BookingAppTeam05.service.users.UserService;
import com.example.BookingAppTeam05.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class UserControllerTest {

    private static final String URL_PREFIX = "/users";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PlaceService placeService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"CLIENT"})
    public void getById() throws Exception {
        mockMvc.perform(get(URL_PREFIX+"/"+7L)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.firstName").value("Jescie"))
                .andExpect(jsonPath("$.lastName").value("Mullins"));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getAll() throws Exception {
        mockMvc.perform(get(URL_PREFIX)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(21)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(7)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem("Jescie")))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem("Mullins")));

    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"CLIENT"})
    public void update() throws Exception {
        Client client = (Client) userService.findUserById(7L);
        ClientDTO clientDTO = new ClientDTO(client);

        clientDTO.setFirstName("Jescie");
        clientDTO.setLastName("Peric");
        clientDTO.setPlace(placeService.getPlaceById(1L));
        clientDTO.setPenalties(0);
        clientDTO.setAddress("Neka nova adresa");
        clientDTO.setDateOfBirth(null);

        String json = TestUtil.json(clientDTO);
        this.mockMvc.perform(put(URL_PREFIX+"/updateUser/"+7L).contentType(contentType).content(json)).
                andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"CLIENT"})
    public void save() throws Exception {

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(100L);
        clientDTO.setFirstName("Pera");
        clientDTO.setLastName("Peric");
        clientDTO.setPenalties(0);
        clientDTO.setPassword("123");
        clientDTO.setAccountAllowed(true);
        Place place = placeService.getPlaceById(1L);
        clientDTO.setPlace(place);
        clientDTO.setUserTypeValue("ROLE_CLIENT");
        clientDTO.setAddress("Neka adresa");
        clientDTO.setEmail("pera@gmail.com");
        clientDTO.setPhoneNumber("456464654");
        clientDTO.setLoyaltyProgram(LoyaltyProgramEnum.REGULAR);
        clientDTO.setAccountAllowed(true);

        String json = TestUtil.json(clientDTO);
        this.mockMvc.perform(post(URL_PREFIX+"/createUser").contentType(contentType).content(json)).
        andExpect(status().isCreated());
    }


}
