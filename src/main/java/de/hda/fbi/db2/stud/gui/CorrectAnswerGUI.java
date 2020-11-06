package de.hda.fbi.db2.stud.gui;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 *
 *
 */


@SuppressFBWarnings({"UWF_NULL_FIELD", "NP_UNWRITTEN_FIELD", "UR_UNINIT_READ"
        , "UWF_UNWRITTEN_FIELD"})

public class CorrectAnswerGUI extends JFrame {


    private JTextPane richtigTextPane;
    private JButton weiterButton;
    private JLabel kategorieFrageKorrektLabel;
    private JLabel idFrageKorrektLabel;
    private JTextPane playerAnswerTextPane;
    private JTextPane correctAnswerTextPane;
    private JLabel rightWrongLabel;
    private JPanel panelMain;

    private static CorrectAnswerGUI correctAnswerGUI = null;

    public CorrectAnswerGUI() {
        super("Correct Answer");
        setContentPane(panelMain);
        pack();
        setVisible(true);
        getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        weiterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuestionGUI questionGUI = QuestionGUI.getInstance();
                questionGUI.setVisible(true);

                questionGUI.getAnswerButtonGroup().clearSelection();
                questionGUI.nextQuestion();
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

    public JTextPane getRichtigTextPane() {
        return richtigTextPane;
    }

    public void setRichtigTextPane(JTextPane richtigTextPane) {
        this.richtigTextPane = richtigTextPane;
    }

    public JButton getWeiterButton() {
        return weiterButton;
    }

    public void setWeiterButton(JButton weiterButton) {
        this.weiterButton = weiterButton;
    }

    public JLabel getKategorieFrageKorrektLabel() {
        return kategorieFrageKorrektLabel;
    }

    public void setKategorieFrageKorrektLabel(JLabel kategorieFrageKorrektLabel) {
        this.kategorieFrageKorrektLabel = kategorieFrageKorrektLabel;
    }

    public JLabel getIdFrageKorrektLabel() {
        return idFrageKorrektLabel;
    }

    public void setIdFrageKorrektLabel(JLabel idFrageKorrektLabel) {
        this.idFrageKorrektLabel = idFrageKorrektLabel;
    }

    public JTextPane getPlayerAnswerTextPane() {
        return playerAnswerTextPane;
    }

    public void setPlayerAnswerTextPane(JTextPane playerAnswerTextPane) {
        this.playerAnswerTextPane = playerAnswerTextPane;
    }

    public JTextPane getCorrectAnswerTextPane() {
        return correctAnswerTextPane;
    }

    public void setCorrectAnswerTextPane(JTextPane correctAnswerTextPane) {
        this.correctAnswerTextPane = correctAnswerTextPane;
    }

    public JLabel getRightWrongLabel() {
        return rightWrongLabel;
    }

    public void setRightWrongLabel(JLabel rightWrongLabel) {
        this.rightWrongLabel = rightWrongLabel;
    }

    public static CorrectAnswerGUI getCorrectAnswerGUI() {
        if (correctAnswerGUI == null) {
            correctAnswerGUI = new CorrectAnswerGUI();
        }

        return correctAnswerGUI;
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
        panelMain.setLayout(new GridLayoutManager(
                8, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Frage");
        panelMain.add(label1, new GridConstraints(0, 0, 1, 3,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        kategorieFrageKorrektLabel = new JLabel();
        kategorieFrageKorrektLabel.setText("Kategorie");
        panelMain.add(kategorieFrageKorrektLabel, new GridConstraints(1, 0, 1, 3,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        idFrageKorrektLabel = new JLabel();
        idFrageKorrektLabel.setText("ID");
        panelMain.add(idFrageKorrektLabel, new GridConstraints(2, 0, 1, 3,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Ihre Anwort :");
        panelMain.add(label2, new GridConstraints(4, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Richtige Antwort");
        panelMain.add(label3, new GridConstraints(5, 0, 1, 2,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        richtigTextPane = new JTextPane();
        panelMain.add(richtigTextPane, new GridConstraints(3, 0, 1, 3,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                null, new Dimension(150, 50), null, 0, false));
        rightWrongLabel = new JLabel();
        rightWrongLabel.setText("Richtig!");
        panelMain.add(rightWrongLabel, new GridConstraints(6, 0, 1, 3,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        weiterButton = new JButton();
        weiterButton.setText("Weiter");
        panelMain.add(weiterButton, new GridConstraints(7, 0, 1, 3,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        playerAnswerTextPane = new JTextPane();
        panelMain.add(playerAnswerTextPane, new GridConstraints(4, 2, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                null, new Dimension(150, 50), null, 0, false));
        correctAnswerTextPane = new JTextPane();
        correctAnswerTextPane.setText("");
        panelMain.add(correctAnswerTextPane, new GridConstraints(5, 2, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent getRootComponent() {
        return panelMain;
    }


}
