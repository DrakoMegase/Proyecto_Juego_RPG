package pruebasMovimiento;



import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;

public class Player extends Entity {

    private int lastSpdX=0;
    private int lastSpdY=2;
    private Weapon[] weapons=new Weapon[2];
    private Armor[] armor=new Armor[3];
    static final Image ICONS =Toolkit.getDefaultToolkit().getImage(Weapon.class.getClassLoader().getResource("img/icons/icons.png"));
    private boolean canShoot=true;
    private int state=0;
    private int skill=0;
    private long startTime=0;
    private int energia;
    int experiencia;
    int puntuacion;
    int level = 1;
    Room salaPlayer;
    int dinero;






    Player(int x, int y, int hp) {
        super(x, y);
        img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/BODY_male.png"));

        this.hp=hp;

        this.name="Player";

        canBeMoved=true;
        canBeDamaged=true;
        hitbox=new Rectangle(x+22,y+46,20,16);

        weapons[0]=Weapon.createWeapon(0);
        weapons[1]=Weapon.createWeapon(5);
//        armor[0]=Armor.createArmor(3);
//        armor[1]=Armor.createArmor(7);
        armor[2]=Armor.createArmor(2);

        this.energia = level*3;
        this.experiencia = 0;
        this.dinero = 0;
    }

    public Player(int x, int y, int hp, Weapon[] weapons, Armor[] armor, int experiencia, int puntuacion, int level, int dinero) {
        super(x, y);
        this.weapons = weapons;
        this.armor = armor;
        this.experiencia = experiencia;
        this.puntuacion = puntuacion;
        this.level = level;
        this.energia = level*3;
        this.dinero = dinero;

        img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/BODY_male.png"));

        this.hp=hp;

        this.name="Player";

        canBeMoved=true;
        canBeDamaged=true;
        hitbox=new Rectangle(x+22,y+46,20,16);
    }

    private int getArmorInt() {
        int armorInt=0;
        for (int i = 0; i < 3; i++) {
            if(armor[i]!=null){
                armorInt+=armor[i].getArmor();
            }
        }

        return armorInt;
    }

    int getEnergia() {
        return energia;
    }

    int getExperiencia() {
        return experiencia;
    }

    int getLevel() {
        return level;
    }

