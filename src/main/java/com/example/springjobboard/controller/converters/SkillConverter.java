package com.example.springjobboard.controller.converters;

import com.example.springjobboard.model.users.Skill;
import com.example.springjobboard.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillConverter implements Converter<String, Skill> {

    private final SkillRepository skillRepository;

    @Override
    public Skill convert(String source) {
        return skillRepository.findByField("name", source);
    }
}
