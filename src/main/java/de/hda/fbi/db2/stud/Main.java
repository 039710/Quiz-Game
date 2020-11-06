package de.hda.fbi.db2.stud;




import de.hda.fbi.db2.stud.gui.MainMenu;

/**
 * Main Class.
 * @version 0.1.1
 * @since 0.1.0
 * @SaschaBauer, Ahmad Mustain Billah
 */
public class Main {
    /**
     * Main Method and Entry-Point.
     * @param args Command-Line Arguments.
     */


        public static void main(String[] args) {
        Controller contr = new Controller ();



        contr.readCSV();

        //persist all data from csv
        if (contr.isDataAlreadyExist() == false)        {
            contr.persistCSV();
        }

        MainMenu.getMainmenu();




    }
    
    public String getGreeting() {
        return "app should have a greeting";
    }
}
