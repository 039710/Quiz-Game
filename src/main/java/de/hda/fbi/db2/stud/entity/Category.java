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
@Table(name = "CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "Category_Name")
    private String categoryName;

    //Relationship to Question
    @OneToMany(targetEntity = Question.class, mappedBy = "cat")
    private List<Question> questions;

    @ManyToMany(mappedBy = "categories")
    private List<Gameinformation> gameInfo = new ArrayList<>();

    public Category() {

        this.categoryName = "";
        this.questions = new ArrayList<>();
    }

    public Category (String name){

        setCategoryName(name);
        this.questions = new ArrayList<>();

    }

    public String getCategoryName() {

        return categoryName;
    }

    public void setCategoryName(String categoryName) {

        this.categoryName = categoryName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Question> getAnswers() {

        return questions;
    }

    public void setQuestions(List<Question> questions) {

        this.questions = questions;
    }

    public void addQuestion(Question ques){

        questions.add(ques);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Category category = (Category) o;
        return Objects.equals(getCategoryName(), category.getCategoryName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCategoryName());
    }

    @Override
    public String toString() {
        return "Category{" +
                ", categoryName='" + categoryName + '}';
    }
}
