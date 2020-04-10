package pruebasMovimiento;

import javax.swing.*;
import java.awt.*;

public class Entity implements Comparable<Entity>{

    Image img;
    boolean canBeMoved;
    int x;
    int y;
    int[] hitbox;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Entity(int x, int y, String img, boolean canBeMoved) {
        this.x = x;
        this.y = y;

        this.img=getImg(img);
        this.canBeMoved=canBeMoved;

        hitbox=createHitbox();

    }


    public void update(){

    }

    public int[] createHitbox(){
        int xMargin=img.getWidth(null)/4;
        int yMargin=img.getHeight(null)/2;
        int[] hitbox={xMargin,img.getWidth(null)-xMargin,yMargin,img.getHeight(null)};
        return hitbox;
    }


    public boolean push(int x, int y){
        int newX=this.x+x;
        int newY=this.y+y;

        if(canBeMoved&&!outOfBounds(newX,newY)){
            this.x = newX;
            this.y = newY;
        }

        return canBeMoved&&!outOfBounds(newX,newY);
    }

    public boolean outOfBounds(int x, int y){
        return x<0 || x>800-img.getWidth(null)||y<0||y>600-img.getHeight(null);
    }

    public Image getImg(String img) {

        ImageIcon imageIcon = new ImageIcon(img);     //Creamos una ImageIcon y le pasamos el recurso
        Image image=imageIcon.getImage();
        return imageIcon.getImage();                                                                      //La convertimos a imagen

    }

    public void draw(Graphics2D graphics2D){
        if(img!=null) {
            graphics2D.drawImage(img, x, y, null);
        }
    }


    @Override
    public int compareTo(Entity o) {
        return this.y-o.y;
    }
}
