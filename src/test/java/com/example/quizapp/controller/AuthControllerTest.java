package com.example.quizapp.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.quizapp.dto.UserRegistrationDto;
import com.example.quizapp.entity.User;
import com.example.quizapp.service.EmailService;
import com.example.quizapp.service.UserService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @Test
    void login_returnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void showRegistrationForm_setsUserModelAndReturnsRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void registerUser_success_redirectsToLogin() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("john");
        dto.setEmail("john@example.com");
        dto.setPassword("secret123");
        dto.setConfirmPassword("secret123");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        User saved = new User("john", "john@example.com", "encoded", "John", "Doe");
        saved.setId(1L);

        when(userService.registerUser(org.mockito.ArgumentMatchers.any(UserRegistrationDto.class))).thenReturn(saved);
        doNothing().when(emailService).sendWelcomeEmail(saved);

        mockMvc.perform(post("/register")
                .contentType("application/x-www-form-urlencoded")
                .param("username", dto.getUsername())
                .param("email", dto.getEmail())
                .param("password", dto.getPassword())
                .param("confirmPassword", dto.getConfirmPassword())
                .param("firstName", dto.getFirstName())
                .param("lastName", dto.getLastName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void registerUser_passwordMismatch_returnsRegisterView() throws Exception {
        mockMvc.perform(post("/register")
                .contentType("application/x-www-form-urlencoded")
                .param("username", "john")
                .param("email", "john@example.com")
                .param("password", "secret123")
                .param("confirmPassword", "different")
                .param("firstName", "John")
                .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }
}
