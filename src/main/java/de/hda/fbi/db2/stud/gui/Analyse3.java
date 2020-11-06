package de.hda.fbi.db2.stud.gui;




import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;


/**
 *
 * @author Sascha Bauer, Ahmad Mustain Billah
 *
 */


public class Analyse3 extends JFrame {
    private JPanel panelMain;
    private JButton zurueckButton;
    private JTable tableA3;
    private static Analyse3 instance = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultMAIN");
    private EntityManager em = emf.createEntityManager();

    public Analyse3() {
        super("Analyse GUI");
        setContentPane(panelMain);
        pack();
        setVisible(true);
        getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        List searchPlayerGamesAmount = em.createQuery("select p.playerName, count(p.gameInfo) "
                + "from Player p "
                + "group by p.playerName "
                + "order by count(p.gameInfo) desc")
                .getResultList();

        DefaultTableModel a3Model = new DefaultTableModel(
                new Object[]{"Player Name", "Games Played"}, 0);
        for (Iterator i = searchPlayerGamesAmount.iterator(); i.hasNext(); ) {
            Object[] element = (Object[]) i.next();
            //Player pl = (Player) element[0];
            a3Model.addRow(new Object[]{element[0], element[1]});
        }
        tableA3.setModel(a3Model);
        zurueckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalyseGUI test = AnalyseGUI.getInstance();
                test.setVisible(true);

                getMainFrame().dispose();
            }
        });
    }

    public static Analyse3 getInstance() {
        if (instance == null) {
            instance = new Analyse3();
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
        panelMain.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        zurueckButton = new JButton();
        zurueckButton.setText("Zurueck");
        panelMain.add(zurueckButton, new GridConstraints(1, 0, 1, 2,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK |
                        GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, new GridConstraints(0, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK |
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK
                | GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false));
        tableA3 = new JTable();
        scrollPane1.setViewportView(tableA3);
    }

    /**
     * @noinspection ALL
     */
    public JComponent getRootComponent() {
        return panelMain;
    }
}
