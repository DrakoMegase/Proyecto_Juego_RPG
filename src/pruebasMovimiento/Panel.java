package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

public class Panel extends JPanel implements ActionListener {
    //No olvidarse de implementar ActionListener


    private Timer mainTimer;                //Declaracion de un timer
    private Player player;                  //Declaracion de un player
    private LinkedList<Entity> entities;
    private CollisionDetector collisionDetector;

    Panel() {

        setFocusable(true);                 //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        entities=new LinkedList<>();
        collisionDetector=new CollisionDetector();

        player = new Player(100, 100, 20,entities);

        new Entity(200,300,10,"src/pruebasMovimiento/img/Fate1.png",true,true,entities);
        new Entity(300,50,1,"src/pruebasMovimiento/img/enano.png",false,false,entities);
//        new Enemy(300,350,1,15,player,entities);

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
        Iterator<Entity> iterator=entities.iterator();
        while (iterator.hasNext()){
            Entity entity=iterator.next();
            entity.update();
            if(entity.remove){
                iterator.remove();
            }
        }
        collisionDetector.adjustPositions(entities);


        repaint();


    }


}
