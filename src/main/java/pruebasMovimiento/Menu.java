package pruebasMovimiento;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.Objects;

public class Menu extends JFrame {
    static Juego juego;
    private final static String GAME = "SLOANEGATE";
    final static int WIDTH = 512;
    final static int HEIGHT = 573;
    static Menu game;
    Clip clip;
    Image a;
    public static float sound = 0; //6.02f max
    static JPanel panelPadre;
    static JPanel backgroundPanel;
    public static void main(String[] args) {
        game = new Menu();

        game.setVisible(true);


    }

    public Menu() throws HeadlessException {

        Menu menu = this;

        musica("res/music/soundtrack1.wav");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);
        setTitle(GAME);
        setResizable(false);
        setIconImage(new ImageIcon("res/img/icon.png").getImage());    //Define el icono

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);

        panelPadre = new JPanel();
        panelPadre.setBounds(0, 0, WIDTH, HEIGHT);
        panelPadre.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(panelPadre);
        panelPadre.setLayout(null);


        backgroundPanel = new JPanel();
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
        highscores.setBounds(171, 290, 170, 30);
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
                repaint();

                juego = new Juego("res/jsonsMapasPruebas/1.json", menu);
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

                NuevaPartida n1 = new NuevaPartida(game, backgroundPanel, panelPadre);
                remove(backgroundPanel);
                remove(panelPadre);
                add(n1);
                setContentPane(n1);
                n1.invalidate();
                validate();


            }
        });


        sonido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                Configuraciones configuraciones = new Configuraciones(game, clip, backgroundPanel, a);

                remove(backgroundPanel);
                remove(panelPadre);
                add(configuraciones);
                setContentPane(configuraciones);
                configuraciones.invalidate();
                validate();

            }
        });

        JLabel backgroundLabel;


        a = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/interfazmenu/primerapantalla.png")))
                .getImage();
        backgroundLabel = new JLabel(new ImageIcon(a));

        backgroundLabel.setBounds(3, 5, WIDTH, HEIGHT);
        backgroundPanel.add(backgroundLabel);


            highscores.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    Puntuaciones configuraciones = new Puntuaciones(game, backgroundPanel, a);

                    remove(backgroundPanel);
                    remove(panelPadre);
                    add(configuraciones);
                    setContentPane(configuraciones);
                    configuraciones.invalidate();
                    validate();

                }
            });



    }



    void musica(String path){

        clip = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File(path));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.02f); // Reduce volume by 10 decibels.
        clip.start();


    }


}
