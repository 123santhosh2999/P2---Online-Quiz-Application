package com.example.quizapp.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.quizapp.entity.QuizAttempt;
import com.example.quizapp.entity.User;
import com.example.quizapp.service.QuizAttemptService;
import com.example.quizapp.service.UserService;


@Controller
@RequestMapping("/results")
public class ResultController {

    @Autowired
    private QuizAttemptService quizAttemptService;

    @Autowired
    private UserService userService;

    @GetMapping("/my-results")
    public String myResults(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        List<QuizAttempt> attempts = quizAttemptService.getUserAttempts(user);
        model.addAttribute("attempts", attempts);
        model.addAttribute("user", user);

        return "my-results";
    }
}