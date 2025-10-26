package com.example.quizapp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    private QuestionType questionType = QuestionType.MULTIPLE_CHOICE;
    
    private Integer marks = 1;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;
    
    // Constructors, getters, and setters
    public Question() {}
    
    public Question(String questionText, QuestionType questionType, Integer marks) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.marks = marks;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }
    
    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }
    
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    
    public List<Answer> getAnswers() { return answers; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }
}

enum QuestionType {
    MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
}
