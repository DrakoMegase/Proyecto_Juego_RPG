package pruebasMovimiento;

import java.awt.*;

public class Armor extends ItemProperties {
    private String name;
    private int armor;
    private int slot; //0 cabeza, 1 Cuerpo, 2 Piernas
    private int spriteSize;
    private Image img;
    private Image icon;



    public Armor(String name, String img, int armor, int slot, int spriteSize) {
        super(name, img, spriteSize, null);
        this.name = name;
        this.armor = armor;
        this.slot = slot;
        this.spriteSize = spriteSize;

        this.img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
    }

    public void draw(Graphics2D graphics2D, int x,int y, int state, int multiSpriteX, int multiSpriteY) {


        // Width and height of sprite
        int sw = spriteSize;
        int sh = spriteSize;
        // Position of sprite on screen
        int px = x -(spriteSize-64)/2;
        int py = y -(spriteSize-64)/2;
        // Coordinates of desired sprite image
        int i = spriteSize*multiSpriteX;
        int j = 256*state+spriteSize*multiSpriteY;
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

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
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
