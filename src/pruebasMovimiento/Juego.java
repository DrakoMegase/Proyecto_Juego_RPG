package pruebasMovimiento;

import herramientas.ManipulacionDatos;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

import static herramientas.ExtraerDatosJson.extraerValorJson;
import static herramientas.ExtraerDatosJson.salidasMapa;
import static pruebasMovimiento.MapGenerator.generateMap;

public class Juego extends JPanel implements ActionListener {


    //Atributos del jugador.
    public static Player player;                          //Declaracion de un player
    static private int cuerpoPlayerX;
    static private int cuerpoPlayerY;

    //Atributos de pantalla
    static public int WIDTH;       //COLUMNAS
    static public int COLUMNS;       //COLUMNAS
    static public int HEIGHT;      //FILAS
    static public int ROWS;      //FILAS
    static public int TILESIZE;    //TAMAÑO (EN PIXELES) DEL SPRITE

    //Atributos graficos
    static private BufferedImage imageBufferJuego;             //Utilizaremos esta imagen para pintar los sprites aqui antes de sacarlos por pantalla (para evitar cortes visuales)
    static private BufferedImage imageBufferDetailsJuego;      //Utilizaremos esta imagen para pintar los sprites aqui antes de sacarlos por pantalla (para evitar cortes visuales)
    static private BufferedImage UI;                            //spriteSheet UI
    static private BufferedImage spriteSheetJuego;             //spriteSheet del terreno
    static private UI ui;                                       //spriteSheet UI
    //static private int[][] spriteInts;                       //los numeritos de los sprites todo esto no me acaba

    //Atributos de camara
    static public int offSetX = 0;
    static public int offSetY = 0;

    //Atributos del juego
    static private int TIMERDELAY = 20;        //Delay del timer
    static public Timer mainTimer;            //Declaracion de un timer
    public static ArrayList<Room> salas;
    private static HashMap<String,Salida> salidasJuego;
    protected static LinkedList<Entity> entitiesJuego;

    static Rectangle slash;

