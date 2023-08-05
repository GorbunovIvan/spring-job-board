package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository extends BasicRepositoryImpl<User, Long> {

    public UserRepository() {
        setClazzForExtendedRepository(User.class);
    }

    @Transactional
    public User findByIdEagerly(Long id) {
        return getEntityManager().createQuery("FROM User user " +
                        "LEFT JOIN FETCH user.applicant applicant " +
                        "LEFT JOIN FETCH user.employer employer " +
                        "LEFT JOIN FETCH applicant.skills " +
                        "LEFT JOIN FETCH employer.vacancies " +
                        "WHERE user.id = :id", User.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findAny()
                .orElse(null);
    }

    @Transactional
    public User findByEmailEagerly(String email) {
        return getEntityManager().createQuery("FROM User user " +
                        "LEFT JOIN FETCH user.applicant applicant " +
                        "LEFT JOIN FETCH user.employer employer " +
                        "LEFT JOIN FETCH applicant.skills " +
                        "LEFT JOIN FETCH employer.vacancies " +
                        "WHERE user.email = :email", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findAny()
                .orElse(null);
    }
}
