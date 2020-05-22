package pruebasMovimiento;

import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class GameOver extends JPanel {

    JLabel background;

    public GameOver() {

        background = new JLabel();
        background.setBounds(-6, -14, WIDTH, HEIGHT);
        this.add(background);
        background.setLayout(null);
        background.setVisible(true);



    }
}
