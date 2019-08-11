package com.example.teamboolean.apprentidash;

import com.example.teamboolean.apprentidash.Controllers.ApprentiDashController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IndividualRouteTest {

    @Autowired
    private ApprentiDashController apprentiDashController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    public static RequestPostProcessor testUser(){
        return user("test").password("test");
    }


    @Test
    public void testRoot() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void test_home() throws Exception {
        this.mockMvc.perform(get("/home")).andExpect(status().isOk());
    }

    @Test
    public void test_login() throws Exception {
        this.mockMvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    public void test_signup() throws Exception {
        this.mockMvc.perform(get("/signup")).andExpect(status().isOk());
    }


    @WithMockUser
    @Test
    public void test_recordHour() throws Exception {
        this.mockMvc.perform(get("/recordHour").with(testUser()))
            .andExpect(content().string(containsString("Clock In")));;

    }

    

    @WithMockUser
    @Test
    public void test_additionalDay() throws Exception {
        this.mockMvc.perform(get("/additionalDayRecord").with(testUser()));
    }

    @WithMockUser
    @Test
    public void test_summary() throws Exception {
        this.mockMvc.perform(get("/summary")
                .with(testUser()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    /**** NOTE WHEN RUNNING THIS TEST: dayId should be existing in the database to pass the test. Replace id (i.e 5)*******/

    @WithMockUser
    @Test
    public void test_edit() throws Exception {
        this.mockMvc.perform(get("/edit/{dayId}", 6).with(testUser()));
    }

    /**** NOTE WHEN RUNNING THIS TEST: dayId should be existing in the database to pass the test. Replace id (i.e 5)*******/
    @WithMockUser
    @Test
    public void test_delete() throws Exception {
        this.mockMvc.perform(get("/delete/{dayId}", 7)
                .with(testUser()))
                .andExpect(status().isOk());

    }

    @WithMockUser
    @Test
    public void test_timesheet() throws Exception {
        String filename = "timesheet.csv";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + filename + "\"");

        this.mockMvc.perform(get("/summary")
                .with(testUser())
                .headers(httpHeaders))
            .andExpect(status().isOk());

    }


}
