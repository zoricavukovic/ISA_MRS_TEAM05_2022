package com.example.BookingAppTeam05.student2;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.transaction.Transactional;

import com.example.BookingAppTeam05.model.*;
import com.example.BookingAppTeam05.model.entities.Cottage;
import com.example.BookingAppTeam05.model.entities.EntityType;
import com.example.BookingAppTeam05.model.repository.entities.CottageRepository;
import com.example.BookingAppTeam05.model.users.Client;
import com.example.BookingAppTeam05.model.users.CottageOwner;
import com.example.BookingAppTeam05.service.*;
import com.example.BookingAppTeam05.service.entities.CottageService;
import com.example.BookingAppTeam05.service.users.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CottageServiceTest {

    @Mock
    private CottageRepository cottageRepositoryMock;
    @Mock
    private ReservationService reservationServiceMock;
    @Mock
    private PictureService pictureServiceMock;
    @Mock
    private RoomService roomServiceMock;
    @Mock
    private RuleOfConductService ruleOfConductServiceMock;
    @Mock
    private PlaceService placeServiceMock;
    @Mock
    private UserService userServiceMock;

    @Mock
    private Cottage cottageMock;

    private @Mock List<Cottage> cottages;

    @InjectMocks
    private CottageService cottageService;



    @Test
    @Transactional
    @Rollback(true)
    public void testGetCottageById() {
        when(cottageRepositoryMock.getCottageById(1L)).thenReturn(new Cottage(
                "super vikendica", new HashSet<Picture>(), "adresa", "ime_vikendice",
                new HashSet<UnavailableDate>(), 12, EntityType.COTTAGE, new HashSet<Pricelist>(), new Place("1233", "Novi Sad", "Srbija", 43.7, 54.901),
                new HashSet<RuleOfConduct>(), new HashSet<Client>(), new HashSet<Room>(), new CottageOwner()
        ));

        Cottage cottage = cottageService.getCottageById(1L);

        assertEquals(cottage.getName(), "ime_vikendice");

        verify(cottageRepositoryMock, times(1)).getCottageById(1L);
        verifyNoMoreInteractions(cottageRepositoryMock);
    }

//    @Test
//    public void getAllUsers() {
//
//        when(cottageRepositoryMock.getCottagesByOwnerId(1L)).thenReturn(cottages);
//
//        List<Cottage> cottages =  cottageService.getCottagesByOwnerId(1L);
//
//        //assertThat()
//
//
//    }

    @Test
    @Transactional
    @Rollback(true)
    public void testAdd() {

        Cottage cottage = new Cottage();
        cottage.setName("vikendica2");
        cottage.setPromoDescription("opis2");
        cottage.setAddress("adresa2");

        // 1. Definisanje ponašanja mock objekata
        when(cottageRepositoryMock.findAll()).thenReturn(Arrays.asList(
                new Cottage(
                        "super vikendica", new HashSet<Picture>(), "adresa", "ime_vikendice",
                        new HashSet<UnavailableDate>(), 12, EntityType.COTTAGE, new HashSet<Pricelist>(), new Place("1233", "Novi Sad", "Srbija", 43.7, 54.901),
                        new HashSet<RuleOfConduct>(), new HashSet<Client>(), new HashSet<Room>(), new CottageOwner()
                )
        ));
        when(cottageRepositoryMock.save(cottage)).thenReturn(cottage);

        // 2. Akcija
        int dbSizeBeforeAdd = cottageService.findAll().size();

        Cottage dbCottage = cottageService.save(cottage);

        when(cottageRepositoryMock.findAll()).thenReturn(Arrays.asList(
                new Cottage(
                        "super vikendica", new HashSet<Picture>(), "adresa", "ime_vikendice",
                        new HashSet<UnavailableDate>(), 12, EntityType.COTTAGE, new HashSet<Pricelist>(), new Place("1233", "Novi Sad", "Srbija", 43.7, 54.901),
                        new HashSet<RuleOfConduct>(), new HashSet<Client>(), new HashSet<Room>(), new CottageOwner()
                ), cottage));

        // 3. Verifikacija: asertacije i/ili verifikacija interakcije sa mock objektima
        assertThat(dbCottage).isNotNull();

        List<Cottage> cottages = cottageService.findAll();
        assertThat(cottages).hasSize(dbSizeBeforeAdd + 1); //verifikacija da je nova vikendica upisana u bazu

        dbCottage = cottages.get(cottages.size() - 1); // preuzimanje poslednje vikendice

        assertThat(dbCottage.getName()).isEqualToIgnoringCase("vikendica2");
        assertThat(dbCottage.getPromoDescription()).isEqualTo("opis2");
        assertThat(dbCottage.getAddress()).isEqualTo("adresa2");

        verify(cottageRepositoryMock, times(2)).findAll();
        verify(cottageRepositoryMock, times(1)).save(cottage);
        verifyNoMoreInteractions(cottageRepositoryMock);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdate() {
        // 1. Definisanje ponašanja mock objekata
        when(cottageRepositoryMock.getCottageById(1L)).thenReturn(new Cottage(
                        "super vikendica", new HashSet<Picture>(), "adresa", "ime_vikendice",
                        new HashSet<UnavailableDate>(), 12, EntityType.COTTAGE, new HashSet<Pricelist>(), new Place("1233", "Novi Sad", "Srbija", 43.7, 54.901),
                        new HashSet<RuleOfConduct>(), new HashSet<Client>(), new HashSet<Room>(), new CottageOwner()
                )
        );

        // 2. Akcija
        Cottage cottageForUpdate = cottageService.getCottageById(1L);
        cottageForUpdate.setName("novo_ime_vikendice");
        cottageForUpdate.setPromoDescription("novi_opis");
        cottageForUpdate.setAddress("nova_adresa");

        when(cottageRepositoryMock.save(cottageForUpdate)).thenReturn(cottageForUpdate);

        cottageForUpdate = cottageService.save(cottageForUpdate);

        // 3. Verifikacija: asertacije i/ili verifikacija interakcije sa mock objektima
        assertThat(cottageForUpdate).isNotNull();

        cottageForUpdate = cottageService.getCottageById(1L); // verifikacija da se u bazi nalaze izmenjeni podaci
        assertThat(cottageForUpdate.getName()).isEqualToIgnoringCase("novo_ime_vikendice");
        assertThat(cottageForUpdate.getPromoDescription()).isEqualTo("novi_opis");
        assertThat(cottageForUpdate.getAddress()).isEqualTo("nova_adresa");

        verify(cottageRepositoryMock, times(2)).getCottageById(1L);
        verify(cottageRepositoryMock, times(1)).save(cottageForUpdate);
        verifyNoMoreInteractions(cottageRepositoryMock);
    }
}
