package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;

public class Player extends Entity {

    int velX = 0;
    int velY = 0;
    int pWidth,pHeight;


    public Player(int x, int y) {
        super(x, y);
        img=getPlayerImg();

        canBeMoved=true;
        hitbox=createHitbox();

    }

    public void update() {

        y += velY;
        x += velX;

        //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);


        if (x > 800-pWidth)
            x = 800-pWidth;
        if (x < 0)
            x = 0;
        if (y > 600-pHeight)
            y = 600-pHeight;
        if (y < 0)
            y = 0;

//        System.out.println(x + " " + y);


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
    public int[] createHitbox(){
        int xMargin=img.getWidth(null)/6;
        int yMargin=img.getHeight(null)/2;
        int[] hitbox={xMargin,img.getWidth(null)-xMargin,yMargin,img.getHeight(null)};
        return hitbox;
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

    public boolean push(int x, int y){
        int newX=this.x+x;
        int newY=this.y+y;

        System.out.println(x+"-"+y);

        if(canBeMoved&&!outOfBounds(newX,newY)){
            this.x = newX;
            this.y = newY;
        }

        return canBeMoved&&!outOfBounds(newX,newY);
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
