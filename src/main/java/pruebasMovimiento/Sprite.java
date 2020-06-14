package pruebasMovimiento;

import java.awt.*;
import java.awt.image.BufferedImage;

class Sprite {

    private Image spriteImg;


    Sprite(int idSprite, BufferedImage spriteSheet, int lado) {

        if (idSprite <= 0){
            return;
        }
        int x, y;

        y = (idSprite / lado) * lado;
        x = ((idSprite % lado) * lado) - lado;

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

    }

    Image getSpriteImg() {
        return spriteImg;
    }

}
