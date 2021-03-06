package pruebasMovimiento;


import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Objects;

public class Menu extends JFrame {
    static Juego juego;
    private final static String GAME = "SLOANEGATE";
    private final static int WIDTH = 512;
    private final static int HEIGHT = 573;
    private static KeyAdapt keyAdapt;
    private static Menu game;
    private Clip clip;
    private Image a;
    static float sound = -10f; //6.02f max
    static float music = -10f; //6.02f max
    static JPanel panelPadre;
    static final JPanel backgroundPanel = new JPanel();
    private JLabel background;
    private final static String savesfolder = "saves";


    public static void main(String[] args) {
        game = new Menu();

        game.setVisible(true);


    }

    private Menu() throws HeadlessException {

        Menu menu = this;

        musica("music/menu.wav");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);
        setTitle(GAME);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("img/icon.png")).getImage());    //Define el icono

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);

        panelPadre = new JPanel();
        panelPadre.setBounds(0, 0, WIDTH, HEIGHT);
        panelPadre.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(panelPadre);
        panelPadre.setLayout(null);


        backgroundPanel.setBounds(-6, -14, WIDTH, HEIGHT);
        panelPadre.add(backgroundPanel);
        backgroundPanel.setLayout(null);


        JButton nuevo_juego = new JButton("Nuevo juego");
        nuevo_juego.setBounds(181, 200, 150, 30);
        nuevo_juego.setContentAreaFilled(true);
        nuevo_juego.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
        nuevo_juego.setBackground(Color.darkGray);
        nuevo_juego.setForeground(Color.white);
        backgroundPanel.add(nuevo_juego);


        JButton cargar_partida = new JButton("Cargar partida");
        cargar_partida.setBounds(181, 245, 150, 30);
        cargar_partida.setContentAreaFilled(true);
        cargar_partida.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
        cargar_partida.setBackground(Color.darkGray);
        cargar_partida.setForeground(Color.white);
        backgroundPanel.add(cargar_partida);


        JButton highscores = new JButton("Mejores puntuaciones");
        highscores.setBounds(171, 290, 170, 30);
        highscores.setContentAreaFilled(true);
        highscores.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
        highscores.setBackground(Color.darkGray);
        highscores.setForeground(Color.white);
        backgroundPanel.add(highscores);

        JButton sonido = new JButton("Sonido");
        sonido.setBounds(181, 335, 150, 30);
        sonido.setContentAreaFilled(true);
        sonido.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
        sonido.setBackground(Color.darkGray);
        sonido.setForeground(Color.white);
        backgroundPanel.add(sonido);

        backgroundPanel.setLayout(null);


        nuevo_juego.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                setFocusable(true);
                remove(panelPadre);
                repaint();

                musica("music/nivel1.wav");

                juego = new Juego(menu);
                juego.start();

                setContentPane(juego);
                validate();
                juego.setVisible(true);

                if (keyAdapt != null) {
                    removeKeyListener(keyAdapt);
                }
                keyAdapt = new KeyAdapt(Juego.player);
                addKeyListener(keyAdapt);


            }

        });


        cargar_partida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                Image a = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/guardarCargarBackground.png")))
                        .getImage();
                background = new JLabel(new ImageIcon(a));
                background.setBounds(0, 0, WIDTH, HEIGHT);
                panelPadre.add(background);

                setContentPane(background);
                invalidate();
                validate();

                panelPadre.setVisible(false);
                ActionListener actionListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton jButton = (JButton) e.getSource();
                        String text = jButton.getText();

                        loadGame(Integer.parseInt(text.substring(text.length() - 1)));
                        setContentPane(juego);
                        validate();
                        juego.setVisible(true);

                        if (keyAdapt != null) {
                            removeKeyListener(keyAdapt);
                        }
                        keyAdapt = new KeyAdapt(Juego.player);
                        addKeyListener(keyAdapt);
                    }
                };

                File file;
                for (int i = 1; i < 4; i++) {
                    file = new File(savesfolder + "/save" + i + ".json");
                    if (file.exists()) {
                        JButton g1 = new JButton("GAME " + i);
                        if (i == 3) {
                            g1.setForeground(Color.black);
                        } else {
                            g1.setForeground(Color.white);
                        }
                        g1.setBounds(57 + 160 * (i - 1), 220, 80, 70);
                        background.add(g1);
                        g1.setOpaque(false);
                        g1.setContentAreaFilled(false);
                        g1.addActionListener(actionListener);
                    }
                }


                JButton menuPrincipal = new JButton("Menu principal");
                menuPrincipal.setBounds(350, 20, 150, 35);
                menuPrincipal.setContentAreaFilled(true);
                menuPrincipal.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
                menuPrincipal.setBackground(Color.darkGray);
                menuPrincipal.setForeground(Color.white);
                background.add(menuPrincipal);
                menuPrincipal.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        menu.remove(background);

                        backgroundPanel.setVisible(true);
                        panelPadre.setVisible(true);
                        menu.setContentPane(backgroundPanel);
                    }
                });
            }
        });


        sonido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                Configuraciones configuraciones = new Configuraciones(game, clip, backgroundPanel, a);

                backgroundPanel.setVisible(false);
                panelPadre.setVisible(false);
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


    void musica(String path) {

        if(Configuraciones.music!=null){
            Configuraciones.music.stop();
        }

        clip = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(ClassLoader.getSystemClassLoader().getResourceAsStream(path)));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(music); // Reduce volume by 10 decibels.
        clip.loop(Clip.LOOP_CONTINUOUSLY);

        Configuraciones.music=clip;

        clip.start();


    }

    private void loadGame(int slot) {
        setFocusable(true);
        repaint();
        System.out.println("Cargado slot 1 aaaa");

        juego = GuardarPartida.loadSave(slot, this);


        setContentPane(juego);
        validate();
        juego.setVisible(true);
        juego.start();

    }


}
