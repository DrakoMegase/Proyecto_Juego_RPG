package pruebasMovimiento;


import java.util.LinkedList;

public class Projectile extends Entity{

    private Entity creator;
    LinkedList<Entity> entities;
    int WIDTH = Juego.WIDTH;
    int HEIGHT = Juego.HEIGHT;



    public Projectile(int x, int y, int hp, String img, int hitX, int hitY, int hitWidth, int hitHeight, boolean canBeMoved, boolean canBeDamaged, int velX, int velY, Entity creator,    LinkedList<Entity> entities) {
        super(x, y, hp, img, hitX, hitY, hitWidth, hitHeight, canBeMoved, canBeDamaged);
        this.velX=velX;
        this.velY=velY;
        this.creator=creator;
        this.entities = entities;
    }

    public void update() {

        move(velX,velY);

        boolean hit=false;
        for (Entity entity:entities) {
            if (!entity.equals(this)&&!entity.equals(creator)&&!((entity instanceof Projectile)&&((Projectile)entity).creator.equals(creator))&&hitbox.intersects(entity.hitbox)){
                entity.damage(5);
                hit=true;
                int knockback=5;
                entity.push(velX*knockback,velY*knockback);
            }
        }

        if (hit||hitbox.x+hitbox.width <0||hitbox.x> WIDTH||hitbox.y+hitbox.height<0||hitbox.y > HEIGHT-hitbox.height) {
            remove=true;
        }

    }

    @Override
    protected void checkCollisions(LinkedList<Entity> entities, int count) {

    }
}
