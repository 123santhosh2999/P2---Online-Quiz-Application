package com.example.quizapp.repository;

import com.example.quizapp.entity.Answer;
import com.example.quizapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByUser(User user);

}
