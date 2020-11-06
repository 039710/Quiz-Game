package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.*;


/**
 *
 * @author Sascha Bauer, Ahmad Mustain Billah
 *
 */
@Entity
@Table(name = "ANSWER")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Answer_ID")
    private int answerId;

    @Column(name = "Answer_Text")
    private String text;

    @Column(name = "ANS_CORRECT")
    private boolean ansCorrect = false;

    //Relationship to Question
    @ManyToOne(fetch = FetchType.EAGER)
    private Question ques;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Gameinformation> answers = new ArrayList<>();


    public String getText() {

        return text;
    }
    public void setText(String text) {

        this.text = text;
    }
    public Integer getAnswerId() {

        return answerId;
    }

    public Question getQues() {

        return ques;
    }

    public void setQues(Question ques) {

        this.ques = ques;
    }

    public Answer() {
    }

    public Answer(String text, boolean ansCorrect) {
        this.text = text;
        this.ansCorrect = ansCorrect;
    }

    public boolean isAnsCorrect() {
        return ansCorrect;
    }

    public void setAnsCorrect(boolean ansCorrect) {
        this.ansCorrect = ansCorrect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Answer answer = (Answer) o;
        return Objects.equals(getAnswerId(), answer.getAnswerId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getAnswerId());
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answerId=" + answerId +
                ", text='" + text + '\'' +
                '}';
    }
}
