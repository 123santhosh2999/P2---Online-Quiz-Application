package com.example.quizapp.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.quizapp.entity.UserAnswer;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    @EntityGraph(attributePaths = {"question", "question.answers", "selectedAnswer"})
    List<UserAnswer> findByQuizAttemptId(Long quizAttemptId);
}
