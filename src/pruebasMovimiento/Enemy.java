package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Enemy extends Entity{

    private int time=0;
    private Player player;
    private int velMov=0;


    Enemy(int x, int y, int velMov,int hp, Player player, LinkedList<Entity> entities) {
        super(x, y, entities);

        img=getEnemyImg();

        this.hp=hp;
        this.player=player;
        name="Enemy";

        this.velMov=velMov;

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

        if(player.hitbox.x>hitbox.x){
            velX=velMov;
        }else if(player.hitbox.x<hitbox.x){
            velX=-velMov;
        }else {
            velX=0;
        }

        if(player.hitbox.y>hitbox.y){
            velY=velMov;
        }else if(player.hitbox.y<hitbox.y){
            velY=-velMov;
        }else {
            velY=0;
        }
        for (Entity entity:roomEntities) {
            if (!entity.equals(this)&&hitbox.intersects(entity.hitbox)){
                Rectangle intersection=hitbox.intersection(entity.hitbox);
                entity.damage(5);
                if(!entity.canBeMoved) {
                    if (intersection.height > intersection.width) {
                        move(-velX, 0);
                    } else if (intersection.height == intersection.width) {

                        if (Math.abs(player.hitbox.x - hitbox.x) > Math.abs(player.hitbox.y - hitbox.y)) {
                            move(-velX, 0);
                        } else {
                            move(0, -velY);
                        }

                    } else {
                        move(0, -velY);
                    }
                }

            }
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
