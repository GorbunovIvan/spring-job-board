package com.example.springjobboard.controller;

import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @SpyBean
    private UserRepository userRepository;
    @MockBean
    private UsersUtil usersUtil;

    private User currentUser;

    @BeforeEach
    void setUp() {

        // for security
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        currentUser = userRepository.findByIdEagerly(1L);
        assertNotNull(currentUser);

        Mockito.reset(userRepository, usersUtil);

        when(usersUtil.getCurrentUser()).thenReturn(currentUser);
    }

    @Test
    void testGetCurrentUserPage() throws Exception {

        // normal
        mvc.perform(get("/users/my-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/user"))
                .andExpect(model().attribute("user", currentUser))
                .andExpect(content().string(containsString(currentUser.getName())))
                .andExpect(content().string(containsString("Delete account")));

        // if unauthorized
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/users/my-page"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testInitUpdate() throws Exception {

        // normal
        mvc.perform(get("/users/my-page/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", currentUser))
                .andExpect(view().name("users/updateForm"))
                .andExpect(content().string(containsString("Update user")));

        // not authorized
        when(usersUtil.getCurrentUser()).thenReturn(null);
        mvc.perform(get("/users/my-page/edit"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/auth/login"));
    }

    @Test
    void testProcessUpdate() throws Exception {

        // not completed all fields
        mvc.perform(post("/users/my-page/edit")
                        .param("name", "")
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("users/updateForm"));

        verify(userRepository, never()).update(anyLong(), any(User.class));

        // normal
        mvc.perform(post("/users/my-page/edit")
                        .param("name", currentUser.getName())
                        .param("email", currentUser.getEmail())
                        .param("password", currentUser.getPassword()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/my-page"));

        verify(userRepository, times(1)).update(anyLong(), any(User.class));
    }

    @Test
    void testProcessDelete() throws Exception {

        mvc.perform(post("/users/my-page/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        verify(userRepository, times(1)).delete(currentUser);
    }
}