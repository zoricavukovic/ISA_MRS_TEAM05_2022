package com.example.BookingAppTeam05.astudent3.JUnit;

import com.example.BookingAppTeam05.dto.calendar.UnavailableDateDTO;
import com.example.BookingAppTeam05.model.UnavailableDate;
import com.example.BookingAppTeam05.model.repository.UnavailableDateRepository;
import com.example.BookingAppTeam05.service.UnavailableDateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnavailableDatesTest {

    @Mock
    private UnavailableDateRepository unavailableDateRepositoryMock;

    @Mock
    private UnavailableDate unavailableDateMock;

    @InjectMocks
    private UnavailableDateService unavailableDateService;

    @Test
    @Transactional
    @Rollback(true)
    public void getActiveUnavailableDateDTOsForEntityIdTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDate now = LocalDate.now();
        LocalDate dt1 = now.plusDays(1);
        LocalDate dt2 = now.plusDays(10);

        LocalDate dt3 = now.plusDays(25);
        LocalDate dt4 = now.plusDays(30);


        LocalDateTime t1 = LocalDateTime.parse("2022-06-04 00:00", formatter);
        LocalDateTime t2 = LocalDateTime.parse("2022-06-10 00:00", formatter);

        LocalDateTime t3 = LocalDateTime.parse("2022-06-12 00:00", formatter);
        LocalDateTime t4 = LocalDateTime.parse("2022-06-14 00:00", formatter);

        LocalDateTime t5 = LocalDateTime.parse("2022-05-05 00:00", formatter);
        LocalDateTime t6 = LocalDateTime.parse("2022-05-06 00:00", formatter);

        LocalDateTime t7 = LocalDateTime.parse( dt1.toString() + " 00:00", formatter);
        LocalDateTime t8 = LocalDateTime.parse(dt2.toString() + " 00:00", formatter);

        LocalDateTime t9 = LocalDateTime.parse( dt3.toString() + " 00:00", formatter);
        LocalDateTime t10 = LocalDateTime.parse(dt4.toString() + " 00:00", formatter);


        UnavailableDate unavailableDate1 = new UnavailableDate(t1, t2);
        UnavailableDate unavailableDate2 = new UnavailableDate(t3, t4);
        UnavailableDate unavailableDate3 = new UnavailableDate(t5, t6);
        UnavailableDate unavailableDate4 = new UnavailableDate(t7, t8);
        UnavailableDate unavailableDate5 = new UnavailableDate(t9, t10);
        List<UnavailableDate> mockData = new ArrayList<>();
        mockData.add(unavailableDate1);
        mockData.add(unavailableDate2);
        mockData.add(unavailableDate3);
        mockData.add(unavailableDate4);
        mockData.add(unavailableDate5);

        when(unavailableDateRepositoryMock.findAllSortedUnavailableDatesForEntityId(1L)).thenReturn(mockData);
        List<UnavailableDateDTO> result = unavailableDateService.getActiveUnavailableDateDTOsForEntityId(1L);
        assertThat(result).hasSize(2);
    }
}
