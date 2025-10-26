package com.example.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.quizapp.entity.Answer;
import com.example.quizapp.entity.Question;
import com.example.quizapp.entity.Quiz;
import com.example.quizapp.entity.QuizAttempt;
import com.example.quizapp.entity.User;
import com.example.quizapp.entity.UserAnswer;
import com.example.quizapp.repository.AnswerRepository;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.repository.QuizAttemptRepository;
import com.example.quizapp.repository.UserAnswerRepository;

@Service
@Transactional
public class QuizAttemptService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EmailService emailService;

    public QuizAttempt startQuiz(User user, Quiz quiz) {
        // Check if user has an incomplete attempt
        Optional<QuizAttempt> existingAttempt = quizAttemptRepository
                .findByUserIdAndQuizIdAndCompletedFalse(user.getId(), quiz.getId());

        if (existingAttempt.isPresent()) {
            return existingAttempt.get();
        }

        QuizAttempt attempt = new QuizAttempt(user, quiz);
        return quizAttemptRepository.save(attempt);
    }

    public QuizAttempt submitQuiz(Long attemptId, Map<Long, Long> answers) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found"));

        if (attempt.getCompleted()) {
            throw new RuntimeException("Quiz already completed");
        }

        int score = 0;

        // Process each answer
        for (Map.Entry<Long, Long> entry : answers.entrySet()) {
            Long questionId = entry.getKey();
            Long selectedAnswerId = entry.getValue();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            Answer selectedAnswer = answerRepository.findById(selectedAnswerId)
                    .orElseThrow(() -> new RuntimeException("Answer not found"));

            UserAnswer userAnswer = new UserAnswer(attempt, question, selectedAnswer);
            
            if (selectedAnswer.getIsCorrect()) {
                score += question.getMarks();
                userAnswer.setIsCorrect(true);
            }

            userAnswerRepository.save(userAnswer);
        }

        // Update attempt
        attempt.setEndTime(LocalDateTime.now());
        attempt.setScoreObtained(score);
        attempt.setCompleted(true);

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

        // Send email notification
        try {
            emailService.sendQuizResultEmail(attempt.getUser(), attempt);
        } catch (Exception e) {
            // Log error but don't fail the quiz submission
            System.err.println("Failed to send email: " + e.getMessage());
        }

        return savedAttempt;
    }

    public List<QuizAttempt> getUserAttempts(User user) {
        return quizAttemptRepository.findByUserIdOrderByStartTimeDesc(user.getId());
    }

    public QuizAttempt getAttemptById(Long attemptId) {
        return quizAttemptRepository.findById(attemptId).orElse(null);
    }

    public List<UserAnswer> getAttemptAnswers(Long attemptId) {
        return userAnswerRepository.findByQuizAttemptId(attemptId);
    }
}