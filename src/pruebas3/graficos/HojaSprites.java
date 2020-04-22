package pruebas3.graficos;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HojaSprites {

    private final int ancho;
    private final int alto;
    public final int[] pixeles;

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public HojaSprites(final String ruta, final int ancho, final int alto) {
        this.ancho = ancho;
        this.alto = alto;

        pixeles = new int [ancho * alto];

        BufferedImage imagen = null;
        try {
            imagen = ImageIO.read(HojaSprites.class.getResource(ruta));
            imagen.getRGB(0,0,alto,ancho,pixeles,0,ancho);
        } catch (IOException e) {
            e.printStackTrace();
        }





    }
}
