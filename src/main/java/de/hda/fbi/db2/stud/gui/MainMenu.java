package de.hda.fbi.db2.stud.gui;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.hda.fbi.db2.stud.entity.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 *
 */
@SuppressFBWarnings({"UWF_NULL_FIELD", "NP_UNWRITTEN_FIELD", "UR_UNINIT_READ"
        , "UWF_UNWRITTEN_FIELD"})

public class MainMenu extends JFrame {
    private JPanel panelMain;

    private JButton buttonStart;
    private JTextField playerNameTF;
    private JButton buttonMassen;
    private JButton buttonAnalyse;
    private JTextField anzahlSpielerTF;
    private JTextField anzahlSpielTF;

    private static MainMenu mainmenu = null;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultMAIN");
    EntityManager em = emf.createEntityManager();

    private List<Player> players = new ArrayList<>();

    private Timestamp start;
    private Timestamp end;

    private Player existingPlayer;
    private boolean existingPlayerBool;
    private String playerName;
    List<Category> selectedCategoriesEntity = new ArrayList<>();
    List<Question> questionsForDB = new ArrayList<>();
    List<Answer> answersForDB = new ArrayList<>();

    public MainMenu() {
        super("Main Menu");
        setContentPane(panelMain);
        pack();
        setVisible(true);
        getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Check if player already in Database
                if (playerNameTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Bitte Geben Sie Name des Spielers",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    List playerList = em.createQuery(
                            "select p from Player p where p.playerName = :pname")
                            .setParameter("pname", playerNameTF.getText()).getResultList();
                    if (playerList.isEmpty()) {


                    } else {
                        Player pName = (Player) playerList.get(0);
                        existingPlayer = pName;
                        JOptionPane.showMessageDialog(null, "Willkommen zurück, "
                                + pName.getPlayerName(), "Meldung",
                                JOptionPane.INFORMATION_MESSAGE);
                        existingPlayerBool = true;

                    }

                    //Create Player Object if player doesn't exit in database
                    String playerName = playerNameTF.getText();
                    Player newPlayer = new Player();
                    newPlayer.setPlayerName(playerName);
                    players.add(newPlayer);

                    //Set start time
                    Timestamp timestampStart = new Timestamp(System.currentTimeMillis());
                    start = timestampStart;
                    //Dispose MainMenu gui
                    getMainFrame().dispose();

                    CategoryGUI test = CategoryGUI.getInstance();
                    test.setVisible(true);

                }


            }
        });
        buttonMassen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (anzahlSpielerTF.getText().isEmpty() || anzahlSpielTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Bitte füllen Sie Anzahl der Spieler und Spiel aus",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    List resultC = em.createQuery("select c from Category c").getResultList();
                    long startTime = System.currentTimeMillis();

                    // Add Players
                    int playerAmount = Integer.parseInt(anzahlSpielerTF.getText());


                    for (int i = 0; i < playerAmount; i++) {
                        String pNameTest = "Spieler" + +i;
                        Player player = new Player();
                        player.setPlayerName("Spieler" + i);
                        players.add(player);
                    }


                    //Add GameInfo
                    int gamesPerPlayer = Integer.parseInt(anzahlSpielTF.getText());

                    em.getTransaction().begin();
                    for (int i = 0; i < players.size(); i++) {

                        Calendar calendar = Calendar.getInstance();

                        for (int o = 0; o < gamesPerPlayer; o++) {
                            selectedCategoriesEntity.clear();
                            questionsForDB.clear();
                            answersForDB.clear();

                            int dummyScore = 0;
                            Timestamp dummyStart;
                            Timestamp dummyEnd;

                            //Add chosen Categories
                            Collections.shuffle(resultC);
                            for (int j = 0; j < 2; j++) {
                                Category cm = (Category) resultC.get(j);
                                selectedCategoriesEntity.add(cm);
                            }

                            //Add random Questions
                            for (int j = 0; j < selectedCategoriesEntity.size(); j++) {
                                List resultQ = em.createQuery("select q, c from Question q " +
                                        "join q.cat c where c.categoryName = :catName")
                                        .setParameter("catName",
                                                selectedCategoriesEntity.get(j).getCategoryName())
                                        .setMaxResults(2)
                                        .getResultList();
                                for (int k = 0; k < resultQ.size(); k++) {
                                    Object[] element = (Object[]) resultQ.get(k);
                                    Question quest1 = (Question) element[0];
                                    questionsForDB.add(quest1);
                                    List<Answer> ans1 = (List) quest1.getAnswers();
                                    Collections.shuffle(ans1);

                                    Answer ans2 = (Answer) ans1.get(0);
                                    if (ans2.isAnsCorrect()) {
                                        dummyScore += 1;
                                    }
                                    answersForDB.add(ans2);
                                }

                            }


                            dummyStart = new Timestamp(calendar.getTimeInMillis());
                            calendar.add(Calendar.HOUR, 1);
                            dummyEnd = new Timestamp(calendar.getTimeInMillis());


                            players.get(i).gameInfo.add((new Gameinformation
                                    (dummyStart, dummyEnd, dummyScore, questionsForDB.size()))
                            );



                            players.get(i).gameInfo.get(o).setPlayers(players.get(i));
                            for (int k = 0; k < selectedCategoriesEntity.size(); k++) {
                                players.get(i).gameInfo.get(o).getCategories()
                                        .add(selectedCategoriesEntity.get(k));
                            }
                            for (int l = 0; l < questionsForDB.size(); l++) {
                                players.get(i).gameInfo.get(o).getQuestions()
                                        .add(questionsForDB.get(l));
                            }
                            for (int m = 0; m < answersForDB.size(); m++) {
                                players.get(i).gameInfo.get(o).getAnswers()
                                        .add(answersForDB.get(m));
                            }

                            //Increment day by 1
                            calendar.add(Calendar.DATE, 1);

                        }
                        em.persist(players.get(i));
                        for (int z = 0; z < players.get(i).gameInfo.size(); z++) {
                            em.persist(players.get(i).gameInfo.get(z));
                        }
                        em.flush();
                        em.clear();
                    }


                    em.getTransaction().commit();

                    long endTime = System.currentTimeMillis();
                    long totalTime = (endTime - startTime) / 1000;
                    JOptionPane.showMessageDialog(null, "Zeit : "
                            + totalTime + " sekunden", "Meldung", JOptionPane.INFORMATION_MESSAGE);
                    //getMainFrame().dispose();
                }
            }
        });
        buttonAnalyse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalyseGUI test = AnalyseGUI.getInstance();
                test.setVisible(true);

                getMainFrame().dispose();
            }
        });

    }

    public JPanel getPanelMain() {
        return panelMain;
    }

    private JFrame getMainFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this.panelMain);
    }


    public boolean isExistingPlayerBool() {
        return existingPlayerBool;
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Timestamp getStart() {
        return new Timestamp(start.getTime());
    }

    public void setStart(Timestamp start) {
        this.start = new Timestamp(start.getTime());
    }

    public Timestamp getEnd() {
        return new Timestamp(end.getTime());
    }

    public void setEnd(Timestamp end) {
        this.end = new Timestamp(end.getTime());
    }

    public Player getExistingPlayer() {
        return existingPlayer;
    }

    public void setExistingPlayer(Player existingPlayer) {
        this.existingPlayer = existingPlayer;
    }

    public static MainMenu getMainmenu() {
        if (mainmenu == null) {
            mainmenu = new MainMenu();
        }

        return mainmenu;
    }

    public String getPlayerName() {
        return playerNameTF.getText();
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
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.setMinimumSize(new Dimension(500, 500));
        panelMain.setPreferredSize(new Dimension(500, 500));
        panelMain.setRequestFocusEnabled(false);
        playerNameTF = new JTextField();
        panelMain.add(playerNameTF, new GridConstraints(1, 1, 1, 3,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Player Name :");
        panelMain.add(label1, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.getFont(null, -1, 26, label2.getFont());
        if (label2Font != null) {
            label2.setFont(label2Font);
        }
        label2.setText("Wissentest V2");
        panelMain.add(label2, new GridConstraints(0, 0, 1, 3,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        buttonMassen = new JButton();
        buttonMassen.setHorizontalAlignment(0);
        buttonMassen.setText("Massendaten generieren");
        panelMain.add(buttonMassen, new GridConstraints(4, 2, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK |
                        GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Max Player");
        panelMain.add(label3, new GridConstraints(2, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        buttonAnalyse = new JButton();
        buttonAnalyse.setText("Analyse Spieldaten");
        panelMain.add(buttonAnalyse, new GridConstraints(4, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK |
                        GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        buttonStart = new JButton();
        buttonStart.setText("Start");
        panelMain.add(buttonStart, new GridConstraints(4, 3, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Max Game");
        panelMain.add(label4, new GridConstraints(3, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        anzahlSpielerTF = new JTextField();
        panelMain.add(anzahlSpielerTF, new GridConstraints(2, 2, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, new Dimension(150, -1), null, 0, false));
        anzahlSpielTF = new JTextField();
        panelMain.add(anzahlSpielTF, new GridConstraints(3, 2, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, new Dimension(150, -1), null, 0, false));
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
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(),
                size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent getRootComponent() {
        return panelMain;
    }
}
