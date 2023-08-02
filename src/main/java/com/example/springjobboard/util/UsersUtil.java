package com.example.springjobboard.util;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersUtil {

    private final UserRepository userRepository;

    public User getCurrentUser() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        String username = principal.toString();
        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }

        if (username.equals("anonymousUser")) {
            return null;
        }

        return userRepository.findByEmailEagerly(username);
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
