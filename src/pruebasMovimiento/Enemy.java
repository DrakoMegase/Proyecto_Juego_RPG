package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Enemy extends Entity{

    private int time=0;
    private Player player;
    private int velMov;
    private int movPath;
    private int spinMult=1;

    public Enemy(int x, int y, int hp, String img, int hitX, int hitY, int hitWidth, int hitHeight, boolean canBeMoved, boolean canBeDamaged,Player player, int velMov, int movPath) {
        super(x, y, hp, img, hitX, hitY, hitWidth, hitHeight, canBeMoved, canBeDamaged);
        this.player=player;
        this.velMov=velMov;
        this.movPath=movPath;
    }

    public void update() {
        time+=1;
        move(velX,velY);
        //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);


        if (hitbox.x<0||hitbox.x+hitbox.width > Pantalla.WIDTH) {
            move(-velX, 0);
        }
        if (hitbox.y<0||hitbox.y+hitbox.height > Pantalla.HEIGHT-hitbox.height) {
            move(0, -velY);
        }

        adjustMovement();

        if(hp<=0){
            remove=true;
        }

    }

    @Override
    protected void checkCollisions(LinkedList<Entity> entities, int count){
        int[] force=null;
        for (Entity entity2:entities) {
            force=intersect(this,entity2);
            if (!this.equals(entity2)&&force!=null) {

                spinMult*=-1;
                if(entity2 instanceof Enemy){
                    ((Enemy) entity2).spinMult*=-1;
                }else if(entity2.canBeDamaged){
                    entity2.damage(5);
                }

                if (!entity2.push(force[0], force[1])) {

                    push(-force[0], -force[1]);
                    if(!(entity2 instanceof Player)){
                        if(force[0]!=0&&Math.abs(hitbox.x-player.hitbox.x)>Math.abs(hitbox.y-player.hitbox.y)){
                            push(0,velMov*2);
                        }else if(force[1]!=0&&Math.abs(hitbox.x-player.hitbox.x)<Math.abs(hitbox.y-player.hitbox.y)){
                            push(velMov*2,0);
                        }
                    }
                    if (count < 7){
                        count++;
                        for (int i=entities.indexOf(this);i>=0;i--) {
                            entities.get(i).checkCollisions(entities, count);
                        }
                    }else {
                        entity2.damage(5);
                        System.out.println("Doing dmg ++ ");
                    }
                }
            }
        }
    }

    public void adjustMovement() {
        switch (movPath){
            case 0:
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
                break;
            case 1:
                int distance=CompareNearEntities.distance(hitbox.x,hitbox.y,player.hitbox.x,player.hitbox.y);
                int enemyDist=50;
                int tolerancia=3;
                if(distance>enemyDist+tolerancia){
                    //System.out.println("lejos");
                    if(player.hitbox.x>hitbox.x){
                        velX=velMov;
                    }else{
                        velX=-velMov;
                    }

                    if(player.hitbox.y>hitbox.y){
                        velY=velMov;
                    }else{
                        velY=-velMov;
                    }
                }else if(distance<enemyDist-tolerancia){
                    //System.out.println("cerca");
                    if(player.hitbox.x>hitbox.x){
                        velX=-velMov;
                    }else{
                        velX=velMov;
                    }

                    if(player.hitbox.y>hitbox.y){
                        velY=-velMov;
                    }else {
                        velY=velMov;
                    }
                }else {
                    //System.out.println("medio "+velX+"-"+velY);
                    if(player.hitbox.x>=hitbox.x&&player.hitbox.y>=hitbox.y){
                        velY=-velMov*spinMult;
                    }else if(player.hitbox.x>=hitbox.x&&player.hitbox.y<hitbox.y){
                        velX=-velMov*spinMult;
                    }else if(player.hitbox.x<hitbox.x&&player.hitbox.y>=hitbox.y){
                        velX=velMov*spinMult;
                    }else if(player.hitbox.x<hitbox.x&&player.hitbox.y<hitbox.y){
                        velY=velMov*spinMult;
                    }

                }
        }
    }
}
