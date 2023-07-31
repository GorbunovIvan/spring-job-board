package com.example.springjobboard.utils;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.BasicRepository;
import com.example.springjobboard.repository.BasicRepositoryImpl;
import org.springframework.stereotype.Component;

@Component
public class UsersUtil {

    private final BasicRepository<User, Long> userRepository;

    public UsersUtil(BasicRepositoryImpl<User, Long> userRepository) {
        this.userRepository = userRepository;
        this.userRepository.setClazz(User.class);
    }

    public User getCurrentUser() {
        return userRepository.findByIdEagerly(1L);
    }

    public Employer getCurrentEmployer() {
        var user = getCurrentUser();
        return user.getEmployer();
    }

    public Applicant getCurrentApplicant() {
        var user = getCurrentUser();
        return user.getApplicant();
    }
}
