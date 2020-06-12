package pruebasMovimiento;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Configuraciones extends JPanel {

    JLabel background;
    static FloatControl gainControl;
    static Clip music;


    public Configuraciones(Menu menu, Clip clip, JPanel backgroundpanel, Image a) {


        background = new JLabel(new ImageIcon(a));
        background.setBounds(-6, -14, 512, 573);
        this.add(background);
        background.setLayout(null);


        JSlider slider = new JSlider();
        JLabel sonido = new JLabel("<html><font color='white'>Sonido</font></html>");
        sonido.setFont(new Font("Verdana", Font.BOLD, 30));
        sonido.setLocation(100, 100);
        sonido.setBounds(200, 125, 300, 300);
        sonido.setVisible(true);

        slider.setBounds(100, 300, 300, 30);
        slider.setOpaque(false);
        slider.setMinimum(0);//-74f
        slider.setMaximum(10); //6f
        slider.setMajorTickSpacing( 1 );
        slider.setPaintLabels( false );
        slider.setValue(((int) Menu.sound+74)/8);
        slider.addChangeListener(new MyChangeAction(0));
        slider.setVisible(true);
        background.add(sonido);
        background.add(slider);

        slider = new JSlider();
        JLabel musica = new JLabel("<html><font color='white'>Musica</font></html>");
        musica.setFont(new Font("Verdana", Font.BOLD, 30));
        musica.setLocation(100, 100);
        musica.setVisible(true);
        musica.setBounds(200, 25, 300, 300);


        slider.setBounds(100, 200, 300, 30);
        slider.setOpaque(false);
        slider.setMinimum(0);//-74f
        slider.setMaximum(10); //6f
        slider.setMajorTickSpacing( 1 );
        slider.setPaintLabels(false);
        slider.setValue(((int) Menu.music+74)/8);
        slider.addChangeListener(new MyChangeAction(1));
        slider.setVisible(true);
        background.add(musica);
        background.add(slider);


        JButton menuPrincipal = new JButton("Menu principal");
        menuPrincipal.setBounds(350, 20, 150, 35);
        background.add(menuPrincipal);

        menuPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                menu.remove(background);

                backgroundpanel.setVisible(true);
                Menu.panelPadre.setVisible(true);
                menu.setContentPane(backgroundpanel);

            }
        });


        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        repaint();


    }


    public class MyChangeAction implements ChangeListener {
        private int id;

        public MyChangeAction(int id) {
            this.id = id;
        }

        public void stateChanged(ChangeEvent ce) {
            JSlider slider=(JSlider) ce.getSource();
            if(id==0) {
                Menu.sound = slider.getValue() * 8 - 74;
                Entity.playSound("sounds/coin.wav");
            }else {
                Menu.music = slider.getValue() * 8 - 74;
                gainControl.setValue(Menu.music);
            }

            System.out.println("Sound: "+Menu.sound+" Music: "+Menu.music);

//            System.out.println(gainControl.getMaximum());
//            System.out.println(gainControl.getMinimum());
//            System.out.println(slider.getValue());

            // 10 -> 6
            // 0 -> -74
        }
    }


}
