package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Panel extends JPanel implements ActionListener {
    //No olvidarse de implementar ActionListener


    private Timer mainTimer;                //Declaracion de un timer
    private Player player;                  //Declaracion de un player
    private LinkedList<Entity> entities;
    private CollisionDetector collisionDetector;

     Panel() {
        setFocusable(true);                 //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        player = new Player(100, 100);

        collisionDetector=new CollisionDetector(player);

        entities=new LinkedList<>();
        entities.add(player);
        entities.add(new Entity(300,300,"src/pruebasMovimiento/img/Fate1.png",true));
        entities.add(new Entity(100,300,"src/pruebasMovimiento/img/Fate1.png",true));
        entities.add(new Entity(200,300,"src/pruebasMovimiento/img/Fate1.png",true));
        entities.add(new Entity(300,50,"src/pruebasMovimiento/img/enano.png",false));
        Entity ivanPrueba=new Entity(50,300,"src/pruebasMovimiento/img/IvanFelis.png",true);
//        ivanPrueba.velX=1;
        entities.add(ivanPrueba);

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

        if(collisionDetector.intersect!=null){
            graphics2D.draw(collisionDetector.intersect);
        }

    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Cada 20ms (depende el timer) se acutaliza el metodo update en player y se repainteara la pantalla
        collisionDetector.adjustPositions(entities);
        for(Entity entity:entities){
            entity.update();
        }

        repaint();


    }


}
