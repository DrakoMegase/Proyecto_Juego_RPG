package pruebasMovimiento;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Player extends Entity {

    private int lastSpdX=0;
    private int lastSpdY=2;
    LinkedList<Entity> addEntities;
    private int state=0;
    private long startTime =0;

    Player(int x, int y, int hp, LinkedList<Entity> addEntities) {
        super(x, y);
        img=getPlayerImg();

        this.hp=hp;

        this.name="Player";

        canBeMoved=true;
        hitbox=new Rectangle(x+22,y+46,20,16);

        this.addEntities = addEntities;
    }



    public void update() {


        switch (state) {
            case 0:
                move(velX, velY);
            //System.out.printf("\nvelX = " + velX + "\tvelY = " + velY +"\tposX = " + this.x +"\tposY = " + this.y);
                break;
            case 1:
                slash();
                break;
            case 2:
                shoot();
                break;
        }




    }

    private void slash() {
        long actionTime=System.currentTimeMillis()- startTime;
        velX=0;
        velY=0;
        int rangeX=20;
        int rangeY=30;
        int knocback=10;

        if(actionTime>=280){
            state=0;
        }else{
            int modSizeX=Math.abs(lastSpdX);
            int modSizeY=Math.abs(lastSpdY);

            int modPosX=0;
            int modPosY=-rangeY;
            if(lastSpdY<0){
                modPosY=hitbox.height+rangeY;
            }else if(lastSpdX>0){
                modPosY=0;
                modPosX=hitbox.width+rangeX;
            }else if(lastSpdX<0) {
                modPosY=0;
                modPosX=-rangeX;
            }

            Rectangle rectangle=new Rectangle(hitbox.x+modPosX,hitbox.y+modPosY,hitbox.width*modSizeY+rangeX*modSizeX,hitbox.height*modSizeX+rangeY*modSizeY);

            for (Entity entity:addEntities) {
                if (!entity.equals(this)&&entity.hitbox.intersects(rectangle)){
                    System.out.println("DAMAGESS");
                    entity.damage(10);
                    entity.push(lastSpdX*knocback,lastSpdY*knocback);
                }
            }
        }


    }

    private void shoot(){

        long actionTime=System.currentTimeMillis()- startTime;
        velX=0;
        velY=0;
        if(actionTime>=480){
            state=0;
            int shootX=hitbox.x+hitbox.width/3;
            int shootY=hitbox.y-hitbox.width*3;

            addEntities.add(new Projectile(shootX,shootY,20,"img/proyectil.png",16,18,32,32,true,false,lastSpdX*2,lastSpdY*2,this,addEntities));
            addEntities.add(new Projectile(shootX+lastSpdY*10,shootY+lastSpdX*10,20,"img/proyectil.png",16,18,32,32,true,false,lastSpdX*2,lastSpdY*2,this,addEntities));
            addEntities.add(new Projectile(shootX-lastSpdY*10,shootY-lastSpdX*10,20,"img/proyectil.png",16,18,32,32,true,false,lastSpdX*2,lastSpdY*2,this,addEntities));
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
        if(state==0&&(velX!=0||velY!=0)){
            multySpriteX=1+(int)((System.currentTimeMillis()/100)%8);
        }else if(state==1){
            multySpriteX=(int)((System.currentTimeMillis()-startTime)/40)%6;
        }else if(state==2){
            multySpriteX=(int)((System.currentTimeMillis()-startTime)/60)%7;
        }

        // Width and height of sprite
        int sw = 64;
        int sh = 64;
        // Position of sprite on screen
        int px = x - offSetX;
        int py = y - offSetY;
        // Coordinates of desired sprite image
        int i = 64*multySpriteX;
        int j = 256*state+64*multiSpriteY;
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
                if(state==0) {
                    velY = -2;
                    lastSpdX = 0;
                    lastSpdY = -2;
                }
                break;
            case KeyEvent.VK_S:
                if(state==0) {
                    velY = 2;
                    lastSpdX = 0;
                    lastSpdY = 2;
                }
                break;
            case KeyEvent.VK_A:
                if(state==0) {
                    velX = -2;
                    lastSpdX = -2;
                    lastSpdY = 0;
                }
                break;
            case KeyEvent.VK_D:
                if(state==0) {
                    velX = 2;
                    lastSpdX = 2;
                    lastSpdY = 0;
                }
                break;

            case KeyEvent.VK_J:
                if(state==0){
                    state=2;
                    startTime =System.currentTimeMillis();
                }
                break;

             case KeyEvent.VK_SPACE:
                 if(state==0){
                     state=1;
                     startTime =System.currentTimeMillis();
                 }
                break;

            case KeyEvent.VK_F:

                long tiempo = System.currentTimeMillis();
                setVelX(this.velX * 2);
                setVelY(this.velY * 2);

                //TODO
                System.out.println("aaa");


                break;

            default:


        }

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
