package com.example.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.quizapp.entity.QuizAttempt;
import com.example.quizapp.entity.User;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendQuizResultEmail(User user, QuizAttempt attempt) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Quiz Result - " + attempt.getQuiz().getTitle());

        double percentage = (double) attempt.getScoreObtained() / attempt.getTotalScore() * 100;

        String emailBody = String.format(
            "Hello %s,\n\n" +
            "You have completed the quiz: %s\n\n" +
            "Your Results:\n" +
            "Score: %d / %d\n" +
            "Percentage: %.2f%%\n\n" +
            "Quiz completed on: %s\n\n" +
            "Thank you for participating!\n\n" +
            "Best regards,\n" +
            "Quiz Application Team",
            user.getFirstName(),
            attempt.getQuiz().getTitle(),
            attempt.getScoreObtained(),
            attempt.getTotalScore(),
            percentage,
            attempt.getEndTime().toString()
        );

        message.setText(emailBody);
        mailSender.send(message);
    }

    public void sendWelcomeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to Quiz Application!");

        String emailBody = String.format(
            "Hello %s,\n\n" +
            "Welcome to Quiz Application!\n\n" +
            "Your account has been created successfully.\n" +
            "Username: %s\n\n" +
            "You can now login and start taking quizzes.\n\n" +
            "Happy learning!\n\n" +
            "Best regards,\n" +
            "Quiz Application Team",
            user.getFirstName(),
            user.getUsername()
        );

        message.setText(emailBody);
        mailSender.send(message);
    }
}
