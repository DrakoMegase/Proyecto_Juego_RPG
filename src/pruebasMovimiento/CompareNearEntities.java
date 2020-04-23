package pruebasMovimiento;

import java.util.Comparator;
import java.util.LinkedList;

class CompareNearEntities implements Comparator<Entity> {

    private LinkedList<Entity> entities;

    public CompareNearEntities(LinkedList<Entity> entities) {

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
        if(o2 instanceof Player || o2 instanceof Enemy){
            return 1;
        }else if(o1 instanceof Player || o1 instanceof Enemy){
            return -1;
        }
        return maxDistanceToEntities(o1)- maxDistanceToEntities(o2);
    }

    private int maxDistanceToEntities(Entity o1){
        int dist=-1;
        for (Entity entity:entities){
            int dist1=distance(o1.hitbox.x,o1.hitbox.y,entity.hitbox.x,entity.hitbox.y);
            if(dist==-1||dist>dist1){
                dist=dist1;
            }
        }
        return dist;
    }

    static int distance(int x1, int y1, int x2, int y2){
        return (int)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }
}
