package com.example.springjobboard.util;

import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
//@PrepareForTest(SecurityContextHolder.class)
class UsersUtilTest {

    @Autowired
    private UsersUtil usersUtil;
    @Autowired
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    private User user;

    @BeforeEach
    void setUp() {

        user = userRepository.findById(1L);
        assertNotNull(user);

        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void getCurrentUser() {

        when(authentication.isAuthenticated()).thenReturn(true);
        assertEquals(user, usersUtil.getCurrentUser());

        when(authentication.isAuthenticated()).thenReturn(false);
        assertNull(usersUtil.getCurrentUser());
    }

    @Test
    void getCurrentEmployer() {

        when(authentication.isAuthenticated()).thenReturn(true);
        assertEquals(user.getEmployer(), usersUtil.getCurrentEmployer());

        when(authentication.isAuthenticated()).thenReturn(false);
        assertNull(usersUtil.getCurrentEmployer());
    }

    @Test
    void getCurrentApplicant() {

        when(authentication.isAuthenticated()).thenReturn(true);
        assertEquals(user.getApplicant(), usersUtil.getCurrentApplicant());

        when(authentication.isAuthenticated()).thenReturn(false);
        assertNull(usersUtil.getCurrentApplicant());
    }
}