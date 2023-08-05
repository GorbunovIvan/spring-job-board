package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Applicant;
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
class ApplicantRepositoryTest {

    @Autowired
    private ApplicantRepository applicantRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Set<Applicant> applicantsExpected;

    @BeforeEach
    @Transactional
    void setUp() {

        applicantsExpected = entityManager.createQuery("FROM Applicant", Applicant.class)
                .getResultStream()
                .collect(Collectors.toSet());

        assertTrue(applicantsExpected.size() > 1);
    }

    @Test
    void testFindAll() {
        assertEquals(applicantsExpected, applicantRepository.findAll());
    }

    @Test
    void testFindById() {

        for (var applicantExpected : applicantsExpected) {
            assertEquals(applicantExpected, applicantRepository.findById(applicantExpected.getId()));
        }

        assertNull(applicantRepository.findById(-1L));
    }

    @Test
    void testFindByIdEagerly() {

        for (var applicantExpected : applicantsExpected) {
            assertEquals(applicantExpected, applicantRepository.findByIdEagerly(applicantExpected.getId()));
        }

        assertNull(applicantRepository.findByIdEagerly(-1L));
    }

    @Test
    void testSave() {

        var user = User.builder()
                .name("new user")
                .email("newUser@mail.com")
                .password("password")
                .build();

        entityManager.persist(user);
        entityManager.flush();

        var newApplicant = Applicant.builder()
                .firstName("F name appl new")
                .lastName("L name appl new")
                .description("Desc appl new")
                .user(user)
                .build();

        assertEquals(newApplicant, applicantRepository.save(newApplicant));
    }

    @Test
    void testUpdate() {

        for (var applicantExpected : applicantsExpected) {
            var applicant = applicantRepository.update(applicantExpected.getId(), applicantExpected);
            assertEquals(applicantExpected, applicant);
        }

        assertThrows(RuntimeException.class, () -> applicantRepository.update(-1L, new Applicant()));
    }

    @Test
    void testDeleteById() {

        assertFalse(applicantRepository.deleteById(-1L));

        for (var applicantExpected : applicantsExpected) {
            assertTrue(applicantRepository.deleteById(applicantExpected.getId()));
        }
    }
}