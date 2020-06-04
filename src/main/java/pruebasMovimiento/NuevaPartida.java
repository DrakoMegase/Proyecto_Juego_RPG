package pruebasMovimiento;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NuevaPartida extends JPanel {

    JLabel background;

    public NuevaPartida(Menu menu, JPanel jp1, JPanel jp2) {


        Image a = new ImageIcon(getClass().getClassLoader().getResource("img/guardarCargarBackground.png"))
                .getImage();
        background = new JLabel(new ImageIcon(a));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(background);

        Border emptyBorder = BorderFactory.createEmptyBorder();

        JButton g1 = new JButton("GAME 1");
        g1.setForeground(Color.white);
        g1.setBounds(57, 220, 80, 70);
        background.add(g1);
        g1.setOpaque(false);
        g1.setContentAreaFilled(false);

        g1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menu.remove(background);
                menu.add(jp1);
                menu.add(jp2);
                menu.setContentPane(jp1);


                setFocusable(true);
                remove(Menu.panelPadre);
                remove(menu.backgroundPanel);
                repaint();


                Menu.juego = GuardarPartida.loadSave(1,menu);
                Menu.juego.start();

                menu.setContentPane(Menu.juego);
                Menu.juego.setVisible(true);
                validate();

                addKeyListener(new KeyAdapt(Juego.player));
                System.out.println("aaaaaaaaaaaa");
            }
        });


        JButton g2 = new JButton("GAME 2");
        g2.setForeground(Color.white);
        g2.setBounds(217, 220, 80, 70);
        background.add(g2);
        g2.setOpaque(false);
        g2.setContentAreaFilled(false);

        JButton g3 = new JButton("GAME 3");
        g3.setForeground(Color.black);
        g3.setBounds(377, 220, 80, 70);
        background.add(g3);
        g3.setOpaque(false);
        g3.setContentAreaFilled(false);



        JButton menuPrincipal = new JButton("Menu principal");
        menuPrincipal.setBounds(350, 20, 150, 35);
        background.add(menuPrincipal);
        menuPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                menu.remove(background);
                menu.add(jp1);
                menu.add(jp2);
                menu.setContentPane(jp1);

            }
        });

        repaint();


    }

    public static void main(String[] args) {


    }

}
