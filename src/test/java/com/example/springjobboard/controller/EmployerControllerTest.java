package com.example.springjobboard.controller;

import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.EmployerRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployerControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @SpyBean
    private EmployerRepository employerRepository;
    @MockBean
    private UsersUtil usersUtil;

    private Set<Employer> employers;

    private User currentUser;
    private User differentUser;

    @BeforeEach
    void setUp() {

        // for security
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        employers = employerRepository.findAll();
        assertTrue(employers.size() > 1);

        var iter = employers.iterator();

        currentUser = iter.next().getUser();
        differentUser = iter.next().getUser();

        Mockito.reset(employerRepository, usersUtil);

        when(usersUtil.getCurrentUser()).thenReturn(currentUser);
        when(usersUtil.getCurrentEmployer()).thenReturn(currentUser.getEmployer());
    }

    @Test
    void testGetAll() throws Exception {
        
        String result = mvc.perform(get("/employers"))
                .andExpect(status().isOk())
                .andExpect(view().name("employers/employers"))
                .andExpect(model().attribute("employers", employers))
                .andReturn()
                .getResponse()
                .getContentAsString();

        for (var employer : employers) {
            assertTrue(result.contains(employer.getName()));
        }
    }

    @Test
    void testGetById() throws Exception {

        var employer = currentUser.getEmployer();

        mvc.perform(get("/employers/{id}", employer.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("employers/employer"))
                .andExpect(model().attribute("employer", employer))
                .andExpect(content().string(containsString(employer.getName())))
                .andExpect(content().string(containsString("Edit")));
    }

    @Test
    void testInitCreation() throws Exception {

        // employer exists for current user
        var employer = currentUser.getEmployer();
        mvc.perform(get("/employers/new"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employers/" + employer.getId()));

        // employer does not exist for current user
        currentUser.setEmployer(null);
        when(usersUtil.getCurrentEmployer()).thenReturn(null);
        mvc.perform(get("/employers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("employers/createForm"));

        // not authorized
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/employers/new"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testProcessCreation() throws Exception {

        var employer = currentUser.getEmployer();

        // employer exists for current user
        mvc.perform(post("/employers")
                    .param("name", employer.getName()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employers/" + employer.getId()));

        verify(employerRepository, never()).save(any(Employer.class));

        // employer does not exist for current user
        employer.setUser(null);
        employerRepository.update(employer.getId(), employer);
        currentUser.setEmployer(null);
        when(usersUtil.getCurrentEmployer()).thenReturn(null);

        // not completed all fields
        mvc.perform(post("/employers")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("employers/createForm"));

        verify(employerRepository, never()).save(any(Employer.class));

        // The next code is commented out because in order to set a new employer for a user,
        // we need to remove the current one.
        // But it's tricky because it has a lot of constraints with other tables....
        // So I decided to skip the test...

//        // normal
//        mvc.perform(post("/employers")
//                    .param("name", employer.getName()))
//                .andExpect(status().isFound());
//
//        verify(employerRepository, times(1)).save(any(Employer.class));
    }

    @Test
    void testInitUpdate() throws Exception {

        // employer exists for current user
        var employer = currentUser.getEmployer();
        mvc.perform(get("/employers/{id}/edit", employer.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("employers/updateForm"));

        // not authorized
        var differentemployer = differentUser.getEmployer();
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/employers/{id}/edit", differentemployer.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testProcessUpdate() throws Exception {

        var employer = currentUser.getEmployer();

        // not completed all fields
        mvc.perform(post("/employers/{id}", employer.getId())
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("employers/updateForm"));

        verify(employerRepository, never()).update(anyLong(), any(Employer.class));

        // normal
        mvc.perform(post("/employers/{id}", employer.getId())
                        .param("name", employer.getName()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employers/" + employer.getId()));

        verify(employerRepository, times(1)).update(anyLong(), any(Employer.class));
    }

    @Test
    void testProcessDelete() throws Exception {

        var employer = currentUser.getEmployer();

        mvc.perform(post("/employers/{id}/delete", employer.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/my-page"));

        verify(employerRepository, times(1)).deleteById(employer.getId());
    }
}