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

    @Transactional
    public Set<Vacancy> findAllForApplicantBySkills(Applicant currentApplicant) {

        if (currentApplicant.getSkills().isEmpty()) {
            return new HashSet<>();
        }

        return getEntityManager().createQuery("FROM Vacancy v " +
                        "JOIN v.skills skills " +
                        "WHERE NOT v.employer = :employer " +
                        "AND skills IN :skills", Vacancy.class)
                .setParameter("employer", currentApplicant.getUser().getEmployer())
                .setParameter("skills", currentApplicant.getSkills())
                .getResultStream()
                .collect(Collectors.toSet());
    }
}
