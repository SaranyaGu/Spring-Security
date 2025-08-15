package com.example.springcloud.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.springcloud.model.Role;
import com.example.springcloud.model.User;
import com.example.springcloud.repos.UserRepo;
import com.example.springcloud.security.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class UserController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/showReg")
    public String showRegistrationPage() {
        return "registerUser";
    }
    
    @PostMapping("/registerUser")
    public String register(User user) {
        // Hash the user's password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(2L);
        roles.add(role);
        user.setRoles(roles);
        userRepo.save(user);
        return "login";
    } 

    @GetMapping("/")
    public String showLoginPage() {
        System.out.println("Showing login page");
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpServletRequest request, HttpServletResponse response) {
        boolean loginResponse = securityService.login(email, password, request, response);
        System.out.println("Login attempt for user: " + email);
        if (loginResponse) {
            return "index";
        }
        return "login";

    }
    
    

}
