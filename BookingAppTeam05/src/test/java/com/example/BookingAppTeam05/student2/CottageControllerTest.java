package com.example.BookingAppTeam05.student2;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashSet;

import com.example.BookingAppTeam05.dto.entities.CottageDTO;
import com.example.BookingAppTeam05.model.Place;
import com.example.BookingAppTeam05.model.Room;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.service.PlaceService;
import com.example.BookingAppTeam05.service.RoomService;
import com.example.BookingAppTeam05.service.entities.CottageService;
import com.example.BookingAppTeam05.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class CottageControllerTest {

    private static final String URL_PREFIX = "/cottages";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CottageService cottageService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private RoomService roomService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getCottages() throws Exception {
        mockMvc.perform(get(URL_PREFIX)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$.[*].id").value(hasItem(1)))
                .andExpect(jsonPath("$.[*].name").value(hasItem("Ultrices Limited")))
                .andExpect(jsonPath("$.[*].address").value(hasItem("Ap #977-2514 Sed Street")))
                .andExpect(jsonPath("$.[*].promoDescription").value(hasItem("est, mollis non, cursus non, egestas a,")));

    }

    @Test
    @Transactional
    @Rollback(true)
    public void getCottageById() throws Exception {
        mockMvc.perform(get(URL_PREFIX+"/"+1L)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ultrices Limited"))
                .andExpect(jsonPath("$.address").value("Ap #977-2514 Sed Street"))
                .andExpect(jsonPath("$.promoDescription").value("est, mollis non, cursus non, egestas a,"));
    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"COTTAGE_OWNER"})
    public void updateCottage() throws Exception {
        Cottage cottage = cottageService.getCottageById(5L);
        CottageDTO cottageDTO = new CottageDTO(cottage);
        cottageDTO.setName("novo ime vikendice");
        cottageDTO.setAddress("nova adresa");
        cottageDTO.setPromoDescription("novi opis");
        cottageDTO.setPlace(cottage.getPlace());
        cottageDTO.setRooms(cottage.getRooms());
        cottageDTO.setRulesOfConduct(cottage.getRulesOfConduct());

        String json = TestUtil.json(cottageDTO);
        this.mockMvc.perform(put(URL_PREFIX+"/"+5L).contentType(contentType).content(json)).
                andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"COTTAGE_OWNER"})
    public void saveCottage() throws Exception {

        CottageDTO cottageDTO = new CottageDTO();
        cottageDTO.setName("ime_vikendice");
        cottageDTO.setAddress("adresa");
        cottageDTO.setPromoDescription("opis");
        cottageDTO.setEntityCancelationRate(10);
        Place place = placeService.getPlaceById(1L);
        cottageDTO.setPlace(place);
        Room room = new Room();
        room.setRoomNum(2);
        room.setNumOfBeds(2);
        room.setDeleted(false);
        HashSet<Room> rooms = new HashSet<>();
        rooms.add(room);
        cottageDTO.setRooms(rooms);

        String json = TestUtil.json(cottageDTO);
        this.mockMvc.perform(post(URL_PREFIX+"/" + 1L).contentType(contentType).content(json)).
                andExpect(status().isCreated());
    }

}