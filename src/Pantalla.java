import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import static herramientas.ExtraerDatosJson.*;
import static herramientas.ExtraerDatosJson.devolverNumSpritesTotal;

public class Pantalla extends JFrame implements ActionListener {

    static private int WIDTH;       //COLUMNAS
    static private int COLUMNS;       //COLUMNAS
    static private int HEIGHT;      //FILAS
    static private int ROWS;      //FILAS
    static private int TILESIZE;    //TAMAÑO (EN PIXELES) DEL SPRITE
    static private int TIMERDELAY = 2000;        //Esto es personalizable


    static BufferedImage spriteSheet;

    static private Timer mainTimer;                //Declaracion de un timer

    static private ArrayList spritesPantalla;           //Clase sprites
    static private int[][] spriteInts;                   //los numeritos de los sprites todo esto no me acaba
    static public ArrayList<Sprite> spritesObjectArrayList;
    static private ArrayList objetos;                   //todo clase obstaculos


    static private JSONObject archivoJson;

    BufferedImage imageBuffer;      //Utilizaremos esta imagen para pintar los sprites aqui antes de sacarlos por pantalla (para evitar cortes visuales)


    public Pantalla(String rutaJson, String rutaSpriteSheet) throws HeadlessException {


        TILESIZE = Integer.parseInt(extraerValorJson(rutaJson, "tileheight"));
        ROWS = Integer.parseInt(extraerValorJson(rutaJson, "height"));
        COLUMNS = Integer.parseInt(extraerValorJson(rutaJson, "width"));
        WIDTH = COLUMNS  * TILESIZE;
        HEIGHT = ROWS * TILESIZE + 32;

        //todo no se centra bien


        try {
            spriteSheet = ImageIO.read(new File(rutaSpriteSheet));
        } catch (IOException e) {
            e.printStackTrace();
        }

        spritesPantalla = arraysSprites(rutaJson);


        spriteInts = devolverNumSpritesTotal(arraysSprites(rutaJson));  //Poner un iterador que separe las capas HECHO

        imageBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         //Al cerrar la ventana, parara la ejecucion del programa
        setResizable(false);                                    //No permitimos que se pueda modificar el tamaño de ventana
        setLayout(new BorderLayout());                          //Añadimos un diseño de ventana añadiendole eun gestor
        //setLocationRelativeTo(null);                            //Colocara la ventana en el centro al lanzarla
        pack();                                                 //Se ajusta el tamaño para evitar errores (ventana con canvas)
        setSize(WIDTH, HEIGHT);
        setVisible(true);                                       //Hacemos la ventana visible

        mainTimer = new Timer(TIMERDELAY, this);
        mainTimer.start();  //Con esto ponemos a ejectuarse en bucle el actionPerfomed() de abajo.


        this.getGraphics().drawImage(printBackground(), 0, 0, null);   //esto pinta el background
    }


    @Override
    public void paint(Graphics g) {
        //super.paint(g);

        //this.getGraphics().drawImage(printBackground(), 0, 0, null);      //pinta background


    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        repaint();

        System.out.println("aaa");

    }


    public BufferedImage printBackground() {


        int capas = spriteInts.length;
        int spritesPorCapa = COLUMNS * ROWS;

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        for (int j = 0; j < capas; j++) {
            int x = 0;
            int y = 0;

            for (int i = 0; i < spritesPorCapa; i++) {
                if (x % COLUMNS == 0) {
                    y++;
                    x = 0;
                }
                graphics.drawImage(new Sprite(spriteInts[j][i], spriteSheet, TILESIZE).getSpriteImg(), x * TILESIZE, y * TILESIZE, null);
                x++;

            }


        }


        return imageBuffer;

    }


    public static void main(String[] args) {


    }

}
