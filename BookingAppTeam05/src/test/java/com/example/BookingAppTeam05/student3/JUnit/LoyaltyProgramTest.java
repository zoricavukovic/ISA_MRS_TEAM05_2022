package com.example.BookingAppTeam05.student3.JUnit;

import com.example.BookingAppTeam05.dto.LoyaltyProgramDTO;
import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.LoyaltyProgramEnum;
import com.example.BookingAppTeam05.service.LoyaltyProgramService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoyaltyProgramTest {


    @Test
    @Transactional
    @Rollback(true)
    public void testGetProgramFromLoyaltyPoints() {
        LoyaltyProgramService ls = new LoyaltyProgramService();
        LoyaltyProgramService loyaltyProgramService = Mockito.spy(ls);
        LoyaltyProgramDTO loyaltyProgramDTO = new LoyaltyProgramDTO(10,50,100,2.0, 2.5,3.0,4.0,5.0,6.0,2,2, LocalDateTime.now());

        doReturn(new LoyaltyProgram(loyaltyProgramDTO)).when(loyaltyProgramService).getCurrentLoyaltyProgram();

        LoyaltyProgramEnum program1 = loyaltyProgramService.getLoyaltyProgramTypeFromUserPoints(15);
        assertEquals(LoyaltyProgramEnum.BRONZE, program1);

        LoyaltyProgramEnum program2 = loyaltyProgramService.getLoyaltyProgramTypeFromUserPoints(10);
        assertEquals(LoyaltyProgramEnum.BRONZE, program2);

        LoyaltyProgramEnum program3 = loyaltyProgramService.getLoyaltyProgramTypeFromUserPoints(5);
        assertEquals(LoyaltyProgramEnum.REGULAR, program3);

        LoyaltyProgramEnum program4 = loyaltyProgramService.getLoyaltyProgramTypeFromUserPoints(90);
        assertEquals(LoyaltyProgramEnum.SILVER, program4);

        LoyaltyProgramEnum program5 = loyaltyProgramService.getLoyaltyProgramTypeFromUserPoints(105);
        assertEquals(LoyaltyProgramEnum.GOLD, program5);

    }

}
