package pruebasMovimiento;


import java.awt.*;
import java.util.Comparator;
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

        CompareNearPlayer comparator=new CompareNearPlayer(player);
        entityLinkedList.sort(comparator);


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

    class CompareNearPlayer implements Comparator<Entity> {

        private Player player;

        public CompareNearPlayer(Player player) {
            this.player = player;
        }

        @Override
        public int compare(Entity o1, Entity o2) {

            return distance(o1.hitbox.x,o1.hitbox.y,player.hitbox.x,player.hitbox.y)-distance(o2.hitbox.x,o2.hitbox.y,player.hitbox.x,player.hitbox.y);
        }

        private int distance(int x1, int y1, int x2, int y2){
            return (int)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
        }
    }
}
