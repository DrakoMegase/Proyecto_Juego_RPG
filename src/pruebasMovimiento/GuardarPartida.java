package pruebasMovimiento;


import javax.swing.*;
import java.awt.*;


public class GuardarPartida extends JPanel {

    JLabel background;

    public GuardarPartida() {


        Image a = new ImageIcon(getClass().getClassLoader().getResource("img/guardarCargarBackground.png"))
                .getImage();
        background = new JLabel(new ImageIcon(a));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(background);



    }

    public static void main(String[] args) {


    }

}
