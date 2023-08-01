package com.example.springjobboard.repository;

import com.example.springjobboard.model.jobs.JobCategory;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class JobCategoryRepository extends BasicRepositoryImpl<JobCategory, Integer> {

    public JobCategoryRepository() {
        setClazzForExtendedRepository(JobCategory.class);
    }
}
