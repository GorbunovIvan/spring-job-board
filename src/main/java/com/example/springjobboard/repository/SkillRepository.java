package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Skill;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class SkillRepository extends BasicRepositoryImpl<Skill, Integer> {

    public SkillRepository() {
        setClazzForExtendedRepository(Skill.class);
    }
}
