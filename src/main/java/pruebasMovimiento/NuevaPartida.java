package pruebasMovimiento;


import javax.swing.*;
import java.awt.*;


public class NuevaPartida extends JPanel {

    JLabel background;

    public NuevaPartida() {


        Image a = new ImageIcon(getClass().getClassLoader().getResource("img/guardarCargarBackground.png"))
                .getImage();
        background = new JLabel(new ImageIcon(a));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(background);


        JButton b1 = new JButton("G1");
        b1.setBounds(62, 220, 70, 70);
        background.add(b1);

    }

    public static void main(String[] args) {


    }

}
