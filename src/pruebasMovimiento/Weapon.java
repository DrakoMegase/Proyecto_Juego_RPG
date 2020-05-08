package pruebasMovimiento;

import java.awt.*;

public class Weapon {
    private String name;
    private int weaponType; //0=Melee 1=Ranged
    private int attackRange;
    private int attackWidth;
    private int damage;
    private int spriteSize;
    private Image img;
    private Image icon;

    public Weapon(String name, String img, int spriteSize, int attackRange, int attackWidth, int damage) {
        this.name = name;
        this.attackRange = attackRange;
        this.attackWidth = attackWidth;
        this.damage = damage;
        weaponType=0;
        this.spriteSize=spriteSize;
        this.img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
    }

    public Weapon(String name, String img, int spriteSize, int damage) {
        this.name = name;
        this.damage = damage;
        this.spriteSize = spriteSize;
        this.img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
        weaponType=1;
    }

    public void draw(Graphics2D graphics2D, int x, int y, int multiSpriteX, int multiSpriteY) {

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

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getAttackWidth() {
        return attackWidth;
    }

    public void setAttackWidth(int attackWidth) {
        this.attackWidth = attackWidth;
    }

    public int getDamage() {
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

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }
}
