package pruebasMovimiento;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;


public class NuevaPartida extends JPanel {

    private final static String savesfolder="saves";

    JLabel background;

    public NuevaPartida(Menu menu, JPanel jp1, JPanel jp2) {


        Image a = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/guardarCargarBackground.png")))
                .getImage();
        background = new JLabel(new ImageIcon(a));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(background);

        NuevaPartida nuevaPartida=this;

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton jButton= (JButton) e.getSource();
                String text=jButton.getText();

                menu.remove(background);
                menu.remove(nuevaPartida);
                menu.loadGame(Integer.parseInt(text.substring(text.length()-1)));
            }
        };

        File file;
        for (int i = 1; i < 4; i++) {
            file=new File(savesfolder+"/save"+i+".json");
            if(file.exists()) {
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
