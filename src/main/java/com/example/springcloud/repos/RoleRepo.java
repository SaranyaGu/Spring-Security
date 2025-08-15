package com.example.springcloud.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcloud.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

    // Additional query methods can be defined here if needed

}
