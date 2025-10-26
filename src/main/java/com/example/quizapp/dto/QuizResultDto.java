package com.example.quizapp.dto;

import java.time.LocalDateTime;

public class QuizResultDto {
    private Long attemptId;
    private String quizTitle;
    private Integer scoreObtained;
    private Integer totalScore;
    private Double percentage;
    private LocalDateTime completedAt;
    private Long timeTaken; // in minutes
    
    // Constructors
    public QuizResultDto() {}
    
    public QuizResultDto(Long attemptId, String quizTitle, Integer scoreObtained, 
                        Integer totalScore, LocalDateTime completedAt, Long timeTaken) {
        this.attemptId = attemptId;
        this.quizTitle = quizTitle;
        this.scoreObtained = scoreObtained;
        this.totalScore = totalScore;
        this.percentage = (double) scoreObtained / totalScore * 100;
        this.completedAt = completedAt;
        this.timeTaken = timeTaken;
    }
    
    // Getters and setters
    public Long getAttemptId() { return attemptId; }
    public void setAttemptId(Long attemptId) { this.attemptId = attemptId; }
    
    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }
    
    public Integer getScoreObtained() { return scoreObtained; }
    public void setScoreObtained(Integer scoreObtained) { this.scoreObtained = scoreObtained; }
    
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public Long getTimeTaken() { return timeTaken; }
    public void setTimeTaken(Long timeTaken) { this.timeTaken = timeTaken; }
}