/**
 *
 */
package de.hda.fbi.db2.stud.gui;


import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.List;
import javax.persistence.*;
import javax.swing.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.hda.fbi.db2.stud.entity.Category;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 *
 *
 */

@SuppressFBWarnings({"UWF_NULL_FIELD", "NP_UNWRITTEN_FIELD", "UR_UNINIT_READ"
        , "UWF_UNWRITTEN_FIELD"})

public class CategoryGUI extends JFrame {
    private JList listavailableCat;
    private JList listSelectedCat;
    private JTextField textFieldCount;
    private JButton weiterButton;
    private JPanel panelCategory = null;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultMAIN");
    private EntityManager em = emf.createEntityManager();
    private int maxQuestion;

    DefaultListModel defaultListModelFromDatabase = new DefaultListModel();
    DefaultListModel selectedCategory = new DefaultListModel();

    private static CategoryGUI instance = null;

    public CategoryGUI() {

        super("Category");
        setContentPane(panelCategory);
        pack();
        getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        System.out.println("BIMBIMBIMBIMBIMBIMBIBIMBIBMIBM");
        populateCategoriesList();


        listavailableCat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selected = listavailableCat.getSelectedValue().toString();
                selectedCategory.addElement(selected);
                defaultListModelFromDatabase.removeElement(selected);
                listSelectedCat.setModel(selectedCategory);

            }
        });
        weiterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldCount.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Bitte füllen Sie max Anzahl der Fragen aus",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {


                    getMainFrame().dispose();
                    setMaxQuestion(Integer.parseInt(textFieldCount.getText()));
                    //show gui Question
                    QuestionGUI questionQUI = QuestionGUI.getInstance();
                    questionQUI.setVisible(true);

                    //randomizeQuestion();
                    //nextQuestion();
                }
            }
        });
        textFieldCount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                textFieldCountKeyTyped(e);
            }
        });
    }

    public int getMaxQuestion() {
        return maxQuestion;
    }

    public void setMaxQuestion(int maxQuestion) {
        this.maxQuestion = maxQuestion;
    }

    public JPanel getPanel() {
        return panelCategory;
    }


    public void populateCategoriesList() {
        listavailableCat.setModel(defaultListModelFromDatabase);
        List resultL = em.createQuery("select c from Category c order by c.id").getResultList();
        if (resultL.isEmpty()) {
            System.out.println("Keine Datensätze ausgewählt");
        }
        for (Iterator i = resultL.iterator(); i.hasNext(); ) {
            Category cm = (Category) i.next();
            defaultListModelFromDatabase.addElement(cm.getCategoryName());
            //System.out.print(cm.toString());
        }

        System.out.println("Size model : " + defaultListModelFromDatabase.getSize());
        System.out.println("List model : " + listavailableCat.getModel().getSize());

    }

    private void textFieldCountKeyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c)
                || (c == KeyEvent.VK_BACK_SPACE)
                || c == KeyEvent.VK_DELETE)) {
            evt.consume();
        }
    }


    private JFrame getMainFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this.panelCategory);
    }

    public JList getListavailableCat() {
        return listavailableCat;
    }

    public JList getListSelectedCat() {
        return listSelectedCat;
    }

    public static CategoryGUI getInstance() {
        if (instance == null) {
            instance = new CategoryGUI();
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
     * <p>
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void setupUI() {
        panelCategory = new JPanel();
        panelCategory.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Kategorie");
        panelCategory.add(label1, new GridConstraints(0, 0, 1, 2,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelCategory.add(scrollPane1, new GridConstraints(2, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false));
        listavailableCat = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("Hallo");
        defaultListModel1.addElement("Test");
        defaultListModel1.addElement("We");
        defaultListModel1.addElement("R");
        defaultListModel1.addElement("Cool");
        listavailableCat.setModel(defaultListModel1);
        scrollPane1.setViewportView(listavailableCat);
        final JScrollPane scrollPane2 = new JScrollPane();
        panelCategory.add(scrollPane2, new GridConstraints(2, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null, 0, false));
        listSelectedCat = new JList();
        scrollPane2.setViewportView(listSelectedCat);
        final JLabel label2 = new JLabel();
        label2.setText("Verfügbare Kategorie");
        panelCategory.add(label2, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Ausgewählte Kategorie");
        panelCategory.add(label3, new GridConstraints(1, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Maximale Frage pro Kategorie");
        panelCategory.add(label4, new GridConstraints(3, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        textFieldCount = new JTextField();
        panelCategory.add(textFieldCount, new GridConstraints(3, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, new Dimension(150, -1), null, 0, false));
        weiterButton = new JButton();
        weiterButton.setText("Weiter");
        panelCategory.add(weiterButton, new GridConstraints(4, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent getRootComponent() {
        return panelCategory;
    }

}
