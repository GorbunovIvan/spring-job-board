package com.example.springjobboard.repository;

import com.example.springjobboard.model.jobs.Vacancy;
import org.springframework.stereotype.Repository;

@Repository
public class VacancyRepository extends BasicRepositoryImpl<Vacancy, Long> {

    public VacancyRepository() {
        setClazzForChildren(Vacancy.class);
    }
}
