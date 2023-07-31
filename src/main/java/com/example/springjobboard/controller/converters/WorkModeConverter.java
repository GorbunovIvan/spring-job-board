package com.example.springjobboard.controller.converters;

import com.example.springjobboard.model.jobs.WorkMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WorkModeConverter implements Converter<String, WorkMode> {

    @Override
    public WorkMode convert(String source) {
        return WorkMode.getByName(source);
    }
}
