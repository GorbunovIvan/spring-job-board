package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class EmployerRepository extends BasicRepositoryImpl<Employer, Long> {

    public EmployerRepository() {
        setClazzForExtendedRepository(Employer.class);
    }
}
