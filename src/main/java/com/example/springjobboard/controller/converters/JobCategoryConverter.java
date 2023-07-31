package com.example.springjobboard.controller.converters;

import com.example.springjobboard.model.jobs.JobCategory;
import com.example.springjobboard.repository.JobCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobCategoryConverter implements Converter<String, JobCategory> {

    private final JobCategoryRepository jobCategoryRepository;

    @Override
    public JobCategory convert(String source) {
        JobCategory jobCategory = jobCategoryRepository.findByField("name", source);
//        if (jobCategory == null) {
//            jobCategory = jobCategoryRepository.save(new JobCategory(source));
//        }
        return jobCategory;
    }
}
