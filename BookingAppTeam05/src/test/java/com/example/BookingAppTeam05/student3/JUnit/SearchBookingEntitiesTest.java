package com.example.BookingAppTeam05.student3.JUnit;

import com.example.BookingAppTeam05.dto.SearchedBookingEntityDTO;
import com.example.BookingAppTeam05.dto.SimpleSearchForBookingEntityOwnerDTO;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.service.SearchService;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchBookingEntitiesTest {

    @Autowired
    private SearchService searchService;

    @Test
    @Transactional
    @Rollback(true)
    public void testSearchBookingEntities() {
        SearchedBookingEntityDTO s1 = new SearchedBookingEntityDTO("naziv1", "adresa1", null, "opis1", 34.0, 3.4f, null, EntityType.COTTAGE);
        SearchedBookingEntityDTO s2 = new SearchedBookingEntityDTO("naziv2", "adresa2", null, "opis2", 50.0, 3.0f, null, EntityType.COTTAGE);
        SearchedBookingEntityDTO s3 = new SearchedBookingEntityDTO("naziv3", "adr3", null, "opis3", 40.0, 2.2f, null, EntityType.ADVENTURE);
        SearchedBookingEntityDTO s4 = new SearchedBookingEntityDTO("dfff", "adresa1", null, "opis4", 60.0, 1.1f, null, EntityType.COTTAGE);
        SearchedBookingEntityDTO s5 = new SearchedBookingEntityDTO("afdfdf", "adresa4", null, "opis5", 90.0, 4.4f, null, EntityType.SHIP);
        List<SearchedBookingEntityDTO> entities = new ArrayList<>();
        entities.add(s1);
        entities.add(s2);
        entities.add(s3);
        entities.add(s4);
        entities.add(s5);

        SimpleSearchForBookingEntityOwnerDTO ss = new SimpleSearchForBookingEntityOwnerDTO("naz", "adre", null, 30.0, 100.0, 2.0f);
        List<SearchedBookingEntityDTO> result = searchService.simpleFilterSearchForBookingEntities(entities, ss);
        assertThat(result).hasSize(2);

        ss = new SimpleSearchForBookingEntityOwnerDTO("naz", "adre", null, 30.0, 100.0, 2.0f);
        result = searchService.simpleFilterSearchForBookingEntities(entities, ss);
        assertThat(result).hasSize(2);

        ss = new SimpleSearchForBookingEntityOwnerDTO("naz", "Adresa1", null, 30.0, 100.0, 2.0f);
        result = searchService.simpleFilterSearchForBookingEntities(entities, ss);
        assertThat(result).hasSize(1);

        ss = new SimpleSearchForBookingEntityOwnerDTO("naz", "adre", null, 150.0, 200.0, 2.0f);
        result = searchService.simpleFilterSearchForBookingEntities(entities, ss);
        assertThat(result).hasSize(0);

        ss = new SimpleSearchForBookingEntityOwnerDTO("", "", null, 0.0, 200.0, 2.0f);
        result = searchService.simpleFilterSearchForBookingEntities(entities, ss);
        assertThat(result).hasSize(4);
    }
}
