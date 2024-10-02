package com.defectus.smartrecruiter.web;

import com.defectus.smartrecruiter.dao.entities.User;
import com.defectus.smartrecruiter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "Registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, @ModelAttribute("role") String role) {
        userService.registerNewUser(user, role);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "Login";
    }
}

