package com.example.springjobboard.repository;

import com.example.springjobboard.model.jobs.Vacancy;
import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class VacancyRepository extends BasicRepositoryImpl<Vacancy, Long> {

    public VacancyRepository() {
        setClazzForExtendedRepository(Vacancy.class);
    }

    @Override
    @Transactional
    public Set<Vacancy> findAll() {
        return getEntityManager().createQuery("FROM " + getClassName() + " vacancies " +
                "LEFT JOIN FETCH vacancies.employer employers " +
                "LEFT JOIN FETCH employers.user users " +
                "LEFT JOIN FETCH users.applicant " +
                "LEFT JOIN FETCH users.employer " +
                "LEFT JOIN FETCH users.roles", getClazz())
                .getResultStream()
                .collect(Collectors.toSet());
    }

    @Transactional
    public Set<Vacancy> findAllForApplicantBySkills(Applicant currentApplicant) {

        if (currentApplicant.getSkills().isEmpty()) {
            return new HashSet<>();
        }

        return new HashSet<>(getEntityManager().createQuery("FROM Vacancy vacancies " +
                        "JOIN vacancies.skills skills " +
                        "LEFT JOIN FETCH vacancies.employer employers " +
                        "LEFT JOIN FETCH employers.user users " +
                        "LEFT JOIN FETCH users.applicant " +
                        "LEFT JOIN FETCH users.employer " +
                        "LEFT JOIN FETCH users.roles " +
                        "WHERE NOT vacancies.employer = :employer " +
                        "AND skills IN :skills", Vacancy.class)
                .setParameter("employer", currentApplicant.getUser().getEmployer())
                .setParameter("skills", currentApplicant.getSkills())
                .getResultList());
    }
}
