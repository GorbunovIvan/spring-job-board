package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class EmployerRepository extends BasicRepositoryImpl<Employer, Long> {

    public EmployerRepository() {
        setClazzForExtendedRepository(Employer.class);
    }

    @Override
    @Transactional
    public Set<Employer> findAll() {
        return getEntityManager().createQuery("FROM " + getClassName() + " employers " +
                        "LEFT JOIN FETCH employers.vacancies vacancies " +
                        "LEFT JOIN FETCH employers.user users " +
                        "LEFT JOIN FETCH users.applicant " +
                        "LEFT JOIN FETCH users.employer " +
                        "LEFT JOIN FETCH users.roles", getClazz())
                .getResultStream()
                .collect(Collectors.toSet());
    }
}
