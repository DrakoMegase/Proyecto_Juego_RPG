package pruebasMovimiento;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Armor extends ItemProperties {
    private String name;
    private int armor;
    private int slot; //0 cabeza, 1 Cuerpo, 2 Piernas
    private int spriteSize;
    private Image img;


    private Armor(String name, int id, String img, int armor, int slot, int spriteSize) {
        super(name, spriteSize, img, null, new Rectangle(30,30));
        this.name = name;
        this.armor = armor;
        this.slot = slot;
        this.spriteSize = spriteSize;
        this.id=id;

        this.img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));

    }

    void draw(Graphics2D graphics2D, int x,int y, int state, int multiSpriteX, int multiSpriteY) {


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

    @Override
    public void drawIcon(Graphics2D graphics2D, int x,int y){
        // Width and height of sprite
        int sw = 30;
        int sh = 30;
        // Coordinates of desired sprite image
        int i = 17;
        int j = 140+14*slot;
        graphics2D.drawImage(img, x,y, x+sw,y+sh, i, j, i+sw, j+sh, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getArmor() {
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

    /*
     * id
     * 0=capucha cuero
     * 1=pechera cuero
     * 2=pantalones cuero
     * 3=casco cota de malla
     * 4=pechera cota de malla
     * 5=pantalones cota de malla
     * 6=casco acero
     * 7=pechera acero
     * 8=pantalones acero
     *
     */
    static Armor createArmor(int id){
        Armor armor=null;
        switch (id){
            case 0:
                armor=new Armor("Capucha Cuero",id, "img/armor/HEAD_robe_hood.png",3,0,64);
                break;
            case 1:
                armor=new Armor("Pechera Cuero",id, "img/armor/TORSO_leather_armor_torso.png",4,1,64);
                break;
            case 2:
                armor=new Armor("Falda Cuero",id, "img/armor/LEGS_robe_skirt.png",4,2,64);
                break;
            case 3:
                armor=new Armor("Casco Cota de Malla",id, "img/armor/HEAD_chain_armor_helmet.png",6,0,64);
                break;
            case 4:
                armor=new Armor("Pechera Cota de Malla",id, "img/armor/TORSO_chain_armor_torso.png",8,1,64);
                break;
            case 5:
                armor=new Armor("Pantalones Cota de Malla",id, "img/armor/LEGS_pants_greenish.png",8,2,64);
                break;
            case 6:
                armor=new Armor("Casco Acero",id, "img/armor/HEAD_plate_armor_helmet.png",12,0,64);
                break;
            case 7:
                armor=new Armor("Pechera Acero",id, "img/armor/TORSO_plate_armor_torso.png",16,1,64);
                break;
            case 8:
                armor=new Armor("Pantalones Acero",id, "img/armor/LEGS_plate_armor_pants.png",16,2,64);
                break;

        }

        return armor;

    }
}
