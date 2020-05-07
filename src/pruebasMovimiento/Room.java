package pruebasMovimiento;

import herramientas.ManipulacionDatos;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

import static herramientas.ExtraerDatosJson.*;
import static herramientas.ExtraerDatosJson.extraerValorJson;
import static pruebasMovimiento.Juego.*;

public class Room {

    int idSala;
    int salaType;
    private static int contador = 0;
    BufferedImage backgroundSala;
    BufferedImage detailsSala;
    LinkedList<Entity>entities;
    LinkedList<Salida>salidas;
    boolean clear;
    static private int[][] spriteInts;                   //los numeritos de los sprites todo esto no me acaba
    static BufferedImage spriteSheet;

    public Room(String rutaJsonRoom, String rutaSpriteSheet) {

        //Pruebas

        TILESIZE = Integer.parseInt(extraerValorJson(rutaJsonRoom, "tileheight"));
        ROWS = Integer.parseInt(extraerValorJson(rutaJsonRoom, "height"));
        COLUMNS = Integer.parseInt(extraerValorJson(rutaJsonRoom, "width"));
        WIDTH = COLUMNS * TILESIZE;
        HEIGHT = ROWS * TILESIZE;



        spriteInts = devolverNumSpritesTotal(arraysSprites(rutaJsonRoom));  //Poner un iterador que separe las capas HECHO
        try {
            spriteSheet = ImageIO.read(new File(rutaSpriteSheet));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundSala = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        detailsSala = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        entities = new LinkedList<>();
        salidas = new LinkedList<>();


        ManipulacionDatos.rectanglesToEntityObjects(rutaJsonRoom, entities);
        salidas.addAll(salidasMapa(rutaJsonRoom));



        printBackground(backgroundSala, spriteInts);
        printBackgroundDetails(detailsSala, spriteInts);



        contador++;
        this.idSala = contador;
    }


    public BufferedImage printBackground(BufferedImage imageBuffer,int[][] spriteInts) {


        int capas = spriteInts.length;
        int spritesPorCapa = COLUMNS * ROWS;

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        for (int j = 0; j < capas - 1; j++) {
            int x = 0;
            int y = -1; //TODO METODOS INTERNOS BUSCAR PARA IGUALAR ESTO A 0

            for (int i = 0; i < spritesPorCapa; i++) {

                if (x % COLUMNS == 0) {
                    y++;
                    x = 0;
                }

                graphics.drawImage(new Sprite(spriteInts[j][i], spriteSheet, TILESIZE).getSpriteImg(), x * TILESIZE, y * TILESIZE, null);
                x++;
                //System.out.println(y + "   x  " +  x);

            }


        }


        return imageBuffer;

    }

    public BufferedImage printBackgroundDetails(BufferedImage imageBuffer, int spriteInts[][]) {


        int capas = spriteInts.length;
        int spritesPorCapa = COLUMNS * ROWS;

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        int x = 0;
        int y = -1; //TODO METODOS INTERNOS BUSCAR PARA IGUALAR ESTO A 0

        for (int i = 0; i < spritesPorCapa; i++) {
            if (x % COLUMNS == 0) {
                y++;
                x = 0;
            }
            graphics.drawImage(new Sprite(spriteInts[capas - 1][i], spriteSheet, TILESIZE).getSpriteImg(), x * TILESIZE, y * TILESIZE, null);
            x++;


        }

        return imageBuffer;

    }



    public void cargarNuevaSala(BufferedImage backgroundSala, LinkedList<Entity> entities, BufferedImage detailsSala){

        backgroundSala = this.backgroundSala;
        //entities.clear();
        //entities.addAll(this.entities);
        detailsSala = this.detailsSala;


    }

    public void start(){

        JFrame jFrame = new JFrame();
        jFrame.setLayout(new BorderLayout());                          //Añadimos un diseño de ventana añadiendole eun gestor
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.setVisible(true);                                       //Hacemos la ventana visible
        jFrame.setBackground(Color.black);
        jFrame.setFocusable(true);                                     //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        //setLocationRelativeTo(null);                          //Colocara la ventana en el centro al lanzarla



    }

    public void setSalaType(int salaType) {
        this.salaType = salaType;
    }

    public int getSalaType() {
        return salaType;
    }

    @Override
    public String toString() {
        return "Room{" +
                "idSala=" + idSala +
                ", salidas=" + salidas +
                ", clear=" + clear +
                "} \n";
    }

    public static void main(String[] args) {

        Room room1 = new Room("res/jsonsMapasPruebas/0001.json", "resources/terrain_atlas.png");

        room1.start();


    }

}
