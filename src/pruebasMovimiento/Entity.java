package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Entity implements Comparable<Entity>{

    Image img;
    boolean canBeMoved,remove=false;
    int x;
    int y;
    int velX = 0;
    int velY = 0;
    int hp;
    boolean canBeDamaged;
    int[] spritesPos;
    Rectangle hitbox;
    String name;
    private static int count=0;


    Entity(int x, int y, int hitX, int hitY, int hitWidth, int hitHeight) {
        this.x = x;
        this.y = y;
    }

    Entity(int x, int y ) {
        this.x = x;
        this.y = y;
    }

    Entity(int x, int y, int hp, String img, int hitX, int hitY, int hitWidth, int hitHeight, boolean canBeMoved, boolean canBeDamaged) {
        this.x = x;
        this.y = y;

        this.hp=hp;

        this.canBeDamaged=canBeDamaged;

        name="Entity: "+count++;

        if(img.contains(":")){
            /**
             * img:tipo:xSprite:ySprite:width:height
             * tipo 0=imagen unica, 1=animacion unica, 2=animaciones para cada direccion
             *
             */
            String[] split=img.split(":");
            this.img=getImg(split[0]);
            spritesPos=new int[5];
            for (int i = 0; i < spritesPos.length; i++) {
                spritesPos[i]=Integer.parseInt(split[i+1]);
            }

        }else {
            this.img = getImg(img);
        }
        this.canBeMoved=canBeMoved;


        hitbox=new Rectangle(x+hitX,y+hitY,hitWidth,hitHeight);


    }

    void damage(int dmg){
        if(canBeDamaged){
            hp-=dmg;
        }
    }


    public void update(){

    }


    boolean push(int x, int y){
        int newX=hitbox.x+x;
        int newY=hitbox.y+y;

        if(canBeMoved&&!outOfBounds(newX,newY)){
            move(x,y);
        }

        return canBeMoved&&!outOfBounds(newX,newY);
    }

    private boolean outOfBounds(int x, int y){
        return x<0 || x>800-hitbox.width||y<0||y>600-hitbox.height;
    }

    private Image getImg(String img) {

        Image pic = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));

        return pic;

    }

    public void draw(Graphics2D graphics2D){
        if(img!=null) {

            if(spritesPos!=null) {
                drawAnimation(graphics2D);
            }else {
                graphics2D.drawImage(img, x, y, null);
            }
        }
    }

    void drawAnimation(Graphics2D graphics2D){
        int multiOr=0;
        if(spritesPos[0]>1) {
            if (velY < 0) {
                multiOr = 1;
            } else if (velX > 0) {
                multiOr = 2;
            } else if (velX < 0) {
                multiOr = 3;
            }
        }

        int multyMov=0;
        if(spritesPos[0]!=0&&(velX!=0||velY!=0)){
            multyMov=(int)Math.abs(System.currentTimeMillis()/150)%3;
        }

        // Width and height of sprite
        int sw = spritesPos[3];
        int sh = spritesPos[4];
        // Position of sprite on screen
        int px = x;
        int py = y;
        // Coordinates of desired sprite image
        int i = spritesPos[1]+sw*multyMov;
        int j = spritesPos[2]+sh*multiOr;
        graphics2D.drawImage(img, px,py, px+sw,py+sh, i, j, i+sw, j+sh, null);
    }

    void move(int x, int y){
        this.x+=x;
        this.y+=y;
        hitbox.translate(x,y);

    }


    @Override
    public int compareTo(Entity o) {
        return this.hitbox.y-o.hitbox.y;
    }


    protected void checkCollisions( LinkedList<Entity> entities){
        int[] force=null;
        for (Entity entity2:entities) {
            if(!this.equals(entity2) && !(entity2 instanceof Projectile)) {
                force = intersect(this, entity2);
                if ( force != null) {
                    if (!entity2.push(force[0], force[1])) {
                        this.push(-force[0], -force[1]);
                        if (force[0] == 0) {
                            this.velX = 0;
                        } else {
                            this.velY = 0;
                        }

                        for (int i = entities.indexOf(this); i >= 0; i--) {
                            entities.get(i).checkCollisions(entities);
                        }
                    }
                }
            }
        }
    }

    protected static int[] intersect(Entity p, Entity e) {

        int[] force=null;

        if (p.hitbox.intersects(e.hitbox)){
            Rectangle intersect = p.hitbox.intersection(e.hitbox);

            force=new int[2];

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
        return force;
    }

}
