package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Panel extends JPanel implements ActionListener {
    //No olvidarse de implementar ActionListener


    Timer mainTimer;                //Declaracion de un timer
    Player player;                  //Declaracion de un player
    LinkedList<Entity> entities;

    public Panel() {
        setFocusable(true);                 //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        player = new Player(100, 100);
        entities=new LinkedList<Entity>();

        entities.add(player);
        entities.add(new Entity(50,300,"src/pruebasMovimiento/img/Fate1.png",true));
        entities.add(new Entity(300,50,"src/pruebasMovimiento/img/enano.png",false));

        addKeyListener(new KeyAdapt(player));



        mainTimer = new Timer(5, this);        //Cada 20 milisecons actualiza actionPerformed
        mainTimer.start();

    }


    public void paint(Graphics graphics) {

        super.paint(graphics);                                          //Referenciamos sobre que Panel tiene que trabajar
        Graphics2D graphics2D = (Graphics2D) graphics;                  //Casteo de Graphics a Graphics2D.      Graphics2D proporciona acceso a las características avanzadas de renderizado del API 2D de Java.


        entities.sort(Entity::compareTo);
        for(Entity entity:entities){
            entity.draw(graphics2D);
        }

    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Cada 20ms (depende el timer) se acutaliza el metodo update en player y se repainteara la pantalla
        player.update();
        CollisionDetector.adjustPositions(player,entities);


        repaint();


    }


}
