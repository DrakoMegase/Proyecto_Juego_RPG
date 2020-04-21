package mapasPruebas2;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame {



    public static void main(String[] args) throws IOException {


        File background = new File("/background.png");
        Ventana v1 = new Ventana("Juego", background);


    }

}
