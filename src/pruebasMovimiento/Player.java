package pruebasMovimiento;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Player extends Entity {

    private int lastSpdX=0;
    private int lastSpdY=2;
    private LinkedList<Entity> addEntities;
    private Weapon weapon;
    private Armor[] armor=new Armor[3];
    private int state=0;
    private long startTime=0;
    private long tiempo=0;
    private int armorInt;
    private int energia;
    private int experiencia;


    Player(int x, int y, int hp, LinkedList<Entity> addEntities) {
        super(x, y);
        img=getPlayerImg();

        this.hp=hp;

        this.name="Player";

        canBeMoved=true;
        hitbox=new Rectangle(x+22,y+46,20,16);

        weapon=new Weapon("Dagita","img/weapons/WEAPON_dagger.png",64,20,30,10);
//        weapon=new Weapon("Estoque","img/weapons/WEAPON_rapier.png",192,42,78,15);
//        weapon=new Weapon("Estoque", "img/weapons/WEAPON_longsword.png",192,42,78,15);
        armor[0]=new Armor("Casco Cota de Malla", "img/armor/HEAD_chain_armor_helmet.png",3,0,64);
        armor[1]=new Armor("Pechera Cota de Malla", "img/armor/TORSO_chain_armor_torso.png",3,1,64);
        armor[2]=new Armor("Pantalones Cota de Malla", "img/armor/LEGS_pants_greenish.png",3,2,64);

        this.addEntities = addEntities;
        this.armorInt = 100;
        this.energia = 10;
        this.experiencia = 0;
    }

    public int getArmorInt() {
        return armorInt;
    }

    public int getEnergia() {
        return energia;
    }

    public int getExperiencia() {
        return experiencia;
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
        int range=weapon.getAttackRange();
        int width=weapon.getAttackWidth();
        int knocback=5;

        if(actionTime>=280){
            state=0;
        }else{
            int modSizeX=Math.abs(lastSpdX)/2;
            int modSizeY=Math.abs(lastSpdY)/2;

            int modPosX=-(width-hitbox.width)/2;
            int modPosY=hitbox.height;
            if(lastSpdY<0){
                modPosY=-range;
            }else if(lastSpdX>0){
                modPosY=-(width-hitbox.height)/2;
                modPosX=hitbox.width;
            }else if(lastSpdX<0) {
                modPosY=-(width-hitbox.height)/2;
                modPosX=-range;
            }

            Rectangle slash=new Rectangle(hitbox.x+modPosX,hitbox.y+modPosY,width*modSizeY+range*modSizeX,width*modSizeX+range*modSizeY);
            Juego.slash=slash;

            for (Entity entity:addEntities) {
                if (!entity.equals(this)&&entity.hitbox.intersects(slash)){
                    System.out.println("DAMAGESS");
                    entity.damage(weapon.getDamage());
                    entity.push(lastSpdX*knocback,lastSpdY*knocback);
                }
            }
        }


    }

    private void shoot(){

        long actionTime=System.currentTimeMillis()- startTime;

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

        for (int k = armor.length-1; k >= 0; k--) {
            if(armor[k]!=null){
                armor[k].draw(graphics2D,px,py,state,multySpriteX,multiSpriteY);
            }
        }

        if(state==1){
            weapon.draw(graphics2D,px,py,multySpriteX,multiSpriteY);
        }

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



                hp -= 1;
                energia -=2;
                experiencia += 1;
                //TODO sistema niveles.
                move(this.velX * 8, this.velY * 8);
                //TODO



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
