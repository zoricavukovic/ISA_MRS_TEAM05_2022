package com.example.BookingAppTeam05.astudent3.Integration;

import com.example.BookingAppTeam05.dto.users.NewAdventureDTO;
import com.example.BookingAppTeam05.model.entities.Adventure;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.service.PlaceService;
import com.example.BookingAppTeam05.service.users.InstructorService;
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

import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class AdventureControllerTest {
    private static final String URL_PREFIX = "/adventures";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private PlaceService placeService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"INSTRUCTOR"})
    public void testSaveBadAdventure() throws Exception {
        Adventure adventure = new Adventure("promoDesc", null, "adresa", "naziv", null, 3.3f, EntityType.ADVENTURE, null, null, null, null, "shortBio", 4, null, null);
        String json = TestUtil.json(adventure);
        this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"INSTRUCTOR"})
    public void testSaveValidAdventure() throws Exception {
        NewAdventureDTO newAdventureDTO = new NewAdventureDTO();
        newAdventureDTO.setInstructorId(13L);
        newAdventureDTO.setName("name");
        newAdventureDTO.setAddress("adresa");
        newAdventureDTO.setPlaceId(1L);
        newAdventureDTO.setCostPerPerson(34.3);
        newAdventureDTO.setMaxNumOfPersons(34);
        newAdventureDTO.setPromoDescription("promo");
        newAdventureDTO.setShortBio("shortBio");
        newAdventureDTO.setEntityCancelationRate(4);
        newAdventureDTO.setFishingEquipment(new ArrayList<>());
        newAdventureDTO.setAdditionalServices(new ArrayList<>());
        newAdventureDTO.setRulesOfConduct(new ArrayList<>());
        newAdventureDTO.setImages(new ArrayList<>());

        String json = TestUtil.json(newAdventureDTO);
        this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
    }
}
