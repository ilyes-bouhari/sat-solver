import javax.swing.*;

import gui.Main;

public class Application {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                Main frame = new Main("SAT Solver");
                frame.setVisible(true);
            }
        });
    }
}
