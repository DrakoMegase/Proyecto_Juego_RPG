package pruebasMovimiento;

import java.awt.*;

public class Weapon extends ItemProperties{
    private String name;
    private int weaponType; //0=Melee 1=Ranged
    private int attackRange;
    private int attackWidth;
    private int damage;
    private int speed;
    private int spriteSize;
    private Image img;
    private static final Image ARROW =Toolkit.getDefaultToolkit().getImage(Weapon.class.getClassLoader().getResource("img/weapons/WEAPON_arrow.png"));



    private Weapon(String name, int id, String img, int spriteSize, int attackRange, int attackWidth, int damage, int speed, Image icon) {
        super(name, spriteSize, img, icon, new Rectangle(30,30));
//        super(name, spriteSize, img, icon, new Rectangle(icon.getWidth(null),icon.getHeight(null)));
        this.name = name;
        this.attackRange = attackRange;
        this.attackWidth = attackWidth;

        this.speed=speed;

        this.id=id;

        this.damage = damage;
        weaponType=0;
        this.spriteSize=spriteSize;
        this.img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
    }

    private Weapon(String name, int id, String img, int spriteSize, int damage, int speed, Image icon) {
        super(name, spriteSize, img, icon, new Rectangle(30,30));
//        super(name, spriteSize, img, icon, new Rectangle(icon.getWidth(null),icon.getHeight(null)));
        this.name = name;
        this.damage = damage;
        this.spriteSize = spriteSize;
        this.img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
        weaponType=1;
        this.speed=speed;

        this.id=id;

    }



    void draw(Graphics2D graphics2D, int x, int y, int multiSpriteX, int multiSpriteY) {
        if(weaponType==1) {
            // Width and height of sprite
            int sw = 64;
            int sh = 64;
            // Coordinates of desired sprite image
            int i = 64 * multiSpriteX;
            int j = 64 * multiSpriteY;
            graphics2D.drawImage(ARROW, x, y, x + sw, y + sh, i, j, i + sw, j + sh, null);
        }

        // Width and height of sprite
        int sw = spriteSize;
        int sh = spriteSize;
        // Position of sprite on screen
        int px = x -(spriteSize-64)/2;
        int py = y -(spriteSize-64)/2;
        // Coordinates of desired sprite image
        int i = spriteSize*multiSpriteX;
        int j = spriteSize*multiSpriteY;
        graphics2D.drawImage(img, px,py, px+sw,py+sh, i, j, i+sw, j+sh, null);

    }

    public void drawIcon(Graphics2D graphics2D, int x,int y){
        // Width and height of sprite
        int sw = 30;
        int sh = 30;
        // Coordinates of desired sprite image
        int i = 30*id;
        int j = 0;
        graphics2D.drawImage(Player.ICONS, x,y, x+sw,y+sh, i, j, i+sw, j+sh, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    int getAttackWidth() {
        return attackWidth;
    }

    public void setAttackWidth(int attackWidth) {
        this.attackWidth = attackWidth;
    }

    int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSpriteSize() {
        return spriteSize;
    }

    public void setSpriteSize(int spriteSize) {
        this.spriteSize = spriteSize;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    /*
     *  id
     *  0=daga
     *  1=estoque
     *  2=espadalarga
     *  3=maza
     *  4=sable
     *  5=arco
     *  6=arco curvo
     *  7=arco largo
     */
    static Weapon createWeapon(int id){
        Weapon weapon=null;

        switch (id){
            case 0:
                weapon=new Weapon("Dagita",id,"img/weapons/WEAPON_dagger.png",64,20,20,3,1,null);
                break;
            case 1:
                weapon=new Weapon("Estoque",id,"img/weapons/WEAPON_rapier.png",192,42,78,6,3,null);
                break;
            case 2:
                weapon=new Weapon("Espada Larga",id,"img/weapons/WEAPON_longsword.png",192,42,78,16,4,null);
                break;
            case 3:
                weapon=new Weapon("Maza",id,"img/weapons/mace_sheet.png",128,20,20,14,4,null);
                break;
            case 4:
                weapon=new Weapon("Sable",id,"img/weapons/sabre_sheet.png",128,20,20,10,1,null);
                break;
            case 5:
                weapon=new Weapon("Arco",id,"img/weapons/WEAPON_bow.png",64,2,2,null);
                break;
            case 6:
                weapon=new Weapon("Arco Curvo",id,"img/weapons/recurvebow_sheet.png",128,6,2,null);
                break;
            case 7:
                weapon=new Weapon("Arco Largo",id,"img/weapons/greatbow.png",192,16,4,null);
                break;
        }

        return weapon;
    }

    int getWeaponType() {
        return weaponType;
    }
}
