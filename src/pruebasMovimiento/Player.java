package pruebasMovimiento;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Player extends Entity {

    private int lastSpdX=0;
    private int lastSpdY=2;
    LinkedList<Entity> addEntities;
    private int state=0;
    private int time=0;

    Player(int x, int y, int hp, LinkedList<Entity> addEntities) {
        super(x, y);
        img=getPlayerImg();

        this.hp=hp;

        this.name="Player";

        canBeMoved=true;
        hitbox=new Rectangle(x+21,y+51,22,10);

        this.addEntities = addEntities;
    }



    public void update() {

        move(velX,velY);
        //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);


        if (hitbox.x<=0||hitbox.x+hitbox.width >= Pantalla.WIDTH) {
            move(-velX, 0);
        }

        if (hitbox.y<=0||hitbox.y+hitbox.height >= Pantalla.HEIGHT-hitbox.height) {
            move(0, -velY);
        }



    }

    public void draw(Graphics2D graphics2D, int offSetX, int offSetY) {
        int multiSpriteY=2;
        if(lastSpdY<0){
            multiSpriteY=0;
        }else if(lastSpdX>0){
            multiSpriteY=3;
        }else if(lastSpdX<0){
            multiSpriteY=1;
        }

        int multySpriteX=0;
        if(velX!=0||velY!=0){
            multySpriteX=(int)Math.abs(System.currentTimeMillis()/150)%9;
        }

        // Width and height of sprite
        int sw = 64;
        int sh = 64;
        // Position of sprite on screen
        int px = x - offSetX;
        int py = y - offSetY;
        // Coordinates of desired sprite image
        int i = 64*multySpriteX;
        int j = 64*state+64*multiSpriteY;
        graphics2D.drawImage(img, px,py, px+sw,py+sh, i, j, i+sw, j+sh, null);

    }

    private Image getPlayerImg() {

        Image pic = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/BODY_male.png"));
        return pic;                                                                      //La convertimos a imagen

    }

    public Rectangle createHitbox(){
        int xMargin=img.getWidth(null)/6;
        int yMargin=img.getHeight(null)/2;
        int[] hitbox={xMargin,img.getWidth(null)-xMargin,yMargin,img.getHeight(null)};
        return new Rectangle(x+xMargin,y+yMargin,img.getWidth(null)-xMargin*2,yMargin);
    }

    void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        switch (key) {
//
            case KeyEvent.VK_W:
                velY = -2;
                lastSpdX=0;
                lastSpdY=-2;
                break;
            case KeyEvent.VK_S:
                velY = 2;
                lastSpdX=0;
                lastSpdY=2;
                break;
            case KeyEvent.VK_A:
                velX = -2;
                lastSpdX=-2;
                lastSpdY=0;
                break;
            case KeyEvent.VK_D:
                velX = 2;
                lastSpdX=2;
                lastSpdY=0;
                break;

            case KeyEvent.VK_SPACE:
                shoot();
                break;

            default:


        }

    }

    private void shoot(){
        int shootX=hitbox.x-hitbox.width*3;
        int shootY=hitbox.y-hitbox.width*3;

        addEntities.add(new Projectile(shootX,shootY,20,"img/proyectil.png",16,18,32,32,true,false,lastSpdX*2,lastSpdY*2,this, addEntities));
        addEntities.add(new Projectile(shootX+lastSpdY*10,shootY+lastSpdX*10,20,"img/proyectil.png",16,18,32,32,true,false,lastSpdX*2,lastSpdY*2,this, addEntities));
        addEntities.add(new Projectile(shootX-lastSpdY*10,shootY-lastSpdX*10,20,"img/proyectil.png",16,18,32,32,true,false,lastSpdX*2,lastSpdY*2,this, addEntities));
    }


    void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();


        switch (key) {

            case KeyEvent.VK_W:
                if(velY<0) {
                    velY = 0;
                }
                break;
            case KeyEvent.VK_S:
                if(velY>0) {
                    velY = 0;
                }
                break;
            case KeyEvent.VK_A:
                if(velX<0) {
                    velX = 0;
                }
                break;
            case KeyEvent.VK_D:
                if(velX>0) {
                    velX = 0;
                }
                break;

            default:

        }



    }


}
