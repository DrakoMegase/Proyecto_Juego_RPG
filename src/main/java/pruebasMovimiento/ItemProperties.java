package pruebasMovimiento;

import java.awt.*;

public abstract class ItemProperties {

    private String name;
    private Image img;
    private Rectangle hitbox;
    int id;

    ItemProperties(){}

    ItemProperties(String name, String img,Rectangle hitbox) {
        this.name = name;
        this.img = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
        this.hitbox = hitbox;
    }

    public String getName() {
        return name;
    }

    public Image getImg() {
        return img;
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

    @Override
    public String toString() {
        return id+"";
    }
}
