package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Enemy extends Entity{

    Player player;
    int velMov;
    int movPath;
    protected int id;
    private long time;
    private int spinMult=1;
    int damage;
    int exp;

    Enemy(int x, int y, int hp, String img, int hitX, int hitY, int hitWidth, int hitHeight, boolean canBeMoved, boolean canBeDamaged,Player player, int velMov, int movPath, int damage, int id) {
        super(x, y, hp, img, hitX, hitY, hitWidth, hitHeight, canBeMoved, canBeDamaged);
        this.player=player;
        this.velMov=velMov;
        this.movPath=movPath;
        this.damage=damage;
        this.id=id;
        this.exp=hp;
    }

    public void update() {
        move(velX,velY);
        //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);

        if(damageWait&&System.currentTimeMillis()-damageTime>300){
            damageWait=false;
            damageTime=0;
        }

        if (hitbox.x<0||hitbox.x+hitbox.width > Juego.WIDTH) {
            move(-velX, 0);
        }
        if (hitbox.y<0||hitbox.y+hitbox.height > Juego.HEIGHT-hitbox.height) {
            move(0, -velY);
        }

        adjustMovement();


        if(hp<=0){
            remove=true;
            player.experiencia+=exp;
            player.dinero+=exp/2;
            player.puntuacion+=exp;
            playSound("sounds/coin.wav");
        }

    }

    @Override
    public String toString() {
        return "Enemy{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    protected void checkCollisions(LinkedList<Entity> entities, int count){
        int[] force;
        for (Entity entity2:entities) {
            force=intersect(this.hitbox,entity2.hitbox);
            if (!this.equals(entity2)&&!(entity2 instanceof Projectile)&&force!=null) {

                spinMult*=-1;
                if(entity2 instanceof Enemy){
                    ((Enemy) entity2).spinMult*=-1;
                }else if(entity2.canBeDamaged){
                    entity2.damage(damage);
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
                    }
                }
            }
        }
    }

    void adjustMovement() {
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
                int enemyDist=150;
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
                    }else if(player.hitbox.x>=hitbox.x){
                        velX=-velMov*spinMult;
                    }else if(player.hitbox.y>=hitbox.y){
                        velX=velMov*spinMult;
                    }else{
                        velY=velMov*spinMult;
                    }
                }

                if(System.currentTimeMillis()-time>2500){
                    int movX=0;
                    int movY=0;
                    int velPro=velMov+1;

                    time=System.currentTimeMillis();

                    if(player.hitbox.x>hitbox.x){
                        movX=velPro;
                    }else if(player.hitbox.x<hitbox.x){
                        movX=-velPro;
                    }

                    if(player.hitbox.y>hitbox.y){
                        movY=velPro;
                    }else if(player.hitbox.y<hitbox.y){
                        movY=-velPro;
                    }

                    Projectile projectile;
                    String img;
                    playSound("sounds/enemyShoot.wav");
                    switch (id){
                        case 3:
                            img="img/projectiles/bolaMarron.png:1:0:0:64:64:4";
                            projectile=new Projectile(hitbox.x,hitbox.y-20,20, img,20,21,25,25,false,false,movX,movY,this,player.salaPlayer.entities,damage);
                            break;
                        case 5:
                        case 6:
                        case 7:
                            img="img/projectiles/bolaRoja.png:1:0:0:64:64:4";
                            projectile=new Projectile(hitbox.x,hitbox.y-20,20, img,20,21,25,25,false,false,movX,movY,this,player.salaPlayer.entities,damage);
                            break;
                        default:
                            img="img/projectiles/bola.png:1:0:0:29:29:4";
                            projectile=new Projectile(hitbox.x,hitbox.y-20,20, img,9,9,12,12,false,false,movX,movY,this,player.salaPlayer.entities,damage);
                    }



                    player.salaPlayer.entities.add(projectile);

                }

                break;
            case 2:
                if(System.currentTimeMillis()/3000%2==0){
                    movPath=0;
                }else {
                    movPath=1;
                }
                adjustMovement();
                movPath=2;
        }
    }

    /*
    * 0:slime
    * 1:golem
    * 2:araña
    * 3:escarabajo
    * 4:calabera
    * 5:caballero
    * 6:demonio
    * 7:murcielago gigante
    * 8:mago del bosque
    * */
    static Enemy createEnemy(int id, int posX, int posY, Player player){

        Enemy enemy=null;

        switch (id){
            case 0:
                enemy=new Enemy(posX, posY, 9, "img/enemies/enemies.png:1:48:0:16:16:3", 3, 11, 10, 5, true, true, player, 1, 0, 2, id);
                break;
            case 1:
                enemy=new Enemy(posX, posY, 15, "img/enemies/enemies.png:2:0:64:32:32:3", 9, 22, 14, 9, true, true, player, 1, 0, 3, id);
                break;
            case 2:
                enemy=new Enemy(posX, posY, 18, "img/enemies/spider11.png:2:0:0:64:64:7", 23, 29, 16, 16, true, true, player, 1, 0, 5, id);
                break;
            case 3:
                enemy=new Enemy(posX, posY, 18, "img/enemies/beetle49.png:2:0:0:49:49:5", 18, 21, 14, 14, true, true, player, 1, 1, 5, id);
                break;
            case 4:
                enemy=new Enemy(posX, posY, 16, "img/enemies/enemies.png:2:192:0:16:32:3", 3, 26, 9, 5, true, true, player, 1, 2, 5, id);
                break;
            case 5:
                enemy=new Enemy(posX, posY, 22, "img/enemies/darksoldier.png:2:0:0:64:64:6", 25, 46, 14, 15, true, true, player, 1, 2, 5, id);
                break;
            case 6:
                enemy=new Boss(posX, posY, 80, "img/bosses/demon.png:2:0:0:128:128:4", 52, 60, 23, 27, true, true, player, 1, 2, 5, id);
                break;
            case 7:
                enemy=new Boss(posX, posY, 40, "img/bosses/bat.png:2:0:0:96:96:3", 33, 64, 30, 19, true, true, player, 1, 2, 3, id);
                break;
            case 8:
                enemy=new Boss(posX, posY, 60, "img/bosses/mage-1.png:2:0:0:32:32:3", 8, 24, 15, 7, true, true, player, 1, 2, 3, id);
                break;



        }


        return enemy;
    }
}
