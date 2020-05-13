package pruebasMovimiento;

import java.awt.*;

public abstract class ItemProperties {

    private String name;
    private Image img;
    private int spriteSize;
    private Image icon;
    private Rectangle hitbox;

    public ItemProperties(String name, int spriteSize, String img, Image icon, Rectangle hitbox) {
        this.name = name;
        this.spriteSize = spriteSize;
        this.img = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
        this.icon = icon;
        this.hitbox = hitbox;
    }

    public ItemProperties(String name, String img, int spriteSize, Image icon) {
        this.name = name;
        this.img = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
        this.spriteSize = spriteSize;
        this.icon = icon;
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

    public Rectangle getHitbox() {
        return hitbox;
    }
}
