package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ApplicantRepository extends BasicRepositoryImpl<Applicant, Long> {

    public ApplicantRepository() {
        setClazzForExtendedRepository(Applicant.class);
    }

    @Override
    @Transactional
    public Set<Applicant> findAll() {
        return getEntityManager().createQuery("FROM " + getClassName() + " a " +
                                "LEFT JOIN FETCH a.user u " +
                                "LEFT JOIN FETCH u.applicant " +
                                "LEFT JOIN FETCH u.employer " +
                                "LEFT JOIN FETCH u.roles", getClazz())
                .getResultStream()
                .collect(Collectors.toSet());
    }
}
