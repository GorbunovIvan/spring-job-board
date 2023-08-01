package com.example.springjobboard.repository;

import com.example.springjobboard.model.jobs.Vacancy;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class VacancyRepository extends BasicRepositoryImpl<Vacancy, Long> {

    public VacancyRepository() {
        setClazzForExtendedRepository(Vacancy.class);
    }
}
