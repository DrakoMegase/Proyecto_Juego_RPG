package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Enemy extends Entity{

    private int time=0;
    private Player player;
    private int velMov=0;

    public Enemy(int x, int y, int hp, String img, int hitX, int hitY, int hitWidth, int hitHeight, boolean canBeMoved, boolean canBeDamaged) {
        super(x, y, hp, img, hitX, hitY, hitWidth, hitHeight, canBeMoved, canBeDamaged);
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

    }

    public void draw(Graphics2D graphics2D) {

        graphics2D.drawImage(img, x, y, null);

    }

    @Override
    protected void checkCollisions(LinkedList<Entity> entities){
        int[] force=null;
        for (Entity entity2:entities) {
            force=intersect(this,entity2);
            if (!this.equals(entity2)&&force!=null) {
                if(entity2.canBeDamaged){
                    entity2.damage(5);
                }

                if (!entity2.push(force[0], force[1])) {
                    this.push(-force[0], -force[1]);
                    for (int i=entities.indexOf(this);i>=0;i--) {
                        entities.get(i).checkCollisions(entities);
                    }
                }
            }
        }
    }

}
