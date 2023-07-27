package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Applicant;

public class ApplicantRepository extends BasicRepositoryImpl<Applicant, Long> {

    public ApplicantRepository() {
        super(Applicant.class);
    }
}
