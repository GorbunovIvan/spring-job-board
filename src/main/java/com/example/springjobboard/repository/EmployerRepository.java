package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Employer;
import org.springframework.stereotype.Repository;

@Repository
public class EmployerRepository extends BasicRepositoryImpl<Employer, Long> {

    public EmployerRepository() {
        setClazzForChildren(Employer.class);
    }
}
