package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;

public class Entity implements Comparable<Entity>{

    Image img;
    boolean canBeMoved;
    int x;
    int y;
    int velX = 0;
    int velY = 0;
    Rectangle hitbox;
    String name;
    private static int count=0;

    Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Entity(int x, int y, String img, boolean canBeMoved) {
        this.x = x;
        this.y = y;

        name="Entity: "+count++;

        this.img=getImg(img);
        this.canBeMoved=canBeMoved;

        hitbox=createHitbox();

    }


    public void update(){

    }

    public Rectangle createHitbox(){
        int xMargin=img.getWidth(null)/4;
        int yMargin=img.getHeight(null)/2;
        int[] hitbox={xMargin,img.getWidth(null)-xMargin,yMargin,img.getHeight(null)};
        return new Rectangle(x+xMargin, y+yMargin,xMargin*2,yMargin);
    }


    boolean push(int x, int y){
        int newX=hitbox.x+x;
        int newY=hitbox.y+y;

        System.out.println("Han empujado a "+name+" "+x+"-"+y);

        if(canBeMoved&&!outOfBounds(newX,newY)){
            move(x,y);
        }

        return canBeMoved&&!outOfBounds(newX,newY);
    }

    private boolean outOfBounds(int x, int y){
        return x<0 || x>800-hitbox.width||y<0||y>600-hitbox.height;
    }

    private Image getImg(String img) {

        ImageIcon imageIcon = new ImageIcon(img);     //Creamos una ImageIcon y le pasamos el recurso
        Image image=imageIcon.getImage();
        return imageIcon.getImage();                                                                      //La convertimos a imagen

    }

    public void draw(Graphics2D graphics2D){
        if(img!=null) {
            graphics2D.drawImage(img, x, y, null);
        }
    }

    void move(int x, int y){
        this.x+=x;
        this.y+=y;
        hitbox.translate(x,y);

    }


    @Override
    public int compareTo(Entity o) {
        return this.hitbox.y-o.hitbox.y;
    }

}