    public void update() {

        if (experiencia >= getExpMax()) {
            experiencia = 0;
            level++;
            hp+=3;
            playSound("sounds/levelUp.wav");

        }

        if(damageWait&&System.currentTimeMillis()-damageTime>500){
            damageWait=false;
            damageTime=0;
        }

        if(state!=2&&energia<level*3&&System.currentTimeMillis()%20==0){
            energia++;
        }

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

    @Override
    void damage(int dmg) {
        super.damage((int)Math.round(dmg*getDmgRecived()));
//        System.out.println(dmg+"-"+(int)Math.round(dmg*getDmgRecived()));
    }

    int getMaxEnergy(){
        return level*4;
    }

    int getMaxHp(){
        return  20+level*4;
    }

    double getDmgRecived(){
        return (100.0 / (100.0 + (double) getArmorInt()));
    }

    int getExpMax(){
        return 100*level*(level/10+1);
    }

    private void skill() {

        long actionTime=System.currentTimeMillis()-startTime;

        if(skill==0) {
            if (actionTime >= 420) {
                playSound("sounds/skill.wav");
                state = 0;

                int damage=3*(1+(level-3)/4);

                int modX = Math.abs(lastSpdX) / 2;
                int modY = Math.abs(lastSpdY) / 2;


                String img = "img/projectiles/daga.png:2:0:0:64:64:1";

                int distance=20;

                salaPlayer.entities.add(new Projectile(x+distance*modY, y+distance*modX, 20, img, 30, 30 * modY + (hitbox.y - y) * modX, 4, 4, false, false, lastSpdX * 4, lastSpdY * 4, this, salaPlayer.entities, damage));
                salaPlayer.entities.add(new Projectile(x, y, 20, img, 30, 30 * modY + (hitbox.y - y) * modX, 4, 4, false, false, lastSpdX * 4, lastSpdY * 4, this, salaPlayer.entities, damage));
                salaPlayer.entities.add(new Projectile(x-distance*modY, y-distance*modX, 20, img, 30, 30 * modY + (hitbox.y - y) * modX, 4, 4, false, false, lastSpdX * 4, lastSpdY * 4, this, salaPlayer.entities, damage));
            }
        }else {
            if (actionTime >= 420) {
                state = 0;
                canShoot=true;

            }else if(canShoot&&actionTime>=360){
                playSound("sounds/skill.wav");

                System.out.println("lel");
                canShoot=false;

                int damage=3*(1+(level-3)/4);
                int vel=3;
                String img="img/projectiles/bola.png:1:0:0:29:29:4";
                for (int i = 0; i < 8; i++) {
                    int movX=-vel;
                    int movY=-vel;
                    if(i%4==0){
                        movX=0;
                    }else if(i-4<0){
                        movX*=-1;
                    }

                    if(i==2||i==6){
                        movY=0;
                    }else if(i>2&&i<6){
                        movY*=-1;
                    }
                    salaPlayer.entities.add(new Projectile(hitbox.x,hitbox.y-20,20, img,9,9,12,12,false,false,movX,movY,this,salaPlayer.entities,damage));
                }

            }
        }

    }

    private void slash() {


        long actionTime=System.currentTimeMillis()- startTime;
        int range= weapons[0].getAttackRange();
        int width= weapons[0].getAttackWidth();
        int knocback=5;

        if(actionTime>=120*weapons[0].getSpeed()){
            state=0;
            canShoot=true;
        }else if(actionTime>=(120*weapons[0].getSpeed())-((120*weapons[0].getSpeed())/6)*2){
            if(canShoot) {
                playSound("sounds/slash.wav");
                canShoot=false;
            }
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

            for (Entity entity:salaPlayer.entities) {
                if (!entity.equals(this)&&entity.hitbox.intersects(slash)){
                    entity.damage(weapons[0].getDamage());
                    entity.push(lastSpdX*knocback,lastSpdY*knocback);
                }
            }

        }


    }

    private void shoot(){

        long actionTime=System.currentTimeMillis()-startTime;

        if(actionTime>=200*weapons[1].getSpeed()){
            state=0;
            canShoot=true;
        }else if(canShoot&&actionTime>=180*weapons[1].getSpeed()){
            playSound("sounds/shoot.wav");
            int modY=Math.abs(lastSpdY)/2;
            int modX=Math.abs(lastSpdX)/2;
            String img="img/projectiles/flecha.png:2:0:0:64:64:1";
            salaPlayer.entities.add(new Projectile(x,y,20, img,30,30*modY+(hitbox.y-y)*modX,4,4,false,false,lastSpdX*4,lastSpdY*4,this,salaPlayer.entities,weapons[1].getDamage()));

            canShoot=false;
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
                multySpriteX=(int)(((System.currentTimeMillis()-startTime)/(20*weapons[0].getSpeed()))%6);
                break;
            case 2:
                multySpriteX=(int)(((System.currentTimeMillis()-startTime)/60)%7);
                break;
            case 3:
                multySpriteX=(int)(((System.currentTimeMillis()-startTime)/(20*weapons[1].getSpeed()))%10);
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
                if(state==0&&level>=2&&energia>=3){
                    state=2;
                    energia-=3;
                    skill=0;
                    startTime=System.currentTimeMillis();
                }
                break;

            case KeyEvent.VK_I:
                if(state==0&&level>=4&&energia>=6){
                    state=2;
                    energia-=6;
                    skill=1;
                    startTime=System.currentTimeMillis();
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

            case KeyEvent.VK_ESCAPE:

                Juego.menuEsc =! Juego.menuEsc;     //switch menu




                System.out.println("Escape" + Juego.menuEsc);

                break;

            case KeyEvent.VK_SPACE:
                ItemProperties item=nearItem();

                if(item!=null){
                    changeWeapon(item);
                }else if(salaPlayer.salidas.containsKey("portal")){
                    if(hitbox.intersects(salaPlayer.salidas.get("portal").getArea())){
                        Juego.siguienteNivel();
                    }
                }
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
                    if(velX!=0){
                        lastSpdY=0;
                        lastSpdX=velX;

                    }
                }

                    break;
            case KeyEvent.VK_S:
                if(velY>0) {
                    velY = 0;
                    if(velX!=0){
                        lastSpdY=0;
                        lastSpdX=velX;
                    }
                }
                break;
            case KeyEvent.VK_A:
                if(velX<0) {
                    velX = 0;
                    if(velY!=0){
                        lastSpdY=velY;
                        lastSpdX=0;
                    }
                }
                break;
            case KeyEvent.VK_D:
                if(velX>0) {
                    velX = 0;
                    if(velY!=0){
                        lastSpdY=velY;
                        lastSpdX=0;
                    }
                }
                break;
            case KeyEvent.VK_P:

                for (Entity e1:salaPlayer.entities
                     ) {

                    if (e1.hp > 0 && !e1.equals(this)){

                        e1.hp = 0;
                    }

                }
                velX = (int) (velX* 1.5);
                velY = (int) (velY* 1.5);
                hp += 10;

//                hp -= 100;

                break;

            default:

        }



    }

    Armor[] getArmor() {
        return armor;
    }

    Weapon[] getWeapons() {
        return weapons;
    }

    private void changeWeapon(ItemProperties objeto){

        if(objeto instanceof Buyable){
            Buyable buyable=(Buyable)objeto;
            if(buyable.price<=dinero){
                playSound("sounds/comprar.wav");
                salaPlayer.objetosMapa.remove(objeto);
                dinero-=buyable.price;
                salaPlayer.objetosMapa.add(buyable.item);
                changeWeapon(buyable.item);
            }

        }else {
            playSound("sounds/inventory.wav");
            Rectangle hitbox = (Rectangle) objeto.getHitbox().clone();

            if (objeto instanceof Weapon) {
                Weapon weapon = (Weapon) objeto;
                salaPlayer.objetosMapa.remove(weapon);
                weapons[weapon.getWeaponType()].setHitbox(hitbox);
                salaPlayer.objetosMapa.add(weapons[weapon.getWeaponType()]);
                weapons[weapon.getWeaponType()] = weapon;

            } else {
                Armor armor = (Armor) objeto;
                salaPlayer.objetosMapa.remove(armor);
                if(this.armor[armor.getSlot()]!=null) {
                    this.armor[armor.getSlot()].setHitbox(hitbox);
                    salaPlayer.objetosMapa.add(this.armor[armor.getSlot()]);
                }
                this.armor[armor.getSlot()] = armor;
            }
        }
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public int getDinero() {
        return dinero;
    }

    private ItemProperties nearItem(){

        ItemProperties item=null;
        for (int i = 0; i < salaPlayer.objetosMapa.size()&&item==null; i++) {
            ItemProperties item2=salaPlayer.objetosMapa.get(i);
            if(item2.getHitbox().intersects(hitbox)){
                item=item2;
            }
        }
        return item;
    }


}
