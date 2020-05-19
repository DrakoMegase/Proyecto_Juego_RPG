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
    final static String GAME = "SLOANEGATE";
    final static int WIDTH = 512;
    final static int HEIGHT = 573;
    static Menu game;


    public static void main(String[] args) {
        game = new Menu();

        game.setVisible(true);


    }

    public Menu() throws HeadlessException {
//        musica("res/music/soundtrack1.wav");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);
        setTitle(GAME);
        setResizable(false);
        setIconImage(new ImageIcon("res/img/icon.png").getImage());    //Define el icono

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, WIDTH, HEIGHT);

        final JPanel panelPadre = new JPanel();
        panelPadre.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(panelPadre);
        panelPadre.setLayout(null);


        final JPanel backgroundPanel = new JPanel();
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
                remove(backgroundPanel);
                repaint();

                juego = new Juego("res/jsonsMapasPruebas/1.json");
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

        JLabel backgroundLabel;


        Image a = new ImageIcon(getClass().getClassLoader().getResource("img/interfazmenu/primerapantalla.png"))
                .getImage();
        backgroundLabel = new JLabel(new ImageIcon(a));

        backgroundLabel.setBounds(0, 0, WIDTH, HEIGHT);
        backgroundPanel.add(backgroundLabel);


            highscores.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    // Fetch the service account key JSON file contents
                    FileInputStream serviceAccount = null;
                    try {
                        serviceAccount = new FileInputStream(new File(ClassLoader.getSystemClassLoader().getResource("firebase/sloanegate-firebase-adminsdk-yaki9-3e58e67761.json").toURI()));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

// Initialize the app with a service account, granting admin privileges
                    FirebaseOptions options = null;
                    try {
                        options = new FirebaseOptions.Builder()
                                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                .setDatabaseUrl("https://sloanegate.firebaseio.com")
                                .build();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    FirebaseApp.initializeApp(options);

// As an admin, the app has access to read and write all data, regardless of Security Rules
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("scores");

//                    ref.setValue(1835,null);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> document = dataSnapshot.getChildren();
                            Iterator<DataSnapshot> iterator=document.iterator();
                            while (iterator.hasNext()){
                                String object=iterator.next().getValue().toString();
                                System.out.println(object);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });

                }
            });



    }

    void musica(String path){
        InputStream music;

        try {
            music = new FileInputStream(new File(path));
            AudioStream audios = new AudioStream(music);
            AudioPlayer.player.start(audios);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
