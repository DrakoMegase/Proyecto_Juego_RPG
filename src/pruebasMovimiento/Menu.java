package pruebasMovimiento;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Menu extends JFrame {
    static Juego juego;
    final static String GAME = "SLOANEGATE";
    final static int WIDTH = 512;
    final static int HEIGHT = 573;

    public static void main(String[] args) {

        Menu game = new Menu();
        game.setVisible(true);


    }

    public Menu() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);
        setTitle(GAME);
        setResizable(false);
        setIconImage(new ImageIcon("res/img/icon.png").getImage());    //Define el icono

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);

        JPanel panelPadre = new JPanel();
        panelPadre.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(panelPadre);
        panelPadre.setLayout(null);


        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBounds(-6, -14, WIDTH, HEIGHT);
        panelPadre.add(backgroundPanel);
        backgroundPanel.setLayout(null);


        JButton nuevo_juego = new JButton("Nuevo juego");
        nuevo_juego.setBounds(181, 200, 150, 30);
        backgroundPanel.add(nuevo_juego);


        JButton cargar_partida = new JButton("Cargar partida");
        cargar_partida.setBounds(181, 245, 150, 30);
        backgroundPanel.add(cargar_partida);


        JButton highscores = new JButton("Mejores puntuaciones");
        highscores.setBounds(181, 290, 150, 30);
        backgroundPanel.add(highscores);

        JButton sonido = new JButton("Sonido");
        sonido.setBounds(181, 335, 150, 30);
        backgroundPanel.add(sonido);

        backgroundPanel.setLayout(null);

        nuevo_juego.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setFocusable(true);
                remove(panelPadre);
                remove(backgroundPanel);
                repaint();

                juego = new Juego("res/jsonsMapasPruebas/1.json", "resources/terrain_atlas.png");
                juego.start();

                setContentPane(juego);
                validate();
                juego.setVisible(true);

                addKeyListener(new KeyAdapt(Juego.player));


            }

        });



        cargar_partida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                NuevaPartida n1 = new NuevaPartida();
                remove(backgroundPanel);
                remove(panelPadre);
                add(n1);
                setContentPane(n1);
                n1.invalidate();
                validate();


            }
        });

        JLabel backgroundLabel;


            Image a = new ImageIcon(getClass().getClassLoader().getResource("img/background.png"))
                    .getImage();
            backgroundLabel = new JLabel(new ImageIcon(a));

            backgroundLabel.setBounds(0, 0, WIDTH, HEIGHT);
            backgroundPanel.add(backgroundLabel);


    }


}
