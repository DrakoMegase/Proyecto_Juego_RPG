package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;

public class Player extends Entity {


    int pWidth,pHeight;


    public Player(int x, int y) {
        super(x, y);
        img=getPlayerImg();

        name="Player";

        canBeMoved=true;
        hitbox=createHitbox();

    }

    public void update() {

        move(velX,velY);
        //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);


        if (hitbox.x<0||hitbox.x+hitbox.width > 800) {
            move(-velX, 0);
        }
        if (hitbox.y<0||hitbox.y+hitbox.height > 600-hitbox.height) {
            move(0, -velY);
        }

    }

    public void draw(Graphics2D graphics2D) {

        graphics2D.drawImage(img, x, y, null);

    }

    public Image getPlayerImg() {

        ImageIcon imageIcon = new ImageIcon("src/pruebasMovimiento/img/ogro.png");     //Creamos una ImageIcon y le pasamos el recurso
        Image image=imageIcon.getImage();
        pHeight=image.getHeight(null);
        pWidth=image.getWidth(null);
        return imageIcon.getImage();                                                                      //La convertimos a imagen

    }

    @Override
    public Rectangle createHitbox(){
        int xMargin=img.getWidth(null)/6;
        int yMargin=img.getHeight(null)/2;
        int[] hitbox={xMargin,img.getWidth(null)-xMargin,yMargin,img.getHeight(null)};
        return new Rectangle(x+xMargin,y+yMargin,img.getWidth(null)-xMargin*2,yMargin);
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        switch (key) {

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
