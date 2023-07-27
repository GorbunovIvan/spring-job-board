package com.example.springjobboard.repository;

import com.example.springjobboard.model.jobs.Vacancy;

public class VacancyRepository extends BasicRepositoryImpl<Vacancy, Long> {

    public VacancyRepository() {
        super(Vacancy.class);
    }
}
