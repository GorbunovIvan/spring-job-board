package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Skill;
import org.springframework.stereotype.Repository;

@Repository
public class SkillRepository extends BasicRepositoryImpl<Skill, Integer> {

    public SkillRepository() {
        setClazzForExtendedRepository(Skill.class);
    }
}
