package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Set<User> usersExpected;

    @BeforeEach
    @Transactional
    void setUp() {

        usersExpected = entityManager.createQuery("FROM User", User.class)
                .getResultStream()
                .collect(Collectors.toSet());

        assertTrue(usersExpected.size() > 1);
    }

    @Test
    void testFindAll() {
        assertEquals(usersExpected, userRepository.findAll());
    }

    @Test
    void testFindById() {

        for (var userExpected : usersExpected) {
            assertEquals(userExpected, userRepository.findById(userExpected.getId()));
        }

        assertNull(userRepository.findById(-1L));
    }

    @Test
    void testFindByEmailEagerly() {

        for (var userExpected : usersExpected) {
            assertEquals(userExpected, userRepository.findByEmailEagerly(userExpected.getEmail()));
        }

        assertNull(userRepository.findByEmailEagerly("none"));
    }

    @Test
    void testFindByIdEagerly() {

        for (var userExpected : usersExpected) {
            assertEquals(userExpected, userRepository.findByIdEagerly(userExpected.getId()));
        }

        assertNull(userRepository.findByIdEagerly(-1L));
    }

    @Test
    void testSave() {

        var newUser = User.builder()
                .name("Name new user")
                .email("newUser@mail.com")
                .password("password")
                .isActive(true)
                .build();

        assertEquals(newUser, userRepository.save(newUser));
    }

    @Test
    void testUpdate() {

        for (var userExpected : usersExpected) {
            var User = userRepository.update(userExpected.getId(), userExpected);
            assertEquals(userExpected, User);
        }

        assertThrows(RuntimeException.class, () -> userRepository.update(-1L, new User()));
    }

    @Test
    void testDelete() {
        for (var userExpected : usersExpected) {
            assertTrue(userRepository.delete(userExpected));
        }
    }

    @Test
    void testDeleteById() {

        assertFalse(userRepository.deleteById(-1L));

        for (var userExpected : usersExpected) {
            assertTrue(userRepository.deleteById(userExpected.getId()));
        }
    }
}