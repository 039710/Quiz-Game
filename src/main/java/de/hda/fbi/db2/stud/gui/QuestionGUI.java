package de.hda.fbi.db2.stud.gui;



import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import javax.swing.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.hda.fbi.db2.stud.entity.Answer;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Gameinformation;
import de.hda.fbi.db2.stud.entity.Question;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Sascha Bauer, Ahmad Mustain Billah
 */


@SuppressFBWarnings({"UWF_NULL_FIELD", "NP_UNWRITTEN_FIELD", "UR_UNINIT_READ"
        , "UWF_UNWRITTEN_FIELD"})

public class QuestionGUI extends JFrame {
    private JTextPane questionTextPane;
    private JRadioButton answer1RadioButton;
    private JRadioButton answer2RadioButton;
    private JRadioButton answer3RadioButton;
    private JRadioButton answer4RadioButton;
    private JButton weiterButton;
    private JPanel panelQuestion;
    private JLabel kategorieFrageLabel;
    private ButtonGroup answerButtonGroup;

    private CategoryGUI catGUI = CategoryGUI.getInstance();
    private MainMenu mainMenu = MainMenu.getMainmenu();
    private static QuestionGUI instance = null;
    List<Gameinformation> informations = new ArrayList<>();
    private int maxQuestion = catGUI.getMaxQuestion();
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultMAIN");
    private EntityManager em = emf.createEntityManager();

    ArrayList<String> chosenAnswers = new ArrayList<>();

    ArrayList<String> randomQuestions = new ArrayList<>();
    ArrayList<Integer> randomQuestionsId = new ArrayList<>();
    ArrayList<String> randomQuestionsCat = new ArrayList<>();

    List<Category> selectedCategoriesName = new ArrayList<>();
    List<Category> selectedCategoriesEntity = new ArrayList<>();
    List<Question> questionsForDB = new ArrayList<>();
    List<Answer> answersForDB = new ArrayList<>();

    int indexQuestion = 0;
    int score = 0;

