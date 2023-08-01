package com.example.springjobboard.util;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersUtil {

    private final UserRepository userRepository;

    public User getCurrentUser() {
//        return null;
        return userRepository.findByIdEagerlyDeeply(1L);
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
