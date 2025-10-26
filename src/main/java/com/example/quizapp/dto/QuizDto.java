package com.example.quizapp.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class QuizDto {
    
    @NotEmpty(message = "Quiz title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Time limit is required")
    @Positive(message = "Time limit must be positive")
    private Integer timeLimit;
    
    private Boolean active = true;
    
    // Constructors
    public QuizDto() {}
    
    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getTimeLimit() { return timeLimit; }
    public void setTimeLimit(Integer timeLimit) { this.timeLimit = timeLimit; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
