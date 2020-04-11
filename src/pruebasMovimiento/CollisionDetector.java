package pruebasMovimiento;


import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class CollisionDetector {

    private int[] force=new int[2];
    private Player player;
    public Rectangle intersect;

    public CollisionDetector(Player player) {
        this.player = player;
    }

    public void adjustPositions(LinkedList<Entity> entities){

        LinkedList<Entity> entityLinkedList=new LinkedList<>(entities);
        entityLinkedList.remove(player);

        checkCollisions(player,entityLinkedList);


        for (Entity entity1:entityLinkedList){
            checkCollisions(entity1,entities);
        }
    }

    private boolean checkCollisions(Entity entity1, LinkedList<Entity> entities){
        boolean intersects=false;
        for (Entity entity2:entities) {
            if (!entity1.equals(entity2)&&intersect(entity1, entity2)) {
                intersects=true;
                if (!entity2.push(force[0], force[1])) {
                    System.out.println("i'm weak");
                    entity1.push(-force[0], -force[1]);
                    if(force[0]==0) {
                        entity1.velX = 0;
                    }else {
                        entity1.velY = 0;
                    }

                }else {
                    checkCollisions(entity1,entities);
                }
            }
        }
        return intersects;
    }

    public boolean intersect(Entity p, Entity e) {

        if (p.hitbox.intersects(e.hitbox)){
            intersect = p.hitbox.intersection(e.hitbox);

            if(intersect.x==e.hitbox.x){
                force[0]=intersect.width;
            }else {
                force[0]=-intersect.width;
            }

            if(intersect.y==e.hitbox.y){
                force[1]=intersect.height;
            }else {
                force[1]=-intersect.height;
            }

            if(intersect.width>intersect.height){
                force[0]=0;
            }else {
                force[1]=0;
            }
        }
        return p.hitbox.intersects(e.hitbox);
    }


}
