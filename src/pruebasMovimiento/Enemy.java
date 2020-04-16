package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;

public class Enemy extends Entity{

    private int time=0;

    Enemy(int x, int y) {
        super(x, y);

        img=getEnemyImg();

        name="Player";

        canBeMoved=true;
        hitbox=createHitbox();
    }

    public void update() {
        time+=1;
        move(velX,velY);
        //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);


        if (hitbox.x<0||hitbox.x+hitbox.width > 800) {
            move(-velX, 0);
        }
        if (hitbox.y<0||hitbox.y+hitbox.height > 600-hitbox.height) {
            move(0, -velY);
        }

        if(time%400==300){
            velX=1;
            velY=0;

        }else if(time%400==200){
            velX=0;
            velY=1;

        }else if(time%400==100){
            velX=-1;
            velY=0;

        }else if(time%400==0){
            velX=0;
            velY=-1;

        }
    }

    public void draw(Graphics2D graphics2D) {

        graphics2D.drawImage(img, x, y, null);

    }

    private Image getEnemyImg() {

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
}
