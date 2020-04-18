package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.LinkedList;

public class Player extends Entity {



    Player(int x, int y, int hp, LinkedList<Entity> roomEntities) {
        super(x, y, roomEntities);
        img=getPlayerImg();

        this.hp=hp;

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

    private Image getPlayerImg() {

        ImageIcon imageIcon = new ImageIcon("src/pruebasMovimiento/img/notHitler.png");     //Creamos una ImageIcon y le pasamos el recurso
        return imageIcon.getImage();                                                                      //La convertimos a imagen

    }

    @Override
    public Rectangle createHitbox(){
        int xMargin=img.getWidth(null)/6;
        int yMargin=img.getHeight(null)/2;
        int[] hitbox={xMargin,img.getWidth(null)-xMargin,yMargin,img.getHeight(null)};
        return new Rectangle(x+xMargin,y+yMargin,img.getWidth(null)-xMargin*2,yMargin);
    }

    void keyPressed(KeyEvent e) {

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

            case KeyEvent.VK_SPACE:
                shoot();
                break;

            default:


        }

    }

    private void shoot(){
        new Projectile(hitbox.x-hitbox.width/4,hitbox.y-hitbox.width/2,20,(int)(Math.random()*5)-2,(int)(Math.random()*5)-2,"src/pruebasMovimiento/img/proyectil.png",true,false,roomEntities,this);
    }


    void keyReleased(KeyEvent e) {
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
