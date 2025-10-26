package com.example.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.quizapp.entity.QuizAttempt;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUserId(Long userId);
    List<QuizAttempt> findByQuizId(Long quizId);
    Optional<QuizAttempt> findByUserIdAndQuizIdAndCompletedFalse(Long userId, Long quizId);
    List<QuizAttempt> findByUserIdOrderByStartTimeDesc(Long userId);
}
