package com.example.quizapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quizapp.config.EmailService;
import com.example.quizapp.dto.UserRegistrationDto;
import com.example.quizapp.entity.User;
import com.example.quizapp.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto registrationDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "register";
        }

        try {
            User user = userService.registerUser(registrationDto);
            
            // Send welcome email
            try {
                emailService.sendWelcomeEmail(user);
            } catch (Exception e) {
                System.err.println("Failed to send welcome email: " + e.getMessage());
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            bindingResult.rejectValue("username", "error.user", e.getMessage());
            return "register";
        }
    }
}