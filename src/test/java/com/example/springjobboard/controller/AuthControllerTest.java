package com.example.springjobboard.controller;

import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(userRepository);
    }

    @Test
    void testInitLogin() throws Exception {

        mvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/loginForm"));
    }

    @Test
    void testInitRegistration() throws Exception {

        mvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registerForm"));
    }

    @Test
    void testProcessRegistration() throws Exception {

        var newUser = User.builder()
                .name("Name new user")
                .email("newUser@mail.com")
                .password("password")
                .build();

        // not completed all fields
        mvc.perform(post("/auth/register")
                        .param("name", "")
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registerForm"));

        verify(userRepository, never()).save(any(User.class));

        // normal
        mvc.perform(post("/auth/register")
                        .param("name", newUser.getName())
                        .param("email", newUser.getEmail())
                        .param("password", newUser.getPassword()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/users/my-page"));

        verify(userRepository, times(1)).save(any(User.class));
    }
}