package pruebasMovimiento;

import java.awt.*;


class Portal extends Salida{
    private Image img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/portal/portal.png"));
    private Portal(Rectangle area) {
        super(area);
    }

    static Portal newPortal(int roomWidth,int roomHeight){
        return new Portal(new Rectangle(roomWidth/2-30,roomHeight/2-17,60,39));
    }
    void draw(Graphics2D graphics2D, int offSetX, int offSetY){

        int multySpriteX=(int)((System.currentTimeMillis()/100)%4);

        // Width and height of sprite
        int sw = 60;
        int sh = 39;
        // Position of sprite on screen
        int px = getArea().x - offSetX;
        int py = getArea().y - offSetY;
        // Coordinates of desired sprite image
        int i = 60*multySpriteX;
        int j = 0;
        graphics2D.drawImage(img, px,py, px+sw,py+sh, i, j, i+sw, j+sh, null);
    }
}
