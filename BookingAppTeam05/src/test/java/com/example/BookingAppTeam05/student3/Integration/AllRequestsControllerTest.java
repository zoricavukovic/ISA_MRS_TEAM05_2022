package com.example.BookingAppTeam05.student3.Integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.example.BookingAppTeam05.constansts.AllRequestsConstraints.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class AllRequestsControllerTest {
    private static final String URL_PREFIX = "/admins";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"ADMIN","SUPER_ADMIN"})
    public void testGetAllRequests() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/allRequestsNums")).andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationReportsCounter").value(DB_COUNT_RESERVATION_REPORT_COUNTER))
                .andExpect(jsonPath("$.clientRatingsCounter").value(DB_COUNT_CLIENT_RATING_COUNTER))
                .andExpect(jsonPath("$.clientComplaintsCounter").value(DB_CLIENT_COMPLAINT_COUNTER))
                .andExpect(jsonPath("$.deleteAccountRequests").value(DB_COUNT_DELETE_ACCOUNT_REQUESTS))
                .andExpect(jsonPath("$.newAccountRequests").value(DB_COUNT_NEW_ACCOUNT_REQUESTS));
    }


}
