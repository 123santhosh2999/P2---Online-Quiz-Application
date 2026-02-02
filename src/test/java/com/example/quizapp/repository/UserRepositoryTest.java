package com.example.quizapp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.quizapp.entity.User;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindByUsernameAndEmail() {
        User u = new User("john", "john@example.com", "p", "John", "Doe");
        userRepository.save(u);

        assertThat(userRepository.findByUsername("john")).isPresent();
        assertThat(userRepository.findByEmail("john@example.com")).isPresent();
        assertThat(userRepository.existsByUsername("john")).isTrue();
        assertThat(userRepository.existsByEmail("john@example.com")).isTrue();
    }
}
