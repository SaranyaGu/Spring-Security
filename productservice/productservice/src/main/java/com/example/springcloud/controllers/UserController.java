package com.example.springcloud.controllers;

//import java.util.HashSet;
//import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//import com.example.springcloud.model.Role;
//import com.example.springcloud.model.User;
import com.example.springcloud.repos.UserRepo;
import com.example.springcloud.security.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class UserController {

    @Autowired
    private SecurityService securityService;

    @GetMapping("/")
    public String showLoginPage() {
        System.out.println("Showing login page");
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Attempting login for user: " + email);
        boolean loginResponse = securityService.login(email, password, request, response);
        System.out.println("Login attempt for user: " + email);
        if (loginResponse) {
            return "index";
        }
        return "login";

    }
    

}
