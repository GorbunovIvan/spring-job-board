package com.example.springjobboard.controller.converters;

import com.example.springjobboard.model.jobs.JobType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class JobTypeConverter implements Converter<String, JobType> {

    @Override
    public JobType convert(String source) {
        return JobType.getByName(source);
    }
}
