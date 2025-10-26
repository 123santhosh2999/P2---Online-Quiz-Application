package com.example.quizapp.entity;

import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answerText;
    
    @Column(name = "is_correct")
    private Boolean isCorrect = false;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    
    // Constructors, getters, and setters
    public Answer() {}
    
    public Answer(String answerText, Boolean isCorrect) {
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
}
