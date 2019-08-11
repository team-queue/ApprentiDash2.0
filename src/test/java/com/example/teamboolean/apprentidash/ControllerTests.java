package com.example.teamboolean.apprentidash;

import com.example.teamboolean.apprentidash.Models.Discussion;
import com.example.teamboolean.apprentidash.Repos.CommentRepository;
import com.example.teamboolean.apprentidash.Repos.DiscussionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;

import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.junit.Assert.assertEquals;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ContextConfiguration
public class ControllerTests {

    @Autowired
    static
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    private InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix("*.html");

        return viewResolver;
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    public static RequestPostProcessor testUser() throws Exception {
        return user("petty").password("password").roles("ADMIN");
    }

    // @GetMapping("/forum")
    @WithMockUser
    @Test
    public void testGetForum() throws Exception {
        mockMvc.perform(
                get("/forum").with(testUser())).andExpect(content().string(containsString("Create Discussion"))
        );
    }

    @Autowired
    DiscussionRepository discussionRepository;

    @Autowired
    CommentRepository commentRepository;

    @WithMockUser
    @Test
    public void testDiscussionCreateReadDelete() throws Exception {

        mockMvc.perform(
                post("/forum")
                    .with(testUser())
                    .param("title", "test discussion post")
                    .param("body", "this is the body of my test discussion post"))
                .andDo(print())
                .andExpect(content().string(containsString("test discussion post")));


        Discussion createdDiscussion = discussionRepository.findAllByBody("this is the body of my test discussion post").get(0);

        assertEquals("The created discussion should have an author whose name matches the logged in author's name.",
                createdDiscussion.getAuthor().getUsername(),
                "petty");
        System.out.println(createdDiscussion.getAuthor().getUsername());

        assertEquals("The created discussion should have a title which matches the given title.",
                createdDiscussion.getTitle(),
                "test discussion post");
        System.out.println(createdDiscussion.getTitle());

        // Next, let's try adding a comment to the test discussion.
        long createdDiscussionId = createdDiscussion.getId();


        mockMvc.perform(
                post("/forum/" + createdDiscussionId)
                    .with(testUser())
                    .param("body", "this is a new comment!!!!"))
                .andDo(print())
                .andExpect(content().string(containsString("this is a new comment!!!!")));


        // After deleting the discussion, findById should return null.

        mockMvc.perform(
                delete("/forum/" + createdDiscussionId + "/delete")
                .with(testUser()));

        assertNull("trying to find the deleted discussion should return null.", discussionRepository.findById(createdDiscussionId));
        assertEquals("the children comments in the deleted discussion should also have been deleted, so the returned list should have a size of 0.", 0, commentRepository.findAllByParentDiscussion(createdDiscussion).size());
    }
}
