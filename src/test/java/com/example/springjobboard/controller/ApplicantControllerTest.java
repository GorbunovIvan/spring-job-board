package com.example.springjobboard.controller;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.ApplicantRepository;
import com.example.springjobboard.util.UsersUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApplicantControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @SpyBean
    private ApplicantRepository applicantRepository;
    @MockBean
    private UsersUtil usersUtil;

    private Set<Applicant> applicants;

    private User currentUser;
    private User differentUser;

    @BeforeEach
    void setUp() {

        // for security
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        applicants = applicantRepository.findAll();
        assertTrue(applicants.size() > 1);

        var iter = applicants.iterator();

        currentUser = iter.next().getUser();
        differentUser = iter.next().getUser();

        Mockito.reset(applicantRepository, usersUtil);

        when(usersUtil.getCurrentUser()).thenReturn(currentUser);
        when(usersUtil.getCurrentApplicant()).thenReturn(currentUser.getApplicant());
    }

    @Test
    void testGetAll() throws Exception {
        
        String result = mvc.perform(get("/applicants"))
                .andExpect(status().isOk())
                .andExpect(view().name("applicants/applicants"))
                .andExpect(model().attribute("applicants", applicants))
                .andReturn()
                .getResponse()
                .getContentAsString();

        for (var applicant : applicants) {
            assertTrue(result.contains(applicant.getFullName()));
        }
    }

    @Test
    void testGetById() throws Exception {

        var applicant = currentUser.getApplicant();

        mvc.perform(get("/applicants/{id}", applicant.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("applicants/applicant"))
                .andExpect(model().attribute("applicant", applicant))
                .andExpect(content().string(containsString(applicant.getFirstName())))
                .andExpect(content().string(containsString(applicant.getLastName())))
                .andExpect(content().string(containsString(applicant.getDescription())))
                .andExpect(content().string(containsString("Edit")));
    }

    @Test
    void testInitCreation() throws Exception {

        // applicant exists for current user
        var applicant = currentUser.getApplicant();
        mvc.perform(get("/applicants/new"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/applicants/" + applicant.getId()));

        // applicant does not exist for current user
        currentUser.setApplicant(null);
        when(usersUtil.getCurrentApplicant()).thenReturn(null);
        mvc.perform(get("/applicants/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("applicants/createForm"));

        // not authorized
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/applicants/new"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testProcessCreation() throws Exception {

        var applicant = currentUser.getApplicant();

        // applicant exists for current user
        mvc.perform(post("/applicants")
                    .param("firstName", applicant.getFirstName())
                    .param("lastName", applicant.getLastName())
                    .param("description", applicant.getDescription()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/applicants/" + applicant.getId()));

        verify(applicantRepository, never()).save(any(Applicant.class));

        // applicant does not exist for current user
        assertTrue(applicantRepository.delete(applicant));
        applicantRepository.findAll(); // wierd, but without this line the code does not work
        currentUser.setApplicant(null);
        when(usersUtil.getCurrentApplicant()).thenReturn(null);

        // not completed all fields
        mvc.perform(post("/applicants")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("applicants/createForm"));

        verify(applicantRepository, never()).save(any(Applicant.class));

        // normal
        mvc.perform(post("/applicants")
                    .param("firstName", applicant.getFirstName())
                    .param("lastName", applicant.getLastName())
                    .param("description", applicant.getDescription()))
                .andExpect(status().isFound());

        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    void testInitUpdate() throws Exception {

        // applicant exists for current user
        var applicant = currentUser.getApplicant();
        mvc.perform(get("/applicants/{id}/edit", applicant.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("applicants/updateForm"));

        // not authorized
        var differentApplicant = differentUser.getApplicant();
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/applicants/{id}/edit", differentApplicant.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testProcessUpdate() throws Exception {

        var applicant = currentUser.getApplicant();

        // not completed all fields
        mvc.perform(post("/applicants/{id}", applicant.getId())
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("applicants/updateForm"));

        verify(applicantRepository, never()).update(anyLong(), any(Applicant.class));

        // normal
        mvc.perform(post("/applicants/{id}", applicant.getId())
                        .param("firstName", applicant.getFirstName())
                        .param("lastName", applicant.getLastName())
                        .param("description", applicant.getDescription()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/applicants/" + applicant.getId()));

        verify(applicantRepository, times(1)).update(anyLong(), any(Applicant.class));
    }

    @Test
    void testProcessDelete() throws Exception {

        var applicant = currentUser.getApplicant();

        mvc.perform(post("/applicants/{id}/delete", applicant.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/my-page"));

        verify(applicantRepository, times(1)).deleteById(applicant.getId());
    }
}