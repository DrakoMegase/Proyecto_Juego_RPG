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

public class Menu extends JFrame implements KeyListener {



    public static void main(String[] args) {

        Menu game = new Menu();
        game.setVisible(true);



    }

    public Menu() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(1,1,500,529);
        JPanel panelPadre = new JPanel();
        panelPadre.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panelPadre);
        panelPadre.setLayout(null);


        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBounds(0, 0, 501, 500);
        panelPadre.add(backgroundPanel);
        backgroundPanel.setLayout(null);

        JButton btnNewButton = new JButton("New game");
        btnNewButton.setBounds(322, 112, 89, 23);
        backgroundPanel.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                remove(panelPadre);
                remove(backgroundPanel);
                repaint();


                Juego juego = new Juego("res/jsonsMapasPruebas/1.json", "resources/terrain_atlas.png");
                setContentPane(juego);
                setFocusable(true);
                validate();
                juego.setVisible(true);
                //TODO LAS PUTAS TECLAAAAAAAAAAAAAAAAAAS
                setFocusTraversalKeysEnabled(false);
                addKeyListener(juego);
                juego.start();
//                setIconImage(new ImageIcon("res/img/icon.png").getImage());    //Define el icono


            }

        });



        JLabel backgroundLabel;
        try {
            backgroundLabel = new JLabel(new ImageIcon(ImageIO.read(new File("res/img/interfazmenu/primerapantalla.png"))));
            backgroundLabel.setBounds(0, 0, 500, 500);
            backgroundPanel.add(backgroundLabel);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println(e.getKeyChar());
        System.out.println("aaaa");
    }
}
