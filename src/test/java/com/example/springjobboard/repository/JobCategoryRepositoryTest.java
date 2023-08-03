package com.example.springjobboard.repository;

import com.example.springjobboard.model.jobs.JobCategory;
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
class JobCategoryRepositoryTest {

    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Set<JobCategory> jobCategoriesExpected;

    @BeforeEach
    @Transactional
    void setUp() {

        jobCategoriesExpected = entityManager.createQuery("FROM JobCategory", JobCategory.class)
                .getResultStream()
                .collect(Collectors.toSet());

        assertTrue(jobCategoriesExpected.size() > 1);
    }

    @Test
    void testFindAll() {
        assertEquals(jobCategoriesExpected, jobCategoryRepository.findAll());
    }

    @Test
    void testFindById() {

        for (var jobCategoryExpected : jobCategoriesExpected) {
            assertEquals(jobCategoryExpected, jobCategoryRepository.findById(jobCategoryExpected.getId()));
        }

        assertNull(jobCategoryRepository.findById(-1));
    }

    @Test
    void testFindByIdEagerly() {

        for (var jobCategoryExpected : jobCategoriesExpected) {
            assertEquals(jobCategoryExpected, jobCategoryRepository.findByIdEagerly(jobCategoryExpected.getId()));
        }

        assertNull(jobCategoryRepository.findByIdEagerly(-1));
    }

    @Test
    void testSave() {
        var newJobCategory = new JobCategory("new job category");
        assertEquals(newJobCategory, jobCategoryRepository.save(newJobCategory));
    }

    @Test
    void testUpdate() {

        for (var jobCategoryExpected : jobCategoriesExpected) {
            var JobCategory = jobCategoryRepository.update(jobCategoryExpected.getId(), jobCategoryExpected);
            assertEquals(jobCategoryExpected, JobCategory);
        }

        assertThrows(RuntimeException.class, () -> jobCategoryRepository.update(-1, new JobCategory()));
    }

    @Test
    void testDeleteById() {

        assertFalse(jobCategoryRepository.deleteById(-1));

        for (var jobCategoryExpected : jobCategoriesExpected) {
            assertTrue(jobCategoryRepository.deleteById(jobCategoryExpected.getId()));
        }
    }
}