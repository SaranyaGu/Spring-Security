package com.example.springcloud.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcloud.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

    // Additional query methods can be defined here if needed

    User findByEmail(String email);

}
