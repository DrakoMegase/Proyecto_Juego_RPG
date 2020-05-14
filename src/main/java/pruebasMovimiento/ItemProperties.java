package pruebasMovimiento;

import java.awt.*;

public abstract class ItemProperties {

    private String name;
    private Image img;
    private int spriteSize;
    private Image icon;
    private Rectangle hitbox;
    int id;

    ItemProperties(String name, int spriteSize, String img, Image icon, Rectangle hitbox) {
        this.name = name;
        this.spriteSize = spriteSize;
        this.img = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
        this.icon = icon;
        this.hitbox = hitbox;
    }

    public String getName() {
        return name;
    }

    public int getSpriteSize() {
        return spriteSize;
    }

    public Image getImg() {
        return img;
    }

    public Image getIcon() {
        return icon;
    }

    Rectangle getHitbox() {
        return hitbox;
    }

    void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public abstract void drawIcon(Graphics2D graphics2D, int x, int y);

    void drawIcon(int offSetX, int offSetY,Graphics2D graphics2D){
        drawIcon(graphics2D,hitbox.x-offSetX,hitbox.y-offSetY);
    }
}
