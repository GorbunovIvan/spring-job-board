package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Applicant;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicantRepository extends BasicRepositoryImpl<Applicant, Long> {

    public ApplicantRepository() {
        setClazzForChildren(Applicant.class);
    }
}
