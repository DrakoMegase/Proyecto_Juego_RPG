package mapasPruebas2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Ventana extends JFrame {


    String nombreVentana;
    File background;
    Image image;
    Graphics2D g;

    public Ventana(String nombreVentana, File background) throws HeadlessException {
        this.nombreVentana = nombreVentana;
        this.background = background;


        try {
            this.image = ImageIO.read(getClass().getResource("/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle(nombreVentana);
        setSize(image.getWidth(null), image.getHeight(null));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        repaint();



//        Sprites s1 = new Sprites()

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, this);
        System.out.println("aa");

    }


}
