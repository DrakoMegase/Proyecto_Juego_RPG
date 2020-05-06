package pruebasMovimiento;

import herramientas.ManipulacionDatos;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import static herramientas.ExtraerDatosJson.*;

public class Pantalla extends JPanel implements ActionListener {

    static public int WIDTH;       //COLUMNAS
    static public int COLUMNS;       //COLUMNAS
    static public int HEIGHT;      //FILAS
    static public int ROWS;      //FILAS
    static public int TILESIZE;    //TAMAÑO (EN PIXELES) DEL SPRITE
    static private int TIMERDELAY = 20;        //Esto es personalizable
    static public int offSetX = 0;
    static public int offSetY = 0;
    static private int cuerpoPlayerX;
    static private int cuerpoPlayerY;


    static BufferedImage spriteSheet;
    static private Timer mainTimer;                //Declaracion de un timer

    //static private ArrayList spritesPantalla;           //Clase sprites
    static private int[][] spriteInts;                   //los numeritos de los sprites todo esto no me acaba

    BufferedImage imageBuffer;      //Utilizaremos esta imagen para pintar los sprites aqui antes de sacarlos por pantalla (para evitar cortes visuales)
    BufferedImage imageBufferDetails;      //Utilizaremos esta imagen para pintar los sprites aqui antes de sacarlos por pantalla (para evitar cortes visuales)


    public static ArrayList<Room> salas;
    private static LinkedList<Rectangle> salidas;
    public static Player player;                          //Declaracion de un player
    protected static LinkedList<Entity> entities;
    protected static LinkedList<Entity> addEntities;

    public Pantalla(String rutaJson, String rutaSpriteSheet) throws HeadlessException {


        TILESIZE = Integer.parseInt(extraerValorJson(rutaJson, "tileheight"));
        ROWS = Integer.parseInt(extraerValorJson(rutaJson, "height"));
        COLUMNS = Integer.parseInt(extraerValorJson(rutaJson, "width"));
        WIDTH = COLUMNS * TILESIZE;
        HEIGHT = ROWS * TILESIZE;


        //todo no se centra bien


        try {
            spriteSheet = ImageIO.read(new File(rutaSpriteSheet));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //spritesPantalla = arraysSprites(rutaJson);
        spriteInts = devolverNumSpritesTotal(arraysSprites(rutaJson));  //Poner un iterador que separe las capas HECHO
        imageBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        imageBufferDetails = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);


        setLayout(new BorderLayout());                          //Añadimos un diseño de ventana añadiendole eun gestor
        setSize(WIDTH, HEIGHT);
        setVisible(true);                                       //Hacemos la ventana visible
        setBackground(Color.black);
        setFocusable(true);                                     //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        //setLocationRelativeTo(null);                          //Colocara la ventana en el centro al lanzarla

        entities = new LinkedList<>();
        ManipulacionDatos.rectanglesToEntityObjects(rutaJson, entities);
        //System.out.println(entities.size());


        //Room sala2 = new Room(printBackground(),printBackgroundDetails(),player, rutaJson);

        addEntities = new LinkedList<>();

        player = new Player(0, 0, 20,entities);  //todo necesario
        entities.add(player);


//        entities.add(new Enemy(500,500,20,"img/spritesheetTest.png:1:48:0:16:16",4,8,8,8,true,false,player,1,0));

        entities.add(new Enemy(200, 500, 20, "img/spritesheetTest.png:2:192:0:16:32", 3, 10, 9, 11, true, true, player, 1, 1));
        entities.add(new Enemy(200, 500, 20, "img/spritesheetTest.png:2:192:0:16:32", 3, 10, 9, 11, true, true, player, 1, 1));
        entities.add(new Enemy(200, 500, 20, "img/spritesheetTest.png:2:192:0:16:32", 3, 10, 9, 11, true, true, player, 1, 1));
        entities.add(new Enemy(200, 500, 20, "img/spritesheetTest.png:2:192:0:16:32", 3, 10, 9, 11, true, true, player, 1, 1));
        entities.add(new Prop(50, 50, 1000, "img/terrain_atlas.png:0:928:896:96:128", 30, 98, 34, 17, false, false));


        addKeyListener(new KeyAdapt(player));

        mainTimer = new Timer(TIMERDELAY, this);
        mainTimer.start();  //Con esto ponemos a ejectuarse en bucle el actionPerfomed() de abajo.

        printBackground(imageBuffer, spriteInts);
        printBackgroundDetails(imageBufferDetails, spriteInts);

        cuerpoPlayerX = (int) (player.hitbox.getWidth() / 2);
        cuerpoPlayerY = (int) (player.hitbox.getHeight() / 2);

//        salidas = new LinkedList<>();
//        colocarsalidas(rutaJson);
        salas = new ArrayList<>();      //inicializacion del arraylist

        System.out.println(cuerpoPlayerX + " AAAAAAAAAAA " + cuerpoPlayerY);
        camaraUpdate();
        //guardarSala();
        //cargarSala(rutaJson);
        player.setPos(50,400);
    }

