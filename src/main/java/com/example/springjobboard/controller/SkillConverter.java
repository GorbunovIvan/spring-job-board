package com.example.springjobboard.controller;

import com.example.springjobboard.model.users.Skill;
import com.example.springjobboard.repository.BasicRepositoryImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SkillConverter implements Converter<String, Skill> {

    private final BasicRepositoryImpl<Skill, Integer> skillRepository;

    public SkillConverter(BasicRepositoryImpl<Skill, Integer> basicRepository) {
        this.skillRepository = basicRepository;
        this.skillRepository.setClazz(Skill.class);
    }

    @Override
    public Skill convert(String source) {
        Skill skill = skillRepository.findByField("name", source);
        if (skill == null) {
            skill = skillRepository.save(new Skill(source));
        }
        return skill;
    }
}
