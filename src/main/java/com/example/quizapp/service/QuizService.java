package com.example.quizapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quizapp.entity.Question;
import com.example.quizapp.entity.Quiz;
import com.example.quizapp.repository.QuizRepository;

@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public List<Quiz> getAllActiveQuizzes() {
        return quizRepository.findByActiveTrue();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public Quiz createQuiz(Quiz quiz) {
        // Calculate total marks
        int totalMarks = quiz.getQuestions().stream()
                .mapToInt(Question::getMarks)
                .sum();
        quiz.setTotalMarks(totalMarks);
        
        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Quiz quiz) {
        // Recalculate total marks
        int totalMarks = quiz.getQuestions().stream()
                .mapToInt(Question::getMarks)
                .sum();
        quiz.setTotalMarks(totalMarks);
        
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public List<Quiz> searchQuizzes(String searchTerm) {
        return quizRepository.findByTitleContainingIgnoreCase(searchTerm);
    }
}