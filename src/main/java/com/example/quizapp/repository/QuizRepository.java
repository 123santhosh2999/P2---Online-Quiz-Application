package com.example.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.quizapp.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByActiveTrue();
    List<Quiz> findByTitleContainingIgnoreCase(String title);
}