    //Constructor de la clase Juego
    public Juego(String rutaJson, String rutaSpriteSheet) {

        salas = new ArrayList<>();
        Room[][] level= MapGenerator.generateMap(12);
        Room inicio=null;
        Room sala=null;
        for (int i = 0; i < level.length; i++) {
            System.out.println("0\t");
            for (int j = 0; j < level[i].length; j++) {
                if(level[i][j]!=null){
                    sala=level[i][j];
                    sala.inicializarSala();
                    salas.add(sala);
                    System.out.print(sala.idSala + "\t");
                    if(sala.salaClass==0){
                        inicio=sala;
                    }
                }
            }
        }


        //Definimos atributos para su uso en ventana.
        TILESIZE = Integer.parseInt(extraerValorJson(rutaJson, "tileheight"));
        ROWS = Integer.parseInt(extraerValorJson(rutaJson, "height"));
        COLUMNS = Integer.parseInt(extraerValorJson(rutaJson, "width"));
        WIDTH = COLUMNS * TILESIZE;
        HEIGHT = ROWS * TILESIZE;

        setLayout(new BorderLayout());                          //Añadimos un diseño de ventana añadiendole eun gestor
//        setSize(WIDTH, HEIGHT);
        setVisible(true);                                       //Hacemos la ventana visible
        setBackground(Color.black);
        setFocusable(true);                                     //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        //setLocationRelativeTo(null);                          //Colocara la ventana en el centro al lanzarla


        //INICIALIZACION DE LAS LISTAS QUE USAREMOS
        entitiesJuego = inicio.entities;
        salidasJuego = inicio.salidas;





        //INICIALIZACION DE ENTITIES
        player = new Player(400, 400, 20, entitiesJuego);

        //INICIACION DE LA UI (siempre despies del player)

        ui = new UI(player);

        //CARGAR DATOS EN LAS LISTAS
        entitiesJuego.add(player);

//        entitiesJuego.add(new Enemy(200, 500, 20, "img/spritesheetTest.png:2:192:0:16:32", 3, 10, 9, 11, true, true, player, 1, 1));
        entitiesJuego.add(new Enemy(500,300,20,"img/spritesheetTest.png:1:48:0:16:16",4,8,8,8,true,true,player,1,0));
        entitiesJuego.add(new Enemy(500,150,20,"img/spritesheetTest.png:1:48:0:16:16",4,8,8,8,true,true,player,1,0));
        entitiesJuego.add(new Enemy(150,500,20,"img/spritesheetTest.png:1:48:0:16:16",4,8,8,8,true,true,player,1,0));
        entitiesJuego.add(new Enemy(300,500,20,"img/spritesheetTest.png:1:48:0:16:16",4,8,8,8,true,true,player,1,0));
        entitiesJuego.add(new Enemy(200, 500, 20, "img/spritesheetTest.png:2:192:0:16:32", 3, 10, 9, 11, true, true, player, 1, 1));


        //Cargar datos salas.


        addKeyListener(new KeyAdapt(player));


        imageBufferJuego = inicio.backgroundSala;
        imageBufferDetailsJuego = inicio.detailsSala;
        try {
            UI = ImageIO.read(new File("res/img/UI.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageBufferJuego = inicio.backgroundSala;
        imageBufferDetailsJuego = inicio.detailsSala;


        mainTimer = new Timer(TIMERDELAY, this);
        mainTimer.start();  //Con esto ponemos a ejectuarse en bucle el actionPerfomed() de abajo.

    }

    private ArrayList<Room> cargarSalasNivel(ArrayList<Room> salas) {





        return salas;
    }

    private void camaraUpdate() {

        int plx = player.getX();
        int ply = player.getY();

        offSetX = player.getX() - this.getWidth() / 2 + player.hitbox.width;
        offSetY = player.getY() - this.getHeight() / 2 + player.hitbox.height;

        //System.out.println(plx + "     " + ply + "   " + offSetX + "    " + offSetY);

    }


    public void cargarSala(Room room) {


        //TODO peta por todos los lados

        /*
        Al cargar una sala utilizaremos el siguiente criterio:
        Las salas estan formadas por 3 elementos (a grosso modo):
        1.El background (una BufferedImage)
        2.Los detalles (donde el personaje se pinta antes que estos) Otra bufferedImage
        3.Todos los entities, aqui tenemos tanto los obstaculos como los enemigos y el jugador.
        */


        //LIMPIAMOS
        //BFF BACKGROUND + ENTITES + BFF DETAILS

        //room.cargarNuevaSala(imageBufferJuego,entitiesJuego,imageBufferDetailsJuego);

        imageBufferJuego = room.backgroundSala;
        imageBufferDetailsJuego = room.detailsSala;

        //Limpiamos nuestras listas
        entitiesJuego.remove(player);
        entitiesJuego=room.entities;
        salidasJuego=room.salidas;
        //Añadimos al jugador
        entitiesJuego.add(player);

        player.setPos(400, 400);


        Set<String> keySet=salidasJuego.keySet();

        for(String key:keySet){
            System.out.println(key+" "+salidasJuego.get(key).getConexion());
        }
        System.out.println();

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        for (int i = 0; i < entitiesJuego.size();) {
            Entity entity = entitiesJuego.get(i);
            entity.update();
            if (entity.remove) {
                entitiesJuego.remove(entity);
            }else {
                i++;
            }
        }



        Iterator<Entity> iterator = entitiesJuego.iterator();

        entitiesJuego.sort(new CompareNearEntities(entitiesJuego));
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            entity.checkCollisions(entitiesJuego, 0);
        }

        if (player.hp <= 0) {
            //Canbedamaged del player esta en false todo
            entitiesJuego.remove(player);
            System.out.println("FIN DE LA PARTIDA vida jugador es = " + player.hp);
            System.exit(0);
            return;
        }

        if(salidasJuego!=null) {
            Set<String> keys=salidasJuego.keySet();
            Salida salida;
            for (String key : keys){
                salida=salidasJuego.get(key);
                if (salida!=null&&player.hitbox.intersects(salida.getArea())) {
                    Room room2 = salida.getConexion().getOrigen();
                    cargarSala(room2);

                }
            }


        }


        repaint();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        camaraUpdate();

        //PRIMERA PINTADA: FONDO

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(imageBufferJuego, -offSetX, -offSetY, null);


        //SEGUNDA PINTADA: ENTITIES
        entitiesJuego.sort(Entity::compareTo);
        for (Entity entity : entitiesJuego) {
            entity.draw(graphics2D, offSetX, offSetY);
            Rectangle rectangle = (Rectangle) entity.hitbox.clone();
            rectangle.x -= offSetX;
            rectangle.y -= offSetY;
            graphics2D.draw(rectangle);
            //graphics2D.draw(entity.hitbox);
        }

        Set<String> keys=salidasJuego.keySet();
        Salida salida;
        for (String key : keys){
            salida=salidasJuego.get(key);
            Rectangle rectSalida = (Rectangle) salida.getArea().clone();
            rectSalida.x -= offSetX;
            rectSalida.y -= offSetY;
            graphics2D.draw(rectSalida);
        }


        if(slash!=null){
            Rectangle rectangle = (Rectangle) slash.clone();
            rectangle.x -= offSetX;
            rectangle.y -= offSetY;
            graphics2D.draw(rectangle);
        }

        //TERCER PINTADA: DETALLES
        graphics2D.drawImage(imageBufferDetailsJuego, -offSetX, -offSetY, null);
        graphics2D.drawImage(ui.draw(graphics2D),0,0,null);



    }


    public static void main(String[] args) {
        Juego juego = new Juego("res/jsonsMapasPruebas/1.json","resources/terrain_atlas.png");
        JFrame frame = new JFrame("Sloanegate");                           //Frame = Marco         Creacion de ventana
        frame.setSize(500, 529);                                                   //Tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //Accion cuando cerramos la ventana
        frame.setResizable(false);                                                        //Negamos que la ventana pueda ser modificada en tamaño
        frame.add(juego);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("res/img/icon.png").getImage());    //Define el icono



    }

}
