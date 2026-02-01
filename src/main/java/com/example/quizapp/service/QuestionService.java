package com.example.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import com.example.quizapp.entity.Answer;
import com.example.quizapp.entity.Question;
import com.example.quizapp.repository.AnswerRepository;
import com.example.quizapp.repository.QuestionRepository;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

    public List<Answer> getAnswersByQuestionId(Long questionId) {

        if (questionRepository.existsById(questionId)) {
            return answerRepository.findByQuestionId(questionId);
        }

        return Collections.emptyList();
    }

    public Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }
}
