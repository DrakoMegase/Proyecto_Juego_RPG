package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Panel extends JPanel implements ActionListener {
    //No olvidarse de implementar ActionListener


    Timer mainTimer;                //Declaracion de un timer
    Player player;                  //Declaracion de un player

    public Panel() {
        setFocusable(true);                 //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        player = new Player(100, 100);

        addKeyListener(new KeyAdapt(player));



        mainTimer = new Timer(5, this);        //Cada 5 milisecons actualiza actionPerformed
        mainTimer.start();

    }


    public void paint(Graphics graphics) {

        super.paint(graphics);                                          //Referenciamos sobre que Panel tiene que trabajar
        Graphics2D graphics2D = (Graphics2D) graphics;                  //Casteo de Graphics a Graphics2D.      Graphics2D proporciona acceso a las características avanzadas de renderizado del API 2D de Java.

        player.draw(graphics2D);


    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Cada 20ms (depende el timer) se acutaliza el metodo update en player y se repainteara la pantalla
        player.update();
        repaint();


    }
}
