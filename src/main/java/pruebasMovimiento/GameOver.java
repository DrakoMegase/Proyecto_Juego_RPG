package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

class GameOver extends JPanel {

    private JLabel background;
    int WIDTH = 512;
    int HEIGHT = 573;
    JButton continuar;
    JLabel monsInt;
    JLabel playerLvInt;
    JLabel dineroInt;
    JLabel puntInt;
    JLabel puntFinalInt;
    JTextField tf4;
    JLabel tituloLabel;
    int puntuacionFinalInt;
    JLabel introduceNombre;
    JLabel backgroundLabel;

    boolean enviarPuntuaciones;


    public GameOver(int mounstruos, Player player, Menu menu) {

        setLayout(null);
        JPanel titulo = new JPanel();
        JPanel puntuaciones = new JPanel(new GridLayout(5, 2, 95, 8));
        puntuaciones.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        continuar = new JButton("CONTINUAR");
        continuar.setForeground(Color.WHITE);
        continuar.setOpaque(false);
        continuar.setContentAreaFilled(false);
        add(continuar);
        continuar.setBounds(WIDTH - 147, HEIGHT - 108, 110, 30);


        continuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (enviarPuntuaciones) {

                    if (tf4.getText().equals("Introduce tu nombre") || (tf4.getText().equals("") || (tf4.getText().equals("Nombre no valido.")) || tf4.getText().length() > 9)) {
                        tf4.setText("Nombre no valido. Max long 8 chars");
                    } else {

                        player.name = tf4.getText();
                        player.puntuacion = puntuacionFinalInt;

                        Firebase.uploadScore(player);
                        tituloLabel.setText("GRACIAS POR JUGAR");
                        tituloLabel.setFont(new Font("Verdana", Font.BOLD, 20));
                        continuar.setText("Creditos");

                        tf4.setVisible(false);
                        introduceNombre.setVisible(false);

                        remove(titulo);
                        remove(puntuaciones);
                        remove(backgroundLabel);
                        continuar.setForeground(Color.BLACK);
                        repaint();

                        JPanel creditos = new JPanel();
                        creditos.setBounds(0, 0, WIDTH, HEIGHT);
                        creditos.setVisible(true);

                        Image a = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/background.png")))
                                .getImage();

                        //todo foto creditos

                        JLabel backgroundLabel1 = new JLabel(new ImageIcon(a));
                        creditos.add(backgroundLabel1);


                        invalidate();
                        validate();
                        add(creditos);

                        continuar.removeActionListener(this);
                        continuar.setText("Menu principal");
                        continuar.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {


                                remove(continuar);
                                menu.add(Menu.panelPadre);
                                menu.setContentPane(Menu.panelPadre);
                                menu.add(Menu.backgroundPanel);
                                repaint();


                            }
                        });
                    }
                }


                if (!enviarPuntuaciones) {
                    puntuaciones.setVisible(false);
                    tf4.setVisible(true);
                    introduceNombre.setVisible(true);
                    enviarPuntuaciones = true;
                    Juego.mainTimer.stop();
                }


            }
        });


        titulo.setBounds(4, 35, (WIDTH - 20), 100);

        puntuaciones.setBounds(2, 175, WIDTH - 20, HEIGHT - titulo.getHeight() - 180);


        add(titulo);
        add(puntuaciones);

        titulo.setVisible(true);
        puntuaciones.setVisible(true);


        tituloLabel = new JLabel("FIN DEL JUEGO");
        tituloLabel.setForeground(Color.BLACK);
        tituloLabel.setFont(new Font("Verdana", Font.BOLD, 30));


        introduceNombre = new JLabel("<html>INTRODUCE<br/>TU NOMBRE</html>", SwingConstants.CENTER);
        introduceNombre.setFont(new Font("Verdana", Font.BOLD, 16));
        introduceNombre.setForeground(Color.white);
        introduceNombre.setBounds(15, 350, 180, 45);
        introduceNombre.setVisible(false);


        tf4 = new JTextField("Introduce tu nombre", 30);
        tf4.setBounds(20, 405, 180, 45);
        tf4.setVisible(false);
        tf4.setHorizontalAlignment(JTextField.CENTER);

        add(tf4);
        add(introduceNombre);


/////////////////////////////////////////////////////////////////////////////////////////////

        JLabel monstruosLabel = new JLabel("Monstruos asesinados");
        monstruosLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        monstruosLabel.setForeground(Color.white);
        monsInt = new JLabel(mounstruos + "");
        monsInt.setFont(new Font("Verdana", Font.BOLD, 14));


        JLabel lvLabel = new JLabel("Nivel jugador");
        lvLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        lvLabel.setForeground(Color.white);
        playerLvInt = new JLabel(player.getLevel() + "");
        playerLvInt.setFont(new Font("Verdana", Font.BOLD, 14));

        JLabel dineroLabel = new JLabel("Dinero");
        dineroLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        dineroLabel.setForeground(Color.white);
        dineroInt = new JLabel(player.getDinero() + "");
        dineroInt.setFont(new Font("Verdana", Font.BOLD, 14));


        JLabel puntIntLabel = new JLabel("Puntuacion interna");
        puntIntLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        puntIntLabel.setForeground(Color.white);
        puntInt = new JLabel(player.getPuntuacion() + "");
        puntInt.setFont(new Font("Verdana", Font.BOLD, 14));


        JLabel puntFinal = new JLabel("PUNTUACION FINAL");
        puntFinal.setFont(new Font("Verdana", Font.BOLD, 15));
        puntFinal.setForeground(Color.BLACK);
        puntuacionFinalInt = puntFinal(mounstruos, player.getLevel(), player.getDinero(), player.getPuntuacion());
        puntFinalInt = new JLabel(puntuacionFinalInt + "");
        puntFinalInt.setFont(new Font("Verdana", Font.BOLD, 18));
        puntFinalInt.setForeground(Color.BLACK);


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

    private int puntFinal(int a, int b, int c, int d) {

        // a = monstruos asesinados
        // b = nivel player
        // c = dinero player
        // d = punt interna

        if (c <= 0) c = 1;


        double v = d + (a * b + Math.log(c));

        Random random=new Random();

        if (v <= 0) {

            return random.nextInt(10);
        }


        return (int) v;


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
