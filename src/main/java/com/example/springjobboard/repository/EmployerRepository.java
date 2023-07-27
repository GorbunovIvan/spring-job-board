package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Employer;

public class EmployerRepository extends BasicRepositoryImpl<Employer, Long> {

    public EmployerRepository() {
        super(Employer.class);
    }
}
