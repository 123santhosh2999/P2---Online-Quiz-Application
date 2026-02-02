package com.example.quizapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.quizapp.dto.UserRegistrationDto;
import com.example.quizapp.entity.User;
import com.example.quizapp.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_whenUsernameExists_throws() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("john");
        dto.setEmail("john@example.com");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void registerUser_success_savesUserWithEncodedPassword() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("john");
        dto.setEmail("john@example.com");
        dto.setPassword("secret123");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded");

        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        User saved = userService.registerUser(dto);
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getPassword()).isEqualTo("encoded");
        assertThat(saved.getUsername()).isEqualTo("john");
    }

    @Test
    void findByUsername_delegatesToRepository() {
        User u = new User("john", "john@example.com", "p", "John", "Doe");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(u));
        assertThat(userService.findByUsername("john")).isPresent();
    }
}
