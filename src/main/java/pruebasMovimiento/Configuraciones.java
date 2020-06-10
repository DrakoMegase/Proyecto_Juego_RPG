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

import static pruebasMovimiento.Menu.panelPadre;
import static pruebasMovimiento.Menu.sound;

public class Configuraciones extends JPanel {

    JLabel background;
    JSlider slider;
    static FloatControl gainControl;


    public Configuraciones(Menu menu, Clip clip, JPanel backgroundpanel, Image a) {


        background = new JLabel(new ImageIcon(a));
        background.setBounds(-6, -14, 512, 573);
        this.add(background);
        background.setLayout(null);


        slider = new JSlider();
        JLabel sonido = new JLabel("<html><font color='white'>Sonido</font></html>");
        sonido.setFont(new Font("Verdana", Font.BOLD, 30));
        sonido.setLocation(100, 100);
        sonido.setBounds(200, 25, 300, 300);
        sonido.setVisible(true);


        slider.setBounds(100, 200, 300, 30);
        slider.setOpaque(false);
        slider.setMinimum((int) -80f);
        slider.setMaximum((int) 6.02f);
        slider.setValue((int) sound);
        slider.addChangeListener(new MyChangeAction());
        slider.setVisible(true);
        background.add(sonido);
        background.add(slider);


        JButton menuPrincipal = new JButton("Menu principal");
        menuPrincipal.setBounds(350, 20, 150, 35);
        background.add(menuPrincipal);

        menuPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                menu.remove(background);

                backgroundpanel.setVisible(true);
                panelPadre.setVisible(true);
                menu.setContentPane(backgroundpanel);

            }
        });


        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        repaint();


    }


    public class MyChangeAction implements ChangeListener {
        public void stateChanged(ChangeEvent ce) {
            String str;
            sound = slider.getValue();

//            label.setText(str);
//            strCommand = new String("mixerctl -q outputs.master=" + str + "," + str);
            gainControl.setValue(sound); // Reduce volume by 10 decibels.
//            System.out.println(gainControl.getMaximum());
//            System.out.println(gainControl.getMinimum());
//            System.out.println(slider.getValue());

            // 255 -> 6.02
            // 1 -> -80
        }
    }


}
