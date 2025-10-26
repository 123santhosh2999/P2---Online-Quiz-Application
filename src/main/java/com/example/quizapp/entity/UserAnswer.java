
package com.example.quizapp.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_answers")
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id")
    private QuizAttempt quizAttempt;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    
    @ManyToOne
    @JoinColumn(name = "selected_answer_id")
    private Answer selectedAnswer;
    
    @Column(name = "answer_text")
    private String answerText; // for text-based answers
    
    @Column(name = "is_correct")
    private Boolean isCorrect = false;
    
    // Constructors, getters, and setters
    public UserAnswer() {}
    
    public UserAnswer(QuizAttempt quizAttempt, Question question, Answer selectedAnswer) {
        this.quizAttempt = quizAttempt;
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = selectedAnswer.getIsCorrect();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public QuizAttempt getQuizAttempt() { return quizAttempt; }
    public void setQuizAttempt(QuizAttempt quizAttempt) { this.quizAttempt = quizAttempt; }
    
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    
    public Answer getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(Answer selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}
