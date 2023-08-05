package com.example.springjobboard.controller;

import com.example.springjobboard.model.jobs.Vacancy;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import com.example.springjobboard.repository.VacancyRepository;
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
class VacancyControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @SpyBean
    private VacancyRepository vacancyRepository;
    @MockBean
    private UsersUtil usersUtil;

    @Autowired
    private UserRepository userRepository;

    private Set<Vacancy> vacancies;

    private User currentUser;
    private User differentUser;

    @BeforeEach
    void setUp() {

        // for security
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        vacancies = vacancyRepository.findAll();
        assertTrue(vacancies.size() > 1);

        currentUser = userRepository.findByIdEagerly(1L);
        differentUser = userRepository.findByIdEagerly(2L);

        Mockito.reset(vacancyRepository, usersUtil);

        when(usersUtil.getCurrentUser()).thenReturn(currentUser);
        when(usersUtil.getCurrentApplicant()).thenReturn(currentUser.getApplicant());
        when(usersUtil.getCurrentEmployer()).thenReturn(currentUser.getEmployer());
    }

    @Test
    void testGetAll() throws Exception {
        
        String result = mvc.perform(get("/vacancies"))
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies/vacancies"))
                .andExpect(model().attribute("vacancies", vacancyRepository.findAll()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        for (var vacancy : vacancies) {
            assertTrue(result.contains(vacancy.getTitle()));
        }
    }

    @Test
    void tesGetAllByEmployer() throws Exception {

        var employer = currentUser.getEmployer();

        String result = mvc.perform(get("/vacancies/employer/{employerId}", employer.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies/vacancies"))
                .andExpect(model().attribute("vacancies", employer.getVacancies()))
                .andExpect(model().attribute("employer", employer))
                .andExpect(content().string(containsString("By employer:")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        for (var vacancy : employer.getVacancies()) {
            assertTrue(result.contains(vacancy.getTitle()));
        }
    }

    @Test
    void testGetAllForCurrentApplicant() throws Exception {

        var applicant = currentUser.getApplicant();

        // normal
        String result = mvc.perform(get("/vacancies/for-current-applicant"))
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies/vacancies"))
                .andExpect(model().attribute("vacancies", vacancyRepository.findAllForApplicantBySkills(applicant)))
                .andExpect(model().attribute("filter", "Suit your skills"))
                .andExpect(content().string(containsString("Suit your skills")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var vacanciesExpected = vacancyRepository.findAllForApplicantBySkills(applicant);

        for (var vacancy : vacanciesExpected) {
            assertTrue(result.contains(vacancy.getTitle()));
        }

        // if applicant is absent for current user
        currentUser.setApplicant(null);
        when(usersUtil.getCurrentApplicant()).thenReturn(null);
        mvc.perform(get("/vacancies/for-current-applicant"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/applicants/new"));

        // if not authorized
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/vacancies/for-current-applicant"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testGetById() throws Exception {

        var vacancy = currentUser.getEmployer().getVacancies().iterator().next();

        mvc.perform(get("/vacancies/{id}", vacancy.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies/vacancy"))
                .andExpect(model().attribute("vacancy", vacancy))
                .andExpect(content().string(containsString(vacancy.getTitle())))
                .andExpect(content().string(containsString(vacancy.getDescription())))
                .andExpect(content().string(containsString("Edit vacancy")));
    }

    @Test
    void testInitCreation() throws Exception {

        // normal
        mvc.perform(get("/vacancies/new")
                        .param("title", "title new vacancy")
                        .param("description", "description new vacancy"))
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies/createForm"))
                .andExpect(content().string(containsString("Add vacancy")));

        // if employer is absent for current user
        currentUser.setEmployer(null);
        when(usersUtil.getCurrentEmployer()).thenReturn(null);
        mvc.perform(get("/vacancies/new")
                        .param("title", "title new vacancy")
                        .param("description", "description new vacancy"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employers/new"));

        // if not authorized
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/vacancies/new")
                        .param("title", "title new vacancy")
                        .param("description", "description new vacancy"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testProcessCreation() throws Exception {

        // not completed all fields
        mvc.perform(post("/vacancies")
                        .param("title", "")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies/createForm"))
                .andExpect(content().string(containsString("Add vacancy")));

        verify(vacancyRepository, never()).save(any(Vacancy.class));

        // normal
        mvc.perform(post("/vacancies")
                        .param("title", "title new vacancy")
                        .param("description", "description new vacancy"))
                .andExpect(status().isFound());

        verify(vacancyRepository, times(1)).save(any(Vacancy.class));

        // if employer is absent for current user
        currentUser.setEmployer(null);
        when(usersUtil.getCurrentEmployer()).thenReturn(null);
        mvc.perform(post("/vacancies")
                        .param("title", "title new vacancy")
                        .param("description", "description new vacancy"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employers/new"));
    }

    @Test
    void testInitUpdate() throws Exception {

        var vacancy = currentUser.getEmployer().getVacancies().iterator().next();

        // normal
        mvc.perform(get("/vacancies/{id}/edit", vacancy.getId())
                        .param("title", vacancy.getTitle())
                        .param("description", vacancy.getDescription()))
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies/updateForm"))
                .andExpect(content().string(containsString("Update vacancy")));

        // if employer is absent for current user
        currentUser.setEmployer(null);
        when(usersUtil.getCurrentEmployer()).thenReturn(null);
        mvc.perform(get("/vacancies/{id}/edit", vacancy.getId())
                        .param("title", vacancy.getTitle())
                        .param("description", vacancy.getDescription()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employers/new"));

        // if not authorized
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/vacancies/{id}/edit", vacancy.getId())
                        .param("title", vacancy.getTitle())
                        .param("description", vacancy.getDescription()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testProcessUpdate() throws Exception {

        var vacancy = currentUser.getEmployer().getVacancies().iterator().next();

        // not completed all fields
        mvc.perform(post("/vacancies/{id}", vacancy.getId())
                        .param("title", "")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies/updateForm"))
                .andExpect(content().string(containsString("Update vacancy")));

        verify(vacancyRepository, never()).save(any(Vacancy.class));

        // normal
        mvc.perform(post("/vacancies/{id}", vacancy.getId())
                        .param("title", vacancy.getTitle())
                        .param("description", vacancy.getDescription()))
                .andExpect(status().isFound());

        verify(vacancyRepository, times(1)).update(anyLong(), any(Vacancy.class));

        // if employer is absent for current user
        currentUser.setEmployer(null);
        when(usersUtil.getCurrentEmployer()).thenReturn(null);
        mvc.perform(post("/vacancies/{id}", vacancy.getId())
                        .param("title", vacancy.getTitle())
                        .param("description", vacancy.getDescription()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employers/new"));
    }

    @Test
    void testProcessDelete() throws Exception {

        var vacancy = currentUser.getEmployer().getVacancies().iterator().next();

        mvc.perform(post("/vacancies/{id}/delete", vacancy.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/vacancies"));

        verify(vacancyRepository, times(1)).deleteById(vacancy.getId());
    }

    @Test
    void testRespond() throws Exception {

        var vacancy = differentUser.getEmployer().getVacancies().iterator().next();

        // normal
        mvc.perform(get("/vacancies/{id}/respond", vacancy.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/vacancies/" + vacancy.getId()));

        verify(vacancyRepository, times(1)).update(anyLong(), any(Vacancy.class));

        // if employer is absent for current user
        currentUser.setApplicant(null);
        when(usersUtil.getCurrentApplicant()).thenReturn(null);
        mvc.perform(get("/vacancies/{id}/respond", vacancy.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/applicants/new"));

        // if not authorized
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/vacancies/{id}/respond", vacancy.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }
}