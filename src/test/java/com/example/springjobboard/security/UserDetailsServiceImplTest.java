package com.example.springjobboard.security;

import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    private Set<User> users;

    @BeforeEach
    @Transactional
    void setUp() {
        users = userRepository.findAll();
        assertTrue(users.size() > 1);
    }

    @Test
    void loadUserByUsername() {

        for (var user : users) {
            assertEquals(user, userDetailsService.loadUserByUsername(user.getUsername()));
        }

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("none"));
    }
}