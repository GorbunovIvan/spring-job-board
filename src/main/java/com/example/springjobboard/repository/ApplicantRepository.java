package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicantRepository extends BasicRepositoryImpl<Applicant, Long> {

    public ApplicantRepository() {
        setClazzForExtendedRepository(Applicant.class);
    }
}
