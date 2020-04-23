package pruebasMovimiento;

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
import java.util.Iterator;
import java.util.LinkedList;

import static herramientas.ExtraerDatosJson.*;

public class Pantalla extends JPanel implements ActionListener {

    static private int WIDTH;       //COLUMNAS
    static private int COLUMNS;       //COLUMNAS
    static private int HEIGHT;      //FILAS
    static private int ROWS;      //FILAS
    static private int TILESIZE;    //TAMAÑO (EN PIXELES) DEL SPRITE
    static private int TIMERDELAY = 20;        //Esto es personalizable

    static BufferedImage spriteSheet;

    static private Timer mainTimer;                //Declaracion de un timer

    static private ArrayList spritesPantalla;           //Clase sprites
    static private int[][] spriteInts;                   //los numeritos de los sprites todo esto no me acaba

    BufferedImage imageBuffer;      //Utilizaremos esta imagen para pintar los sprites aqui antes de sacarlos por pantalla (para evitar cortes visuales)


    private Player player;                  //Declaracion de un player
    protected static LinkedList<Entity> entities;
    protected static LinkedList<Entity> addEntities;

    public Pantalla(String rutaJson, String rutaSpriteSheet) throws HeadlessException {


        TILESIZE = Integer.parseInt(extraerValorJson(rutaJson, "tileheight"));
        ROWS = Integer.parseInt(extraerValorJson(rutaJson, "height"));
        COLUMNS = Integer.parseInt(extraerValorJson(rutaJson, "width"));
        WIDTH = COLUMNS  * TILESIZE;
        HEIGHT = ROWS * TILESIZE;

        //todo no se centra bien


        try {
            spriteSheet = ImageIO.read(new File(rutaSpriteSheet));
        } catch (IOException e) {
            e.printStackTrace();
        }

        spritesPantalla = arraysSprites(rutaJson);


        spriteInts = devolverNumSpritesTotal(arraysSprites(rutaJson));  //Poner un iterador que separe las capas HECHO

        imageBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);


        setLayout(new BorderLayout());                          //Añadimos un diseño de ventana añadiendole eun gestor
        //setLocationRelativeTo(null);                            //Colocara la ventana en el centro al lanzarla
        setSize(WIDTH, HEIGHT);
        setVisible(true);                                       //Hacemos la ventana visible

        setFocusable(true);                 //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        entities=new LinkedList<>();
        addEntities=new LinkedList<>();

        player = new Player(10, 10, 20);
        entities.add(player);

        entities.add(new Enemy(500,500,20,"img/ogro.png",7,32,14,15,true,true,player,1));

        entities.add(new Prop(50,50,1000,"img/terrain_atlas.png:0:928:896:96:128",30,98,34,17,false,false));


        addKeyListener(new KeyAdapt(player));
        printBackground();

        mainTimer = new Timer(TIMERDELAY, this);
        mainTimer.start();  //Con esto ponemos a ejectuarse en bucle el actionPerfomed() de abajo.


    }


    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);                                          //Referenciamos sobre que Panel tiene que trabajar
        Graphics2D graphics2D=(Graphics2D) graphics;
//        Graphics2D graphics2D = (Graphics2D) imageBuffer.getGraphics();                  //Casteo de Graphics a Graphics2D.      Graphics2D proporciona acceso a las características avanzadas de renderizado del API 2D de Java.


//        graphics2D.drawImage(printBackground(), 0, 0, null);      //pinta background
        graphics2D.drawImage(imageBuffer,0,0,null);

        entities.sort(Entity::compareTo);
        for(Entity entity:entities){
            entity.draw(graphics2D);
        }



    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Cada 20ms (depende el timer) se acutaliza el metodo update en player y se repainteara la pantalla
        Iterator<Entity> iterator=entities.iterator();
        while (iterator.hasNext()){
            Entity entity=iterator.next();
            entity.update();
            if(entity.remove){
                iterator.remove();
            }
        }
        if(addEntities.size()>0) {
            entities.addAll(addEntities);
            addEntities.clear();
        }

        iterator=entities.iterator();




        entities.sort(new CompareNearEntities(entities));
        while (iterator.hasNext()){
            Entity entity=iterator.next();
            entity.checkCollisions(entities);
        }


        repaint();

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
