package com.example.quizapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.quizapp.entity.Question;
import com.example.quizapp.entity.Quiz;
import com.example.quizapp.repository.QuizRepository;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    @Test
    void createQuiz_calculatesTotalMarks() {
        Quiz quiz = new Quiz("T", "D", 10);
        Question q1 = new Question();
        q1.setMarks(2);
        Question q2 = new Question();
        q2.setMarks(3);
        quiz.setQuestions(List.of(q1, q2));

        when(quizRepository.save(quiz)).thenReturn(quiz);

        Quiz saved = quizService.createQuiz(quiz);
        assertThat(saved.getTotalMarks()).isEqualTo(5);
    }

    @Test
    void getAllActiveQuizzes_delegatesToRepository() {
        when(quizRepository.findByActiveTrue()).thenReturn(List.of(new Quiz("T", "D", 10)));
        assertThat(quizService.getAllActiveQuizzes()).hasSize(1);
    }
}
