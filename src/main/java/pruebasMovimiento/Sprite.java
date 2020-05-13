package pruebasMovimiento;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite {

    int idSprite;
    int lado;
    Image spriteImg;
    BufferedImage spriteSheet;

    static public ArrayList<Sprite> listaSprites = new ArrayList<>();


    public Sprite(int idSprite, BufferedImage spriteSheet, int lado) {
        this.idSprite = idSprite;


        this.spriteSheet = spriteSheet;
        this.lado = lado;

        //dimension stylesheet 1024 x 1024 32SPRITES x 32SPRITES

        if (idSprite <= 0){
            //spriteImg = spriteSheet.getSubimage(0, 0, lado, lado);
            listaSprites.add(this);
            return;


        }
        int x, y;

        y = (idSprite / lado) * lado;
        x = ((idSprite % lado) * lado) - lado;

        //TODO cuidado cuando guarmos el json que le suma 1 a todos los sprites (al id me refiero)

        if (x < 0){
            x = spriteSheet.getWidth()-lado;
            y -= lado;
        }


        try {
            spriteImg = spriteSheet.getSubimage(x, y, lado, lado);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FALLA EN LA X " + x + " Y " + y);
        }

        listaSprites.add(this);

    }


    public int getIdSprite() {
        return idSprite;
    }

    public Image getSpriteImg() {
        return spriteImg;
    }

    public int getLado() {
        return lado;
    }

    public BufferedImage getSpriteSheet() {
        return spriteSheet;
    }
}
