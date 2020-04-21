package mapasPruebas2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player extends Entity {


    private static Rectangle lago = new Rectangle(0,0,100,100);
    private static ArrayList<Rectangle>obstaculos = new ArrayList<>();

    private boolean switchImgEnano;
    Image image;
    int velX = 0;
    int velY = 0;

    public Player(int x, int y) {
        super(x, y);
        switchImgEnano = true;


    }

    public void update() {

        y += velY;
        x += velX;

        if (velX != 0 || velY != 0){
            switchImgEnano =! switchImgEnano;
        }

        //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);

        if (x > 800)
            x = 800;
        if (x < 0)
            x = 0;
        if (y > 600)
            y = 600;
        if (y < 0)
            y = 0;

        System.out.println(x + " " + y);


    }

    public void draw(Graphics2D graphics2D) {

        graphics2D.drawImage(getPlayerImg(), x, y, null);

    }

    public Image getPlayerImg() {

        if (switchImgEnano){
            ImageIcon imageIcon = new ImageIcon("src/pruebasMovimiento/img/Enano 1.png");     //Creamos una ImageIcon y le pasamos el recurso
            return imageIcon.getImage();
        }


        ImageIcon imageIcon = new ImageIcon("src/pruebasMovimiento/img/Enano 2.png");     //Creamos una ImageIcon y le pasamos el recurso
        return imageIcon.getImage();                                                                      //La convertimos a imagen

    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        switch (key) {
//
            case KeyEvent.VK_W:
                velY = -2;
                break;
            case KeyEvent.VK_S:
                velY = 2;
                break;
            case KeyEvent.VK_A:
                velX = -2;
                break;
            case KeyEvent.VK_D:
                velX = 2;
                break;

            default:


        }


    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();


        switch (key) {

            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
                velY = 0;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                velX = 0;
                break;

            default:

        }

    }

}
