package com.example.quizapp.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    
    @Column(name = "start_time")
    private LocalDateTime startTime = LocalDateTime.now();
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "score_obtained")
    private Integer scoreObtained = 0;
    
    @Column(name = "total_score")
    private Integer totalScore;
    
    private Boolean completed = false;
    
    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<UserAnswer> userAnswers;
    
    // Constructors, getters, and setters
    public QuizAttempt() {}
    
    public QuizAttempt(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
        this.totalScore = quiz.getTotalMarks();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public Integer getScoreObtained() { return scoreObtained; }
    public void setScoreObtained(Integer scoreObtained) { this.scoreObtained = scoreObtained; }
    
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    
    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    
    public List<UserAnswer> getUserAnswers() { return userAnswers; }
    public void setUserAnswers(List<UserAnswer> userAnswers) { this.userAnswers = userAnswers; }
}