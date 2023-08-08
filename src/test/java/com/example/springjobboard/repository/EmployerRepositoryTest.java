package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Employer;
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
class EmployerRepositoryTest {

    @Autowired
    private EmployerRepository employerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Set<Employer> employerExpected;

    @BeforeEach
    @Transactional
    void setUp() {

        employerExpected = entityManager.createQuery("FROM Employer", Employer.class)
                .getResultStream()
                .collect(Collectors.toSet());

        assertTrue(employerExpected.size() > 1);
    }

    @Test
    void testFindAll() {
        assertEquals(employerExpected, employerRepository.findAll());
    }

    @Test
    void testFindById() {

        for (var employerExpected : employerExpected) {
            assertEquals(employerExpected, employerRepository.findById(employerExpected.getId()));
        }

        assertNull(employerRepository.findById(-1L));
    }

    @Test
    void testFindByIdEagerly() {

        for (var employerExpected : employerExpected) {
            assertEquals(employerExpected, employerRepository.findByIdEagerly(employerExpected.getId()));
        }

        assertNull(employerRepository.findByIdEagerly(-1L));
    }

    @Test
    void testSave() {

        var user = User.builder()
                .name("Name new test user")
                .email("newTestUser@mail.com")
                .password("password")
                .build();

        entityManager.persist(user);
        entityManager.flush();

        var newApplicant = Employer.builder()
                .name("Name test empl new")
                .user(user)
                .build();

        assertEquals(newApplicant, employerRepository.save(newApplicant));
    }

    @Test
    void testUpdate() {

        for (var employerExpected : employerExpected) {
            var applicant = employerRepository.update(employerExpected.getId(), employerExpected);
            assertEquals(employerExpected, applicant);
        }

        assertThrows(RuntimeException.class, () -> employerRepository.update(-1L, new Employer()));
    }

    @Test
    void testDeleteById() {

        assertFalse(employerRepository.deleteById(-1L));

        for (var employerExpected : employerExpected) {
            assertTrue(employerRepository.deleteById(employerExpected.getId()));
        }
    }
}