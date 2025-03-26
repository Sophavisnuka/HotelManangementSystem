package gui;

import javax.swing.SwingUtilities;

public class appLuncher {
    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                new RegisterGui().setVisible(true);
            }
        });
    }
}
