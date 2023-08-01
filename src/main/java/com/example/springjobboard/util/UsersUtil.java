package com.example.springjobboard.util;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.basic.BasicRepository;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Component;

@Component
public class UsersUtil {

    private final BasicRepository<User, Long> userRepository;

    public UsersUtil(BasicRepositoryImpl<User, Long> userRepository) {
        this.userRepository = userRepository;
        this.userRepository.setClazz(User.class);
    }

    public User getCurrentUser() {
//        return null;
        return userRepository.findByIdEagerly(1L);
    }

    public Employer getCurrentEmployer() {
        var user = getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getEmployer();
    }

    public Applicant getCurrentApplicant() {
        var user = getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getApplicant();
    }
}
