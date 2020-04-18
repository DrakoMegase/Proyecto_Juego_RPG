package pruebasMovimiento;


import java.awt.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

class CollisionDetector {

    private int[] force=new int[2];
    Rectangle intersect;

    void adjustPositions(LinkedList<Entity> entities){


        LinkedList<Entity> entityLinkedList=new LinkedList<>(entities);
        Iterator<Entity> iterator=entityLinkedList.iterator();
        Entity entity=null;
        while (iterator.hasNext()){
            entity=iterator.next();
            if(entity instanceof Projectile){
                iterator.remove();
            }
        }

        CompareNearPlayer comparator=new CompareNearPlayer(entityLinkedList);
        entityLinkedList.sort(comparator);



        for (Entity entity1:entityLinkedList){
            checkCollisions(entity1,entityLinkedList);
        }
    }

    private void checkCollisions(Entity entity1, LinkedList<Entity> entities){
        for (Entity entity2:entities) {
            if (!entity1.equals(entity2)&&intersect(entity1, entity2)) {
                if (!entity2.push(force[0], force[1])) {
                    entity1.push(-force[0], -force[1]);
                    if(force[0]==0) {
                        entity1.velX = 0;
                    }else {
                        entity1.velY = 0;
                    }

                    for (int i=entities.indexOf(entity1);i>=0;i--) {
                        checkCollisions(entities.get(i), entities);
                    }
                }
            }
        }
    }

    private boolean intersect(Entity p, Entity e) {

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

        private LinkedList<Entity> entities;

        private CompareNearPlayer(LinkedList<Entity> entities) {

            LinkedList<Entity> subentities=new LinkedList<>();
            for (Entity entity:entities){
                if(entity instanceof Player || entity instanceof Enemy){
                    subentities.add(entity);
                }
            }

            this.entities = subentities;
        }

        @Override
        public int compare(Entity o1, Entity o2) {
            if(o2 instanceof Player){
                return 1;
            }else if(o1 instanceof Player){
                return -1;
            }
            return sumOfDistanceToEntities(o1)-sumOfDistanceToEntities(o2);
        }

        private int sumOfDistanceToEntities(Entity o1){
            int dist=-1;
            for (Entity entity:entities){
                int dist1=distance(o1.hitbox.x,o1.hitbox.y,entity.hitbox.x,entity.hitbox.y);
                if(dist==-1||dist>dist1){
                    dist=dist1;
                }
            }
            return dist;
        }

        private int distance(int x1, int y1, int x2, int y2){
            return (int)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
        }
    }
}
