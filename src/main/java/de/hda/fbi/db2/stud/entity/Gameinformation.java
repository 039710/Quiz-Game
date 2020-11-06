package de.hda.fbi.db2.stud.entity;
import java.sql.Timestamp;
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
@Table(name = "GAMEINFO")
public class Gameinformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "GAME_ID")
    private int gameId;

    @Column(name = "TIME_START")
    private Timestamp timeStart;

    @Column(name = "TIME_END")
    private Timestamp timeEnd;

    @Column(name = "SCORE")
    private int score;

    @Column(name = "Question_AMOUNT")
    private int questionAmount;

    //Relationship to Player
    @ManyToOne(fetch = FetchType.EAGER)
    private Player players;

    @ManyToMany
    private List<Category> categories = new ArrayList<>();

    @ManyToMany(targetEntity = Question.class, mappedBy = "questions")
    private List<Question> questions = new ArrayList<>();

    @ManyToMany(targetEntity = Answer.class, mappedBy = "answers")
    private List<Answer> answers = new ArrayList<>();


    public Gameinformation(Timestamp timeStart, Timestamp timeEnd, int score, int questionAmount) {
        this.timeStart =  new Timestamp(timeStart.getTime());
        this.timeEnd =  new Timestamp(timeEnd.getTime());
        this.score = score;
        this.questionAmount = questionAmount;
    }

    public Gameinformation() {
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Player getPlayers() {
        return players;
    }

    public void setPlayers(Player players) {
        this.players = players;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Timestamp getTimeStart() {
        return new Timestamp(timeStart.getTime());
    }

    public Timestamp getTimeEnd() {
       return new Timestamp(timeEnd.getTime());
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = new Timestamp(timeStart.getTime());
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = new Timestamp (timeEnd.getTime());
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQuestionAmount() {
        return questionAmount;
    }

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Gameinformation that = (Gameinformation) o;
        return gameId == that.gameId &&
                Objects.equals(timeStart, that.timeStart) &&
                Objects.equals(timeEnd, that.timeEnd) &&
                Objects.equals(players, that.players) &&
                Objects.equals(categories, that.categories) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, timeStart, timeEnd, players, categories, questions, answers);
    }

    @Override
    public String toString() {
        return "Gameinformation{" +
                "gameId=" + gameId +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", players=" + players.getPlayerName() +
                '}';
    }


}
