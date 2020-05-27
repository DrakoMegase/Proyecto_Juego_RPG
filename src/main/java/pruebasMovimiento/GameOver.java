package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

class GameOver extends JPanel {

    private JLabel background;
    boolean ready = false;
    int WIDTH = 512;
    int HEIGHT = 573;
    JButton continuar;
    JLabel monsInt;
    JLabel playerLvInt;
    JLabel dineroInt;
    JLabel puntInt;
    JLabel puntFinalInt;


    public GameOver() {

        setLayout(null);
        JPanel titulo = new JPanel();
        JPanel puntuaciones = new JPanel(new GridLayout(5, 2, 65, 8));
        puntuaciones.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        continuar = new JButton("CONTINUAR");
        continuar.setForeground(Color.WHITE);
        continuar.setOpaque(false);
        continuar.setContentAreaFilled(false);
        add(continuar);
        continuar.setBounds(WIDTH - 147, HEIGHT - 108, 110, 30);

        titulo.setBounds(4, 35, (WIDTH - 20), 100);

        puntuaciones.setBounds(2, 175, WIDTH - 20, HEIGHT - titulo.getHeight() - 180);


        add(titulo);
        add(puntuaciones);

        titulo.setVisible(true);
        puntuaciones.setVisible(true);


        JLabel tituloLabel = new JLabel("FIN DEL JUEGO");
        tituloLabel.setForeground(Color.BLACK);
        tituloLabel.setFont(new Font("Verdana", Font.BOLD, 30));


        JLabel monstruosLabel = new JLabel("Monstruos asesinados");
        monstruosLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        monstruosLabel.setForeground(Color.white);
        monsInt = new JLabel("1000");
        monsInt.setFont(new Font("Verdana", Font.BOLD, 14));


        JLabel lvLabel = new JLabel("Nivel jugador");
        lvLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        lvLabel.setForeground(Color.white);
        playerLvInt = new JLabel("1000");
        playerLvInt.setFont(new Font("Verdana", Font.BOLD, 14));


        JLabel dineroLabel = new JLabel("Dinero");
        dineroLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        dineroLabel.setForeground(Color.white);
        dineroInt = new JLabel("1000");
        dineroInt.setFont(new Font("Verdana", Font.BOLD, 14));


        JLabel puntIntLabel = new JLabel("Puntuacion interna");
        puntIntLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        puntIntLabel.setForeground(Color.white);
        puntInt = new JLabel("1000");
        puntInt.setFont(new Font("Verdana", Font.BOLD, 14));


        JLabel puntFinal = new JLabel("PUNTUACION FINAL");
        puntFinal.setFont(new Font("Verdana", Font.BOLD, 16));
        puntFinal.setForeground(Color.BLACK);
        puntFinalInt = new JLabel("1000");
        puntFinalInt.setFont(new Font("Verdana", Font.BOLD, 18));
        puntFinalInt.setForeground(Color.BLACK);


        JLabel backgroundLabel;


        Image a = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/backgroundpuntuaciones.png")))
                .getImage();
        backgroundLabel = new JLabel(new ImageIcon(a));

        backgroundLabel.setBounds(0, -14, WIDTH, HEIGHT);
        add(backgroundLabel);
        puntuaciones.setOpaque(false);
        titulo.setOpaque(false);


        titulo.add(tituloLabel);
        puntuaciones.add(monstruosLabel);
        puntuaciones.add(monsInt);

        puntuaciones.add(lvLabel);
        puntuaciones.add(playerLvInt);

        puntuaciones.add(dineroLabel);
        puntuaciones.add(dineroInt);

        puntuaciones.add(puntIntLabel);
        puntuaciones.add(puntInt);

        puntuaciones.add(puntFinal);
        puntuaciones.add(puntFinalInt);


        monsInt.setVisible(false);
        playerLvInt.setVisible(false);
        dineroInt.setVisible(false);
        puntInt.setVisible(false);
        puntFinalInt.setVisible(false);
        continuar.setVisible(false);


    }

    public void aparicion(int contador) {

        if (contador > 300) monsInt.setVisible(true);
        if (contador > 375) playerLvInt.setVisible(true);
        if (contador > 450) dineroInt.setVisible(true);
        if (contador > 525) puntInt.setVisible(true);
        if (contador > 600) puntFinalInt.setVisible(true);
        if (contador > 675) continuar.setVisible(true);


    }


    public static void main(String[] args) {

    }


}
