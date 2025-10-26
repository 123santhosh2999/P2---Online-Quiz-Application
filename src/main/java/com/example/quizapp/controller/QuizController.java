package com.example.quizapp.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quizapp.entity.Quiz;
import com.example.quizapp.entity.QuizAttempt;
import com.example.quizapp.entity.User;
import com.example.quizapp.entity.UserAnswer;
import com.example.quizapp.service.QuestionService;
import com.example.quizapp.service.QuizAttemptService;
import com.example.quizapp.service.QuizService;
import com.example.quizapp.service.UserService;


@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizAttemptService quizAttemptService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/list")
    public String listQuizzes(Model model) {
        List<Quiz> quizzes = quizService.getAllActiveQuizzes();
        model.addAttribute("quizzes", quizzes);
        return "quiz-list";
    }

    @GetMapping("/{id}/start")
    public String startQuiz(@PathVariable Long id,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        Optional<Quiz> quizOpt = quizService.getQuizById(id);
        if (!quizOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found");
            return "redirect:/quiz/list";
        }

        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        QuizAttempt attempt = quizAttemptService.startQuiz(user, quizOpt.get());
        return "redirect:/quiz/take/" + attempt.getId();
    }

    @GetMapping("/take/{attemptId}")
    public String takeQuiz(@PathVariable Long attemptId, Model model, Authentication authentication) {
        QuizAttempt attempt = quizAttemptService.getAttemptById(attemptId);
        
        if (attempt == null || attempt.getCompleted()) {
            return "redirect:/quiz/list";
        }

        // Verify user owns this attempt
        User currentUser = userService.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null || !attempt.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/quiz/list";
        }

        Quiz quiz = attempt.getQuiz();
        List<com.example.quizapp.entity.Question> questions = questionService.getQuestionsByQuizId(quiz.getId());

        model.addAttribute("attempt", attempt);
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);

        return "quiz-take";
    }

    @PostMapping("/submit/{attemptId}")
    public String submitQuiz(@PathVariable Long attemptId,
                            @RequestParam Map<String, String> answers,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        
        QuizAttempt attempt = quizAttemptService.getAttemptById(attemptId);
        if (attempt == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Quiz attempt not found");
            return "redirect:/quiz/list";
        }

        // Verify user owns this attempt
        User currentUser = userService.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null || !attempt.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/quiz/list";
        }

        // Convert answers map
        Map<Long, Long> processedAnswers = new java.util.HashMap<>();
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            if (entry.getKey().startsWith("question_")) {
                Long questionId = Long.valueOf(entry.getKey().substring(9));
                Long answerId = Long.valueOf(entry.getValue());
                processedAnswers.put(questionId, answerId);
            }
        }

        try {
            QuizAttempt completedAttempt = quizAttemptService.submitQuiz(attemptId, processedAnswers);
            return "redirect:/quiz/result/" + completedAttempt.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting quiz: " + e.getMessage());
            return "redirect:/quiz/take/" + attemptId;
        }
    }

    @GetMapping("/result/{attemptId}")
    public String viewResult(@PathVariable Long attemptId, Model model, Authentication authentication) {
        QuizAttempt attempt = quizAttemptService.getAttemptById(attemptId);
        
        if (attempt == null || !attempt.getCompleted()) {
            return "redirect:/quiz/list";
        }

        // Verify user owns this attempt
        User currentUser = userService.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null || !attempt.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/quiz/list";
        }

        List<UserAnswer> userAnswers = quizAttemptService.getAttemptAnswers(attemptId);
        double percentage = (double) attempt.getScoreObtained() / attempt.getTotalScore() * 100;

        model.addAttribute("attempt", attempt);
        model.addAttribute("userAnswers", userAnswers);
        model.addAttribute("percentage", percentage);

        return "quiz-result";
    }
}