    public QuestionGUI() {
        super("Frage");
        setContentPane(panelQuestion);
        pack();
        setVisible(true);
        getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        randomizeQuestion();
        nextQuestion();
        weiterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (answerButtonGroup.getSelection() == null) {
                    JOptionPane.showMessageDialog(
                            null, "Bitte wählen Sie eine Antwort aus",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    getMainFrame().dispose();
                    CorrectAnswerGUI correctAnswerGUI = CorrectAnswerGUI.getCorrectAnswerGUI();

                    correctAnswerGUI.setVisible(true);


                    correctAnswerGUI.getKategorieFrageKorrektLabel().setText(
                            "Kategorie: " + randomQuestionsCat.get(indexQuestion));
                    correctAnswerGUI.getIdFrageKorrektLabel().setText(
                            "ID: " + randomQuestionsId.get(indexQuestion));
                    correctAnswerGUI.getRichtigTextPane().setText(
                            randomQuestions.get(indexQuestion));

                    correctAnswerGUI.getPlayerAnswerTextPane().setText(
                            answerButtonGroup.getSelection().getActionCommand());
                    correctAnswerGUI.getCorrectAnswerTextPane().setText(
                            randomQuestions.get(indexQuestion));

                    chosenAnswers.add(answerButtonGroup.getSelection().getActionCommand());

                    List checkAnswer = em.createQuery(
                            "select a, q from Answer a join a.ques q where " +
                                    "q.questionId = :queID and a.text = :text")
                            .setParameter("queID", randomQuestionsId.get(indexQuestion))
                            .setParameter("text",
                                    answerButtonGroup.getSelection().getActionCommand())
                            .getResultList();
                    for (Iterator j = checkAnswer.iterator(); j.hasNext(); ) {
                        Object[] element = (Object[]) j.next();

                        if (((Answer) element[0]).isAnsCorrect()) {
                            correctAnswerGUI.getRightWrongLabel().setText("Richtig!");
                            score += 1;
                        } else {
                            correctAnswerGUI.getRightWrongLabel().setText("Falsch!");
                        }
                    }
                    List correctAnswer = em.createQuery(
                            "select a, q from Answer a join a.ques q where " +
                                    "q.questionId = :quesID and a.ansCorrect = 1")
                            .setParameter("quesID",
                                    randomQuestionsId.get(indexQuestion)).getResultList();
                    for (Iterator j = correctAnswer.iterator(); j.hasNext(); ) {
                        Object[] element = (Object[]) j.next();
                        correctAnswerGUI.getCorrectAnswerTextPane().setText(
                                ((Answer) element[0]).getText());
                    }
                    indexQuestion = indexQuestion + 1;
                }
            }
        });
    }

    private void randomizeQuestion() {
        Random random = new Random();
        List<Category> catArray = selectedCategoriesAsArrayList();
        for (int i = 0; i < catArray.size(); i++) {
            List resultX = em.createQuery("select q, c from Question q join q.cat c " +
                    "where c.categoryName = :categoryName ")
                    .setParameter("categoryName",
                            catArray.get(i).getCategoryName())
                    .getResultList();

            List resultL = em.createQuery("select q, c from Question q join q.cat c " +
                    "where c.categoryName = :categoryName")
                    .setParameter("categoryName",
                            catArray.get(i).getCategoryName())
                    .setFirstResult(random.nextInt(resultX.size()))
                    .setMaxResults(maxQuestion).getResultList();
            for (Iterator j = resultL.iterator(); j.hasNext(); ) {
                Object[] element = (Object[]) j.next();
                randomQuestions.add(((Question) element[0]).getText());
                randomQuestionsId.add(((Question) element[0]).getQuestionId());
                randomQuestionsCat.add(((Category) element[1]).getCategoryName());
            }
        }
    }


    public void nextQuestion() {
        if ((indexQuestion + 1) > randomQuestions.size()) {
            showResult();
            addGameInfoToDB();
        } else {
            kategorieFrageLabel.setText("Kategorie: " + randomQuestionsCat.get(indexQuestion));
            //idFrage_Label.setText("ID: " + randomQuestionsId.get(indexQuestion).toString());
            questionTextPane.setText(randomQuestions.get(indexQuestion));
            setAnswersFromRandomQuestions();
        }
    }

    private void setAnswersFromRandomQuestions() {
        ArrayList<String> answersFromRandomQuestions = new ArrayList<>();
        //select a, q from Answer a join a.que q where q.queId = :quesID
        List resultL = em.createQuery("select a, q from Answer a join " +
                "a.ques q where q.questionId = :quesID")
                .setParameter("quesID",
                        randomQuestionsId.get(indexQuestion)).getResultList();
        for (Iterator j = resultL.iterator(); j.hasNext(); ) {
            Object[] element = (Object[]) j.next();
            answersFromRandomQuestions.add(((Answer) element[0]).getText());
        }
        for (int k = 0; k < answersFromRandomQuestions.size(); k++) {
            if (k == 0) {
                answer1RadioButton.setText(
                        answersFromRandomQuestions.get(k));
                answer1RadioButton.setActionCommand(
                        answersFromRandomQuestions.get(k));
            }
            if (k == 1) {
                answer2RadioButton.setText(answersFromRandomQuestions.get(k));
                answer2RadioButton.setActionCommand(answersFromRandomQuestions.get(k));
            }
            if (k == 2) {
                answer3RadioButton.setText(
                        answersFromRandomQuestions.get(k));
                answer3RadioButton.setActionCommand(
                        answersFromRandomQuestions.get(k));
            }
            if (k == 3) {
                answer4RadioButton.setText(answersFromRandomQuestions.get(k));
                answer4RadioButton.setActionCommand(
                        answersFromRandomQuestions.get(k));
            }
        }
    }

    private void showResult() {

        getMainFrame().dispose();
        // show result card
        ResultGUI resultGUI = ResultGUI.getResultGUI();
        resultGUI.setVisible(true);


        Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
        mainMenu.setEnd(timestampEnd);

        // show result
        resultGUI.getGreetingsLabel().setText("Hallo, " + mainMenu.getPlayerName());
        resultGUI.getNumberLabel().setText(score + "/" + String.valueOf(randomQuestions.size()));
        resultGUI.getStartTimeTextPane().setText(String.valueOf(mainMenu.getStart()));
        resultGUI.getEndTimeTextPane().setText(String.valueOf(mainMenu.getEnd()));
    }

    private void addGameInfoToDB() {

        selectedCategoriesAsEntity();
        randomizedQuestionAsEntity();
        chosenAnswerAsEntity();

        if (mainMenu.isExistingPlayerBool()) {
            informations.add(new Gameinformation(
                    mainMenu.getStart(), mainMenu.getEnd(), score, randomQuestions.size())
            );

            em.getTransaction().begin();

            for (int i = 0; i < informations.size(); i++) {
                informations.get(i).setPlayers(mainMenu.getExistingPlayer());
                for (int k = 0; k < selectedCategoriesEntity.size(); k++) {
                    informations.get(i).getCategories().add(selectedCategoriesEntity.get(k));
                }
                for (int l = 0; l < questionsForDB.size(); l++) {
                    informations.get(i).getQuestions().add(questionsForDB.get(l));
                }
                for (int m = 0; m < answersForDB.size(); m++) {
                    informations.get(i).getAnswers().add(answersForDB.get(m));
                }
                em.persist(informations.get(i));
            }
            em.getTransaction().commit();
        } else {
            for (int i = 0; i < mainMenu.getPlayers().size(); i++) {
                mainMenu.getPlayers().get(i).gameInfo.add((new Gameinformation(
                        mainMenu.getStart(), mainMenu.getEnd(), score, randomQuestions.size())
                ));
            }

            em.getTransaction().begin();
            for (int i = 0; i < mainMenu.getPlayers().size(); i++) {
                em.persist(mainMenu.getPlayers().get(i));
                for (int j = 0; j < mainMenu.getPlayers().get(i).gameInfo.size(); j++) {
                    mainMenu.getPlayers().get(i).gameInfo.get(j).setPlayers(
                            mainMenu.getPlayers().get(i));
                    for (int k = 0; k < selectedCategoriesEntity.size(); k++) {
                        mainMenu.getPlayers().get(i).gameInfo.get(j).getCategories().add(
                                selectedCategoriesEntity.get(k));
                    }
                    for (int l = 0; l < questionsForDB.size(); l++) {
                        mainMenu.getPlayers().get(i).gameInfo.get(j).getQuestions().add(
                                questionsForDB.get(l));
                    }
                    for (int m = 0; m < answersForDB.size(); m++) {
                        mainMenu.getPlayers().get(i).gameInfo.get(j).getAnswers().add(
                                answersForDB.get(m));
                    }
                    em.persist(mainMenu.getPlayers().get(i).gameInfo.get(j));
                }
            }
            em.getTransaction().commit();
        }
    }

    private List<Category> selectedCategoriesAsArrayList() {
        for (int i = 0; i < catGUI.getListSelectedCat().getModel().getSize(); i++) {
            String categoryName;
            categoryName = catGUI.getListSelectedCat().getModel().getElementAt(i).toString();
            selectedCategoriesName.add(new Category(categoryName));
        }
        return selectedCategoriesName;
    }

    private void randomizedQuestionAsEntity() {
        for (int i = 0; i < randomQuestionsId.size(); i++) {
            List resultL = em.createQuery("select q from Question q where q.questionId = :qID")
                    .setParameter("qID", randomQuestionsId.get(i)).getResultList();
            for (Iterator j = resultL.iterator(); j.hasNext(); ) {
                Question cm = (Question) j.next();
                questionsForDB.add(cm);
            }
        }
    }

    public ButtonGroup getAnswerButtonGroup() {
        return answerButtonGroup;
    }

    private void chosenAnswerAsEntity() {
        for (int i = 0; i < randomQuestionsId.size(); i++) {
            List resultL = em.createQuery("select a, q from Answer a join a.ques q where " +
                    "q.questionId = :quesID and a.text = :aText")
                    .setParameter("quesID", randomQuestionsId.get(i))
                    .setParameter("aText", chosenAnswers.get(i)).getResultList();
            for (Iterator j = resultL.iterator(); j.hasNext(); ) {
                Object[] element = (Object[]) j.next();
                answersForDB.add(((Answer) element[0]));
            }
        }
    }

    private void selectedCategoriesAsEntity() {
        for (int i = 0; i < selectedCategoriesName.size(); i++) {
            List resultL = em.createQuery("select c from Category c where " +
                    "c.categoryName = :catName")
                    .setParameter("catName", selectedCategoriesName.get(i).
                            getCategoryName()).getResultList();
            for (Iterator j = resultL.iterator(); j.hasNext(); ) {
                Category cm = (Category) j.next();
                selectedCategoriesEntity.add(cm);
            }
        }
    }


    private JFrame getMainFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this.panelQuestion);
    }

    public JPanel getPanel() {
        return panelQuestion;
    }

    public static QuestionGUI getInstance() {
        if (instance == null) {
            instance = new QuestionGUI();
        }

        return instance;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        setupUI();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void setupUI() {
        panelQuestion = new JPanel();
        panelQuestion.setLayout(new GridLayoutManager(6, 4,
                new Insets(0, 0, 0, 0), -1, -1));
        panelQuestion.setMinimumSize(new Dimension(500, 500));
        panelQuestion.setPreferredSize(new Dimension(500, 500));
        final JLabel label1 = new JLabel();
        Font label1Font = this.getFont(null, -1, 22, label1.getFont());
        if (label1Font != null) {
            label1.setFont(label1Font);
        }
        label1.setText("Frage");
        panelQuestion.add(label1, new GridConstraints(0, 1, 1, 3,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelQuestion.add(spacer1, new GridConstraints(2, 3, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
                1, GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false));
        kategorieFrageLabel = new JLabel();
        kategorieFrageLabel.setText("Kategorie :");
        panelQuestion.add(kategorieFrageLabel, new GridConstraints(1, 1, 1, 3,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        answer1RadioButton = new JRadioButton();
        answer1RadioButton.setText("Antwort 1");
        panelQuestion.add(answer1RadioButton, new GridConstraints(3, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        answer2RadioButton = new JRadioButton();
        answer2RadioButton.setText("Antwort 2");
        panelQuestion.add(answer2RadioButton, new GridConstraints(3, 2, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK |
                        GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        answer4RadioButton = new JRadioButton();
        answer4RadioButton.setText("Antwort 4");
        panelQuestion.add(answer4RadioButton, new GridConstraints(4, 2, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        answer3RadioButton = new JRadioButton();
        answer3RadioButton.setText("Antowrt 3");
        panelQuestion.add(answer3RadioButton, new GridConstraints(4, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelQuestion.add(spacer2, new GridConstraints(2, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
                1, GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false));
        weiterButton = new JButton();
        weiterButton.setText("Weiter");
        panelQuestion.add(weiterButton, new GridConstraints(5, 2, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK |
                        GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        questionTextPane = new JTextPane();
        panelQuestion.add(questionTextPane, new GridConstraints(2, 1, 1, 2,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW,
                null, new Dimension(150, 50), null, 0, false));
        answerButtonGroup = new ButtonGroup();
        answerButtonGroup.add(answer1RadioButton);
        answerButtonGroup.add(answer2RadioButton);
        answerButtonGroup.add(answer4RadioButton);
        answerButtonGroup.add(answer3RadioButton);
    }

    /**
     * @noinspection ALL
     */
    private Font getFont(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) {
            return null;
        }
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style :
                currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent getRootComponent() {
        return panelQuestion;
    }



}
