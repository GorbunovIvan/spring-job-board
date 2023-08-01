package com.example.springjobboard.repository;

import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.basic.BasicRepositoryImpl;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BasicRepositoryImpl<User, Long> {

    public UserRepository() {
        setClazzForExtendedRepository(User.class);
    }
}
