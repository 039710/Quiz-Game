package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author Sascha Bauer, Ahmad Mustain Billah
 *
 */
@Entity
@Table(name = "QUESTION")
public class Question {
    @Column(name = "Question_Text")
    private String text;
    @Id
    private Integer questionId;
    @JoinColumn(name = "Question_CorrectAnswer")
    private Answer correctAnswer;

    // Relationship to Answer
    @OneToMany (targetEntity = Answer.class, mappedBy = "ques")
    public List<Answer> answers;

    //Relationship to Category
    @ManyToOne(fetch = FetchType.EAGER)
    private Category cat;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Gameinformation> questions = new ArrayList<>();

    public Question(String text, Integer questionId) {
        this.text = text;
        this.questionId = questionId;
    }

    public Question() {

        this.answers = new ArrayList<>();
    }

    public String getText(){

        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public Integer getQuestionId(){

        return questionId;
    }

    public void setQuestionId(Integer questionId){

        this.questionId = questionId;
    }

    public Answer getCorrectAnswer(){

        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer){

        this.correctAnswer = correctAnswer;
    }

    public Category getCategory(){
        return cat;
    }

    public void setCategory(Category category) {

        this.cat = category;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public int getCorrectAnswerID() {
        return correctAnswer.getAnswerId();
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Question question = (Question) o;
        return Objects.equals(getQuestionId(), question.getQuestionId()) &&
                Objects.equals(getCategory(), question.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestionId(), getCategory());
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", questionId=" + questionId +
                ", correctAnswer=" + correctAnswer +
                ", answers=" + answers.toString() +
                ", category=" + cat +
                '}';
    }
}
