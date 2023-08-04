package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Skill;
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
class SkillRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Set<Skill> skillsExpected;

    @BeforeEach
    @Transactional
    void setUp() {

        skillsExpected = entityManager.createQuery("FROM Skill", Skill.class)
                .getResultStream()
                .collect(Collectors.toSet());

        assertTrue(skillsExpected.size() > 1);
    }

    @Test
    void testFindAll() {
        assertEquals(skillsExpected, skillRepository.findAll());
    }

    @Test
    void testFindById() {

        for (var skillExpected : skillsExpected) {
            assertEquals(skillExpected, skillRepository.findById(skillExpected.getId()));
        }

        assertNull(skillRepository.findById(-1));
    }

    @Test
    void testFindByIdEagerly() {

        for (var skillExpected : skillsExpected) {
            assertEquals(skillExpected, skillRepository.findByIdEagerly(skillExpected.getId()));
        }

        assertNull(skillRepository.findByIdEagerly(-1));
    }

    @Test
    void testFindByField() {

        for (var jobCategoryExpected : skillsExpected) {
            assertEquals(jobCategoryExpected, skillRepository.findByField("name", jobCategoryExpected.getName()));
        }

        assertNull(skillRepository.findByField("name", ""));
    }

    @Test
    void testSave() {
        var newSkill = new Skill("new skill");
        assertEquals(newSkill, skillRepository.save(newSkill));
    }

    @Test
    void testUpdate() {

        for (var skillExpected : skillsExpected) {
            var Skill = skillRepository.update(skillExpected.getId(), skillExpected);
            assertEquals(skillExpected, Skill);
        }

        assertThrows(RuntimeException.class, () -> skillRepository.update(-1, new Skill()));
    }

    @Test
    void testDeleteById() {

        assertFalse(skillRepository.deleteById(-1));

        for (var skillExpected : skillsExpected) {
            assertTrue(skillRepository.deleteById(skillExpected.getId()));
        }
    }
}