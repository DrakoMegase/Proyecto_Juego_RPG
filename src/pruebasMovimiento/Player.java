package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Entity {

    private boolean switchImgEnano;
    Image image;
    int velX = 0;
    int velY = 0;
    Rectangle player;
    boolean retrocediendo;


    private static Rectangle lago = new Rectangle(196, 35, 90, 90);
    private static ArrayList<Rectangle> obstaculos = new ArrayList<>();


    public Player(int x, int y) {
        super(x, y);
        switchImgEnano = true;
        image = getPlayerImg();
        player = new Rectangle(0, 0, getPlayerImg().getWidth(null), getPlayerImg().getWidth(null));
        obstaculos.add(lago);

    }

    public void update() {



        for (Rectangle r : obstaculos
        ) {

            if (r.intersects(player)){
                retrocediendo =true;
               leaveRectangle(player, r);
               System.out.println("???");

            }

                y += velY;
                x += velX;


                player.x = x;
                player.y = y;



                if (velX != 0 || velY != 0) {
                    switchImgEnano = !switchImgEnano;
                }


                //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);


                if (x > 320)
                    x = 320;
                if (x < 0)
                    x = 0;
                if (y > 320)
                    y = 320;
                if (y < 0)
                    y = 0;


            }





        System.out.println(player.x + " " + player.y + " W " + player.width  + " h " + player.height + "VEL X = " +velX + " VEL Y = " + velY);


    }

    private void leaveRectangle(Rectangle player, Rectangle obstaculo) {

        if (retrocediendo){
            velY = velY * -1;
            velX = velX * -1;
            x += velX;
            y += velY;
            retrocediendo = false;

        }




    }

    public void draw(Graphics2D graphics2D) {

        graphics2D.drawImage(getPlayerImg(), x, y, null);

    }

    public Image getPlayerImg() {

        if (switchImgEnano) {
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
