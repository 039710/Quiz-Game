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

public class Analyse4 extends JFrame {
    private JPanel panelMain;
    private JTable tableA4;
    private JButton zurueckButton;
    private static Analyse4 instance = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultMAIN");
    private EntityManager em = emf.createEntityManager();


    public Analyse4() {
        super("Analyse GUI");
        setContentPane(panelMain);
        pack();
        setVisible(true);
        zurueckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalyseGUI test = AnalyseGUI.getInstance();
                test.setVisible(true);

                getMainFrame().dispose();
            }
        });
        List searchPlayerGamesAmount = em.createQuery("select c.categoryName, count(c.gameInfo) "
                + "from Category c "
                + "group by c.categoryName "
                + "order by count(c.gameInfo) desc")
                .getResultList();

        DefaultTableModel a4Model = new DefaultTableModel(
                new Object[]{"Category", "Times Selected"}, 0);
        for (Iterator i = searchPlayerGamesAmount.iterator(); i.hasNext(); ) {
            Object[] element = (Object[]) i.next();
            //Player pl = (Player) element[0];
            a4Model.addRow(new Object[]{element[0], element[1]});
        }
        tableA4.setModel(a4Model);
    }

    public static Analyse4 getInstance() {
        if (instance == null) {
            instance = new Analyse4();
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
        panelMain.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK |
                        GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK |
                GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false));
        tableA4 = new JTable();
        scrollPane1.setViewportView(tableA4);
        zurueckButton = new JButton();
        zurueckButton.setText("Zurueck");
        panelMain.add(zurueckButton, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent getRootComponent() {
        return panelMain;
    }
}