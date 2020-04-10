package pruebasMovimiento;

import java.util.LinkedList;

public class CollisionDetector {

    public static void adjustPositions(Player player, LinkedList<Entity> entities){
        LinkedList<Entity> entityLinkedList=new LinkedList<Entity>(entities);
        entityLinkedList.remove(player);
        for (Entity entity:entities){
            if(intersect(player,entity)){
                if(!entity.push(player.velX,player.velY)){
                    player.push(-player.velX,-player.velY);
                    player.velX=0;
                    player.velY=0;
                }
            }
        }
    }

    private static boolean intersect(Player p, Entity e){
        int[] pHitbox=p.hitbox;
        int[] eHitbox=e.hitbox;

        boolean colision=false;

        for (int i = 0; i < 4 && !colision ; i++) {
            int cornerX=p.x+pHitbox[i%2];
            int cornerY=p.y+pHitbox[2+i/2];
            colision=(cornerX>eHitbox[0]+e.x&&cornerX<eHitbox[1]+e.x)&&(cornerY>eHitbox[2]+e.y&&cornerY<eHitbox[3]+e.y);
        }


        return colision;
    }


}
