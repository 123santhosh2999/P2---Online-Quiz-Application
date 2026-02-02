package com.example.quizapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.quizapp.entity.Quiz;

@DataJpaTest
@ActiveProfiles("test")
class QuizRepositoryTest {

    @Autowired
    private QuizRepository quizRepository;

    @Test
    void findByActiveTrue_and_searchByTitle() {
        Quiz q1 = new Quiz("Java Basics", "D", 10);
        q1.setActive(true);
        quizRepository.save(q1);

        Quiz q2 = new Quiz("Spring Boot", "D", 10);
        q2.setActive(false);
        quizRepository.save(q2);

        List<Quiz> active = quizRepository.findByActiveTrue();
        assertThat(active).isNotEmpty();

        List<Quiz> search = quizRepository.findByTitleContainingIgnoreCase("java");
        assertThat(search).isNotEmpty();
    }
}