    private void colocarsalidas(String rutaJson) {

        //salidas.addAll(salidasMapa(rutaJson));

    }


    public void guardarSala() {

//        Room sala = new Room(this.imageBuffer, this.imageBufferDetails, this.player);
//        System.out.println(sala);
//        sala.setUsed(false);
//        salas.add(sala);
    }


    public void cargarSala(String rutaJson) {


        //TODO peta por todos los lados

        /*
        Al cargar una sala utilizaremos el siguiente criterio:
        Las salas estan formadas por 3 elementos (a grosso modo):
        1.El background (una BufferedImage)
        2.Los detalles (donde el personaje se pinta antes que estos) Otra bufferedImage
        3.Todos los entities, aqui tenemos tanto los obstaculos como los enemigos y el jugador.
        */







        player.setPos(400, 400);


    }


    @Override
    public void paint(Graphics graphics) {
        camaraUpdate();
        super.paint(graphics);                                          //Referenciamos sobre que Panel tiene que trabajar
        Graphics2D graphics2D = (Graphics2D) graphics;
//        Graphics2D graphics2D = (Graphics2D) imageBuffer.getGraphics();                  //Casteo de Graphics a Graphics2D.      Graphics2D proporciona acceso a las características avanzadas de renderizado del API 2D de Java.

        graphics2D.drawImage(imageBuffer, -offSetX, -offSetY, null);


        entities.sort(Entity::compareTo);
        for (Entity entity : entities) {
            entity.draw(graphics2D, offSetX, offSetY);
            Rectangle rectangle = (Rectangle) entity.hitbox.clone();
            rectangle.x -= offSetX;
            rectangle.y -= offSetY;
            graphics2D.draw(rectangle);
            //graphics2D.draw(entity.hitbox);
        }

        for (Rectangle rectangle : salidas){
            Rectangle rectSalida = (Rectangle) rectangle.clone();
            rectSalida.x -= offSetX;
            rectSalida.y -= offSetY;
            graphics2D.draw(rectSalida);
        }

        graphics2D.drawImage(imageBufferDetails, -offSetX, -offSetY, null);      //pinta background

    }

    private void camaraUpdate() {

        int plx = player.getX();
        int ply = player.getY();

        offSetX = player.getX() - this.getWidth() / 2 + player.hitbox.width;
        offSetY = player.getY() - this.getHeight() / 2 + player.hitbox.height;

        //System.out.println(plx + "     " + ply + "   " + offSetX + "    " + offSetY);

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Cada 20ms (depende el timer) se acutaliza el metodo update en player y se repainteara la pantalla
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            entity.update();
            if (entity.remove) {
                iterator.remove();
            }
        }
        if (addEntities.size() > 0) {
            entities.addAll(addEntities);
            addEntities.clear();
        }

        iterator = entities.iterator();


        entities.sort(new CompareNearEntities(entities));
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            entity.checkCollisions(entities, 0);
        }

        if (player.hp <= 0) {
            //Canbedamaged del player esta en false todo
            entities.remove(player);
            System.out.println("FIN DE LA PARTIDA vida jugador es = " + player.hp);
            System.exit(0);
            return;
        }

        if(salidas!=null) {
            for (Rectangle salidas : salidas
            ) {
                if (player.hitbox.intersects(salidas)) {
                    System.out.println("POLLA");
                    cargarSala("res/jsonsMapasPruebas/1110.json");

                }

            }
        }


        repaint();

    }


    public static BufferedImage printBackground(BufferedImage imageBuffer,int[][] spriteInts) {


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

    public static BufferedImage printBackgroundDetails(BufferedImage imageBuffer, int spriteInts[][]) {


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


    public static void main(String[] args) {


    }

}
