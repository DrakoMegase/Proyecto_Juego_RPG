package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;

class GameOver extends JPanel {

    private JLabel background;
    boolean ready=false;
    JLabel tituloFinal;
    JLabel monstruos;
    JLabel nivel;
    JLabel puntuacion;

    GameOver(int contador) {

        JPanel a = new JPanel(new FlowLayout(FlowLayout.LEFT));

        background = new JLabel();
        background.setBounds(-6, -14, WIDTH, HEIGHT);
        this.add(background);
        background.setLayout(null);
        background.setVisible(true);




        tituloFinal = new JLabel("FIN DEL JUEGO");
        tituloFinal.setFont(new Font("Verdana", Font.BOLD, 30));
        tituloFinal.setLocation(100,100);
        tituloFinal.setBounds(200,25,300,300);
        tituloFinal.setVisible(true);
        tituloFinal.setLayout(new FlowLayout(FlowLayout.LEFT));

//        monstruos = new JLabel("Mounstruos asesinados:");
//        monstruos.setFont(new Font("Verdana", Font.BOLD, 14));
//        monstruos.setLocation(20,200);
//        monstruos.setBounds(20,200,100,100);
//        monstruos.setVisible(true);
//
//        nivel = new JLabel("Nivel del jugador");
//        nivel.setFont(new Font("Verdana", Font.BOLD, 14));
//        nivel.setLocation(20,300);
//        nivel.setBounds(20,300,300,300);
//        nivel.setVisible(true);
//        nivel.setLayout(new FlowLayout(FlowLayout.LEFT));
//
//
//        puntuacion = new JLabel("PUNTUACION FINAL");
//        puntuacion.setFont(new Font("Verdana", Font.BOLD, 16));
//        puntuacion.setLocation(20,400);
//        puntuacion.setBounds(20,400,20,20);
//        puntuacion.setVisible(true);
//
//
//
//
        add(a);
        a.add(tituloFinal);
//        add(monstruos);
//        add(nivel);
//        add(puntuacion);
        repaint();

        //puntuacion.setLocation(20,400);

    }



    public static void main(String[] args) {
        int a = 0;
        GameOver gameOver = new GameOver(a);
        //gameOver.tablafinal(gameOver.tituloFinal);

    }


}
