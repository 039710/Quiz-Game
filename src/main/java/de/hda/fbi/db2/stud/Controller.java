package de.hda.fbi.db2.stud;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import de.hda.fbi.db2.stud.entity.*;
import de.hda.fbi.db2.tools.CsvDataReader;









/**
 *
 * @author Sascha Bauer, Ahmad Mustain Billah
 *
 */
public class Controller {

    private Question question;
    private ArrayList<Question> questions;
    private List<Answer> answers;
    private List<Category> categories;

    // Create the Entity Manager
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    public Controller() {
        answers = null;
        questions = new ArrayList<Question>();
        categories = new ArrayList<>();
        entityManagerFactory = null;
        entityManager = null;
        entityTransaction = null;

    }

    public void readCSV() {

        try {
            //Read default csv
            final List<String[]> defaultCsvLines = CsvDataReader.read();

            //Read (if available) additional csv-files and default csv-file
            List<String> availableFiles = CsvDataReader.getAvailableFiles();
            for (String availableFile : availableFiles) {
                final List<String[]> additionalCsvLines = CsvDataReader.read(availableFile);
            }

            //Erste Zeile entfernen
            defaultCsvLines.remove(0);

            //Reading from CSV into a TreeSet to avoid duplicates and to sort Category names
            Set<String> categorySet = new TreeSet<>();
            for (String[] csv : defaultCsvLines) {
                String name = csv[7];

                categorySet.add(name);
            }
            //Converts Set of Categories (String) to an Array (String)
            String[] categoriesNames = categorySet.toArray(new String[categorySet.size()]);

            //Converts Array of Categories(String) to ArrayList (Category)

            for (String catName : categoriesNames) {
                categories.add(new Category(catName));
            }

            for (String[] csv : defaultCsvLines) {

                //Reads ID
                String id = csv[0];

                //Reads current Category
                String currentCat = csv[7];


                //convert id to int
                int intId = Integer.parseInt(id);
                //Reads _frage
                String text = csv[1];


                //Reads Answers 1 to 4 and correct answers
                answers = new ArrayList<>();
                Answer answer;

                String corAnswer = csv[6];

                //Set Answer 1
                answer = new Answer();
                answer.setText(csv[2]);
                answers.add(answer);

                //Set Answer 2
                answer = new Answer();
                answer.setText(csv[3]);
                answers.add(answer);

                //Set Answer 3
                answer = new Answer();
                answer.setText(csv[4]);
                answers.add(answer);

                //Set Answer 4
                answer = new Answer();
                answer.setText(csv[5]);
                answers.add(answer);

                // Build Question Object
                question = new Question();
                question.setQuestionId(intId);
                question.setAnswers(answers);

                //convert correctAnswer to int
                int intCorrectAnswer = Integer.parseInt(corAnswer);

                //Set correct Answer
                question.setCorrectAnswer(answers.get(intCorrectAnswer - 1));

                // Set Text
                question.setText(text);

                // Set Questionobject in all the Answer Objedts related to the Question
                for (Answer answertmp : answers) {

                    answertmp.setQues(question);
                }

                // Set Category in Question related to the Category and the other way
                for (Category cat2 : categories) {

                    if (cat2.getCategoryName().equalsIgnoreCase(currentCat)) {
                        question.setCategory(cat2);
                        cat2.addQuestion(question);
                    }
                }

                //set correct Answer
                for (Category cat : categories) {
                    System.out.println(" Category : " + cat.getCategoryName());
                    for (Question question1 : cat.getQuestions()) {
                        for (Answer ans : question1.getAnswers()) {
                            if (ans.getText().equalsIgnoreCase(
                                    question1.getCorrectAnswer().getText())) {
                                question1.getCorrectAnswer().setAnsCorrect(true);

                            }
                        }
                    }

                }
                //Add Question to collection
                questions.add(question);

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void printCSVdata() {
        //Print Questions
        for (Question temp : questions) {

            System.out.println(temp.toString());
        }

        //Print ammount of Categories
        System.out.println("Gesamtzahl Kategorien: " + categories.size());
        //Print ammount of Questions
        System.out.println("Gesamtzahl Fragen: " + questions.size());

    }

    public void persistCSV() {

                // Begin Transaction
                try {
                    entityManagerFactory = Persistence.createEntityManagerFactory(
                            "defaultPU");
                    entityManager = entityManagerFactory.createEntityManager();
                    entityTransaction = entityManager.getTransaction();
                    entityTransaction.begin();
                    for (Question question : questions) {
                        for (Answer ans : question.getAnswers()) {
                            //save all answers
                            entityManager.persist(ans);
                        }
                        //save all category
                        for (Category catTmp : categories) {
                            entityManager.persist(catTmp);
                        }
                        //save all questions
                        entityManager.persist(question);


                    }


                    entityTransaction.commit();

                } catch (RuntimeException e) {
                    if (entityTransaction != null && entityTransaction.isActive()){
                        entityTransaction.rollback();
                        // Rollback Transaction ---------------- [TX]
                    }
                    throw e;
                } finally {
                    if (entityManager != null){
                        entityManager.close();
                        // Close Session *********************** [EM]
                    }
            }
        // Disconnect
        entityManagerFactory.close();




    }

    public boolean isDataAlreadyExist(){
            //check if data already in database
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultMAIN");
            EntityManager em = emf.createEntityManager();

            try {
                List resultCategory = em.createQuery("select c from Category c").getResultList();
                List resultQuestion = em.createQuery("select q from Question q").getResultList();
                List resultAnswer = em.createQuery("select a from Answer a").getResultList();

                if (resultCategory.isEmpty() && resultAnswer.isEmpty()
                        && resultQuestion.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e){
                return false;
            }

        }


    public boolean isDataAlreadyExistPlayer(){
        //check if data already in database
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultMAIN");
        EntityManager em = emf.createEntityManager();

        try {
            List resultPlayer = em.createQuery("select a from Player a").getResultList();
            if (resultPlayer.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e){
            return false;
        }

    }


    public ArrayList<Gameinformation> generateGameinformation (int ammount, Player player){

        Random rn = new Random();
        int range = 100 - 0 + 1;
        ArrayList<Gameinformation> tmp = new ArrayList<Gameinformation>();

        for (int i = 0; i < ammount; i++) {

            Gameinformation gameinformation;

            Timestamp timestampStart = new Timestamp(System.currentTimeMillis());


            Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());

            int questions =  rn.nextInt(range) + 1;
            int score = rn.nextInt(questions) + 1;

            gameinformation = new Gameinformation(timestampStart, timestampEnd, score, questions);
            gameinformation.setPlayers(player);
            tmp.add(gameinformation);
        }
        return tmp;
    }

    // This function can only be used if the Table Player is cleared before persisting.
    // Otherwise there can be Problems with the unique Key
    public ArrayList<Player> generatePlayersWithEach100Games (int ammount) {

        ArrayList<Player> tmpPlayer = new ArrayList<Player>();

        for (int i = 0; i < ammount; i++) {
            Player player = new Player();
            player.setPlayerName("player" + i);
            tmpPlayer.add(player);
        }

        for (Player tmp : tmpPlayer){

                ArrayList<Gameinformation>tmp3 = generateGameinformation(100, tmp);
                tmp.setGameInfo(tmp3);
            }
        return tmpPlayer;
    }
    // This function can only be used if the Table Player is cleared before persisting.
    // Otherwise there can be Problems with the unique Key
    public void persistGeneratedPlayers(ArrayList<Player> players) {
        // Begin Transaction
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory(
                    "defaultMAIN");
            entityManager = entityManagerFactory.createEntityManager();
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            for (Player tmp : players){
                for (Gameinformation gameinfo : tmp.gameInfo){
                    entityManager.persist(gameinfo);
                }
                entityManager.persist(tmp);
                entityManager.flush();
                entityManager.clear();
            }

            entityTransaction.commit();

        } catch (RuntimeException e) {
            if (entityTransaction != null && entityTransaction.isActive()){
                entityTransaction.rollback();
                // Rollback Transaction ---------------- [TX]
            }
            throw e;
        } finally {
            if (entityManager != null){
                entityManager.close();
                // Close Session *********************** [EM]
            }
        }
        // Disconnect
        entityManagerFactory.close();




    }








}
