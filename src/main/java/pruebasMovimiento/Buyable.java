package pruebasMovimiento;

import java.awt.*;

public class Buyable extends ItemProperties{

    ItemProperties item;
    int price;

    Buyable(ItemProperties item) {
        this.item = item;

        setHitbox(item.getHitbox());

        if(item instanceof Armor){
            Armor armor=(Armor)item;
            price=armor.getArmor()*4;
        }else {
            Weapon weapon=(Weapon)item;
            price=weapon.getDamage()*4;
        }
    }

    @Override
    public void drawIcon(Graphics2D graphics2D, int x, int y) {

        graphics2D.setFont(new Font("TimesRoman", Font.BOLD, 10));
        graphics2D.drawString(price + "", x-10, y-20);
        graphics2D.setStroke(graphics2D.getStroke());
        item.drawIcon(graphics2D,x,y);

    }
}
