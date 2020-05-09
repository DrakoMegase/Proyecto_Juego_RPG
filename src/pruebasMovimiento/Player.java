package pruebasMovimiento;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Player extends Entity {

    private int lastSpdX=0;
    private int lastSpdY=2;
    private LinkedList<Entity> addEntities;
    private Weapon[] weapons=new Weapon[2];
    private Armor[] armor=new Armor[3];
    private int state=0;
    private long startTime=0;
    private long tiempo=0;
    private int armorInt;
    private int energia;
    protected int experiencia;
    protected int level = 1;
    protected Room salaPlayer;

    Player(int x, int y, int hp, LinkedList<Entity> addEntities) {
        super(x, y);
        img=getPlayerImg();

        this.hp=hp;

        this.name="Player";

        canBeMoved=true;
        hitbox=new Rectangle(x+22,y+46,20,16);

        weapons[0]=new Weapon("Dagita","img/weapons/WEAPON_dagger.png",64,20,30,10);
        weapons[1]=new Weapon("Arco de Madera","img/weapons/WEAPON_bow.png",64,10);
//        weapons=new Weapon("Estoque","img/weapons/WEAPON_rapier.png",192,42,78,15);
//        weapons=new Weapon("Espada Larga", "img/weapons/WEAPON_longsword.png",192,42,78,15);
        armor[0]=new Armor("Casco Cota de Malla", "img/armor/HEAD_chain_armor_helmet.png",3,0,64);
        armor[1]=new Armor("Pechera Cota de Malla", "img/armor/TORSO_chain_armor_torso.png",3,1,64);
        armor[2]=new Armor("Pantalones Cota de Malla", "img/armor/LEGS_pants_greenish.png",3,2,64);

        this.addEntities = addEntities;
        this.armorInt = 100;
        this.energia = 10;
        this.experiencia = 0;

        canBeDamaged=true;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
                skill();
                break;
            case 3:
                shoot();
                break;
        }




    }

    private void slash() {
        long actionTime=System.currentTimeMillis()- startTime;
        int range= weapons[0].getAttackRange();
        int width= weapons[0].getAttackWidth();
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
                    entity.damage(weapons[0].getDamage());
                    entity.push(lastSpdX*knocback,lastSpdY*knocback);
                }
            }
        }


    }

    private void shoot(){

        long actionTime=System.currentTimeMillis()- startTime;

        if(actionTime>=480){
            state=0;

            int modX=Math.abs(lastSpdX)/2;
            int modY=Math.abs(lastSpdY)/2;


            String img="img/projectiles/flecha.png:2:0:0:64:64:1";


            addEntities.add(new Projectile(x,y,20, img,30,30*modY+(hitbox.y-y)*modX,4,4,true,false,lastSpdX*4,lastSpdY*4,this,addEntities,weapons[1].getDamage()));
        }
    }


    private void skill(){
        long actionTime=System.currentTimeMillis()- startTime;

        if(actionTime>=480){
            state=0;

            int modX=Math.abs(lastSpdX)/2;
            int modY=Math.abs(lastSpdY)/2;
            String img="img/projectiles/bola.png:1:0:0:64:64:4";

            int mod=20;

            addEntities.add(new Projectile(x+mod,y+mod,20, img,20,21,25,25,true,false,lastSpdX*2,lastSpdY*2,this,addEntities,weapons[1].getDamage()));
            addEntities.add(new Projectile(x-mod,y+mod,20, img,20,21,25,25,true,false,lastSpdX*2,lastSpdY*2,this,addEntities,weapons[1].getDamage()));
            addEntities.add(new Projectile(x+mod,y-mod,20, img,20,21,25,25,true,false,lastSpdX*2,lastSpdY*2,this,addEntities,weapons[1].getDamage()));
            addEntities.add(new Projectile(x-mod,y-mod,20, img,20,21,25,25,true,false,lastSpdX*2,lastSpdY*2,this,addEntities,weapons[1].getDamage()));

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

        switch (state) {
            case 0:
                if(velX!=0||velY!=0) {
                    multySpriteX=1+(int)((System.currentTimeMillis()/100)%8);
                }
                break;
            case 1:
                multySpriteX=(int)((System.currentTimeMillis()-startTime)/40)%6;
                break;
            case 2:
                multySpriteX=(int)((System.currentTimeMillis()-startTime)/60)%7;
                break;
            case 3:
                multySpriteX=(int)((System.currentTimeMillis()-startTime)/60)%13;
                break;
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

        switch (state){
            case 1:
                weapons[0].draw(graphics2D,px,py,multySpriteX,multiSpriteY);
                break;
            case 3:
                weapons[1].draw(graphics2D,px,py,multySpriteX,multiSpriteY);
                break;
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

            case KeyEvent.VK_U:
                if(state==0){
                    state=2;
                    startTime =System.currentTimeMillis();
                }
                break;

            case KeyEvent.VK_J:
                if(state==0){
                    state=1;
                    startTime =System.currentTimeMillis();
                }
                break;

            case KeyEvent.VK_K:
                if(state==0){
                    state=3;
                    startTime =System.currentTimeMillis();
                }
                break;

            case KeyEvent.VK_M:

                UI.map =! UI.map;
                break;

            case KeyEvent.VK_F:



                //hp -= 1;
                energia -=2;
                experiencia += 1;
                //TODO sistema niveles.
                //move(this.velX * 8, this.velY * 8);
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

    public LinkedList<Entity> getAddEntities() {
        return addEntities;
    }

    public void setAddEntities(LinkedList<Entity> addEntities) {
        this.addEntities = addEntities;
    }
}
