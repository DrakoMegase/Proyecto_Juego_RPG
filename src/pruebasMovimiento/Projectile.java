package pruebasMovimiento;

import java.awt.*;
import java.util.LinkedList;

public class Projectile extends Entity{

    Entity creator;

    Projectile(int x, int y, int hp, int velX, int velY, String img, boolean canBeMoved, boolean canBeDamaged, LinkedList<Entity> roomEntities, Entity creator) {
        super(x, y, hp, img, canBeMoved, canBeDamaged, roomEntities);
        this.velX=velX;
        this.velY=velY;
        this.creator=creator;
    }

    public void update() {

        move(velX,velY);
        //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);
        boolean hit=false;
        for (Entity entity:roomEntities) {
            if (!entity.equals(this)&&!entity.equals(creator)&&hitbox.intersects(entity.hitbox)){
                Rectangle intersection=hitbox.intersection(entity.hitbox);
                entity.damage(5);
                hit=true;
            }
        }

        if (hit||hitbox.x+hitbox.width <0||hitbox.x> 800||hitbox.y+hitbox.height<0||hitbox.y > 600-hitbox.height) {
            remove=true;
        }

    }
}
