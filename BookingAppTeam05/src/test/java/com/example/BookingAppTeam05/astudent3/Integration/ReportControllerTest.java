package com.example.BookingAppTeam05.astudent3.Integration;

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


import static com.example.BookingAppTeam05.constansts.ReportConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class ReportControllerTest {
    private static final String URL_PREFIX = "/reports";

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
    public void testGetUnprocessedReports() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/all/unprocessed")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(DB_COUNT_UNPROCESSED_REPORTS)))
                .andExpect(jsonPath("$.[*].ownerComment").value(hasItem(DB_COMMENT_REPORT)));
    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles={"ADMIN","SUPER_ADMIN"})
    public void testGetProcessedReports() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/all/processed")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(DB_COUNT_PROCESSED_REPORTS)))
                .andExpect(jsonPath("$.[*].adminResponse").value(hasItem(DB_ADMIN_RESPONSE_REPORT)));
    }



}
