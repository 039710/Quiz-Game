package de.hda.fbi.db2.stud.gui;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.hda.fbi.db2.stud.entity.Player;


/**
 *
 * @author Sascha Bauer, Ahmad Mustain Billah
 *
 */

public class Analyse1 extends JFrame {
    private JTextField startTimeTF;
    private JTextField endTimeTF;
    private JButton buttonStart;
    private JTable tableA1;
    private JPanel panelMain;
    private JButton zurueckButton;
    private static Analyse1 instance = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultMAIN");
    private EntityManager em = emf.createEntityManager();

    public Analyse1() {
        super("Analyse GUI");
        setContentPane(panelMain);
        pack();
        setVisible(true);
        getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        zurueckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalyseGUI test = AnalyseGUI.getInstance();
                test.setVisible(true);

                getMainFrame().dispose();
            }
        });
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List searchPlayer = em.createQuery("select g.players "
                        + "from Gameinformation g "
                        + "where g.timeStart between :startDate and :endDate")
                        .setParameter("startDate", Timestamp.valueOf(startTimeTF.getText()))
                        .setParameter("endDate", Timestamp.valueOf(endTimeTF.getText()))
                        .getResultList();

                DefaultTableModel a1Model = new DefaultTableModel(new Object[]{"ID", "Player"}, 0);
                for (Iterator i = searchPlayer.iterator(); i.hasNext(); ) {
                    Player cm = (Player) i.next();
                    a1Model.addRow(new Object[]{cm.getPlayerID(), cm.getPlayerName()});
                }
                tableA1.setModel(a1Model);

            }
        });
    }

    public static Analyse1 getInstance() {
        if (instance == null) {
            instance = new Analyse1();
        }

        return instance;
    }

    private JFrame getMainFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this.panelMain);
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
        panelMain.setLayout(new GridLayoutManager(4, 6, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Start");
        panelMain.add(label1, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        startTimeTF = new JTextField();
        panelMain.add(startTimeTF, new GridConstraints(1, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("format : yyyy-mm-dd hh:mm:ss");
        panelMain.add(label2, new GridConstraints(0, 2, 1, 2,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        buttonStart = new JButton();
        buttonStart.setText("Suchen");
        panelMain.add(buttonStart, new GridConstraints(1, 5, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zurueckButton = new JButton();
        zurueckButton.setText("Zurueck");
        panelMain.add(zurueckButton, new GridConstraints(2, 5, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, new GridConstraints(3, 1, 1, 4,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false));
        tableA1 = new JTable();
        scrollPane1.setViewportView(tableA1);
        final JLabel label3 = new JLabel();
        label3.setText("End");
        panelMain.add(label3, new GridConstraints(1, 2, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        endTimeTF = new JTextField();
        panelMain.add(endTimeTF, new GridConstraints(1, 3, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1),
                null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent getRootComponent() {
        return panelMain;
    }
}