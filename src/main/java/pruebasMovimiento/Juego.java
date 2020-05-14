package pruebasMovimiento;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
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

import static herramientas.ExtraerDatosJson.extraerValorJson;

public class Juego extends JPanel implements ActionListener {


    //Atributos del jugador.
    static Player player;                          //Declaracion de un player
    static private int cuerpoPlayerX;
    static private int cuerpoPlayerY;

    //Atributos de pantalla
    static int WIDTH;       //COLUMNAS
    static int COLUMNS;       //COLUMNAS
    static int HEIGHT;      //FILAS
    static int ROWS;      //FILAS
    static int TILESIZE;    //TAMAÑO (EN PIXELES) DEL SPRITE

    static private int nivel=0;

    //Atributos graficos
    static private BufferedImage imageBufferJuego;             //Utilizaremos esta imagen para pintar los sprites aqui antes de sacarlos por pantalla (para evitar cortes visuales)
    static private BufferedImage imageBufferDetailsJuego;      //Utilizaremos esta imagen para pintar los sprites aqui antes de sacarlos por pantalla (para evitar cortes visuales)
    static private BufferedImage UI;                            //spriteSheet UI
    static private BufferedImage escapeMenuImg;                            //menu escape
    static private BufferedImage spriteSheetJuego;             //spriteSheet del terreno
    static private UI ui;                                       //spriteSheet UI
    static private Rectangle uiRecMinimap;
    static private Rectangle map;
    //static private int[][] spriteInts;                       //los numeritos de los sprites todo esto no me acaba
    private Stroke defStroke;     //Esto sirve para hacer los rectangulos (las lineas y eso)
    private JPanel backgroundPanel;

    //Atributos de camara
    static private int offSetX = 0;
    static private int offSetY = 0;

    //Atributos del juego
    static private int TIMERDELAY = 10;        //Delay del timer
    static private Timer mainTimer;            //Declaracion de un timer
    private static ArrayList<Room> salas;
    private static HashMap<String, Salida> salidasJuego;
    private static LinkedList<Entity> entitiesJuego;
    private Rectangle[][] rectangles;
    static boolean menuEsc;
    protected static boolean paintSt;



    private Image imagenEscape;
    static Rectangle slash;

    //Constructor de la clase Juego
    Juego(String rutaJson, String rutaSpriteSheet) {

        //INICIALIZACION DE ENTITIES



        player = new Player(400, 400, 20, entitiesJuego);

        salas = new ArrayList<>();
        Room[][] level = MapGenerator.generateMap(5*(1+nivel));
        Room inicio = null;
        Room sala;
        String[][] salasint = new String[level.length][level.length];
        rectangles = new Rectangle[level.length][level.length];
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                if (level[i][j] != null) {
                    sala = level[i][j];
                    sala.inicializarSala(nivel);
                    salas.add(sala);
                    salasint[sala.x][sala.y] = "["+sala.salaClass+"]";
                    rectangles[sala.x][sala.y] = new Rectangle(200 + sala.x * 12, 200 + sala.y * 12, 8, 8);
                    if (sala.salaClass == 0) {
                        inicio = sala;
                    }
                }
                if (level[i][j] == null) salasint[i][j] = " ";

            }
        }


        for (int i = 0; i < salasint.length; i++) {
            for (int j = 0; j < salasint.length; j++) {

                System.out.print(salasint[j][i] + "\t");

            }
            System.out.println();
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
        setLayout(null);

        //setLocationRelativeTo(null);                          //Colocara la ventana en el centro al lanzarla


        //TODO
        imagenEscape = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/guardar.png")))
                .getImage();
        backgroundPanel = new JPanel();


        //INICIALIZACION DE LAS LISTAS QUE USAREMOS
        entitiesJuego = inicio.entities;
        salidasJuego = inicio.salidas;
        //DAMOS LAS ENTITIES AL PLAYER
        player.setAddEntities(entitiesJuego);

        //INICIACION DE LA UI (siempre despies del player)


        //backgroundPanel = new JPanel();
        ui = new UI(player);
        uiRecMinimap = ui.getMinimapa();
        map = ui.getMapa();
        try {
            UI = ImageIO.read(new File("res/img/UI.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //CARGAR DATOS EN LAS LISTAS
        entitiesJuego.add(player);

//        player.setAddEntities(entitiesJuego);

        for (Room r : salas
        ) {
            if (r.player != null) {
                player.salaPlayer = r;
                r.setVisited(true);
            }

        }

//


        //Cargar datos salas.
        //TODO
        for (String s : player.salaPlayer.salidas.keySet()
        ) {

            Room r = player.salaPlayer.salidas.get(s).getConexion().getOrigen();
            r.setNear(true);

            System.out.println(r);
        }


        addKeyListener(new KeyAdapt(player));


        imageBufferJuego = inicio.backgroundSala;
        imageBufferDetailsJuego = inicio.detailsSala;
        try {
            escapeMenuImg = ImageIO.read(new File("res/img/guardar.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageBufferJuego = inicio.backgroundSala;
        imageBufferDetailsJuego = inicio.detailsSala;


        mainTimer = new Timer(TIMERDELAY, this);
        //mainTimer.start();  //Con esto ponemos a ejectuarse en bucle el actionPerfomed() de abajo.


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


    private void cargarSala(Room room, String exit) {


        /*
        Al cargar una sala utilizaremos el siguiente criterio:
        Las salas estan formadas por 3 elementos (a grosso modo):
        1.El background (una BufferedImage)
        2.Los detalles (donde el personaje se pinta antes que estos) Otra bufferedImage
        3.Todos los entities, aqui tenemos tanto los obstaculos como los enemies y el jugador.
        */


        //LIMPIAMOS
        //BFF BACKGROUND + ENTITES + BFF DETAILS

        //room.cargarNuevaSala(imageBufferJuego,entitiesJuego,imageBufferDetailsJuego);

        imageBufferJuego = room.backgroundSala;
        imageBufferDetailsJuego = room.detailsSala;

        //Limpiamos nuestras listas
        entitiesJuego.remove(player);
        entitiesJuego = room.entities;
        salidasJuego = room.salidas;
        //Añadimos al jugador
        entitiesJuego.add(player);
        player.setAddEntities(entitiesJuego);

        switch (exit){
            case "2":
                player.setPos(player.x,100);
                break;
            case "1":
                player.setPos(player.x,HEIGHT-100-player.hitbox.height);
                break;
            case "4":
                player.setPos(WIDTH-100-player.hitbox.width,player.y);
                break;
            case "3":
                player.setPos(100,player.y);
                break;

        }


        Set<String> keySet = salidasJuego.keySet();

        for (String key : keySet) {
            System.out.println(key + " " + salidasJuego.get(key).getConexion());
        }
        System.out.println();

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        boolean clear=true;

        for (int i = 0; i < entitiesJuego.size(); ) {
            Entity entity = entitiesJuego.get(i);
            entity.update();
            if(!player.salaPlayer.clear&&clear&&entity instanceof Enemy){
                clear=false;
            }

            if (entity.remove) {
                entitiesJuego.remove(entity);
            } else {
                i++;
            }
        }

        if(!player.salaPlayer.clear&&clear){
            player.salaPlayer.clear=true;
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

        if (player.salaPlayer.clear&&salidasJuego != null) {
            Set<String> keys = salidasJuego.keySet();
            Salida salida;
            for (String key : keys) {
                salida = salidasJuego.get(key);
                if (salida != null && player.hitbox.intersects(salida.getArea())) {
                    Room room2 = salida.getConexion().getOrigen();
                    cargarSala(room2, key);
                    player.salaPlayer.player = null;
                    room2.setVisited(true);
                    room2.player = player;
                    player.salaPlayer = room2;



                }
            }


        }
        for (String s : player.salaPlayer.salidas.keySet()
        ) {

            Room r = player.salaPlayer.salidas.get(s).getConexion().getOrigen();
            r.setNear(true);

            //System.out.println(r);
        }



        repaint();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (menuEsc){

            //TODO hacer un nuevo JPANEL y añadir ahi el menu de pausa.

            escape(imagenEscape);
            repaint();
            return;
        }
        remove(backgroundPanel);
        camaraUpdate();
        Graphics2D graphics2D = (Graphics2D) g;


        //PRIMERA PINTADA: FONDO

        graphics2D.drawImage(imageBufferJuego, -offSetX, -offSetY, null);




        if (defStroke == null) {
            defStroke = graphics2D.getStroke();
        }

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

        Set<String> keys = salidasJuego.keySet();
        Salida salida;
        for (String key : keys) {
            salida = salidasJuego.get(key);
            Rectangle rectSalida = (Rectangle) salida.getArea().clone();
            rectSalida.x -= offSetX;
            rectSalida.y -= offSetY;
            graphics2D.draw(rectSalida);
        }


        if (slash != null) {
            Rectangle rectangle = (Rectangle) slash.clone();
            rectangle.x -= offSetX;
            rectangle.y -= offSetY;
            graphics2D.draw(rectangle);
        }

        //TERCER PINTADA: DETALLES
        graphics2D.drawImage(imageBufferDetailsJuego, -offSetX, -offSetY, null);
        graphics2D.drawImage(ui.draw(graphics2D), 0, 0, null);

        ui.drawArmor(graphics2D);


        for (Room r : salas
        ) {


            //DIBUJO DEL MINIMAPA (LOS RECTANGULITOS) (ROJO DONDE ESTA EL PLAYER, NEGROS LOS QUE NO HA VISITADO Y NEGROS RELLENOS LOS QUE SI)
            if (!ui.map) {
                //CENTRO: 410,80
                int offsetXMinimap = 410;
                int offsetYMinimap = 80;

                int casillacentroX = player.salaPlayer.x;
                int casillacentroY = player.salaPlayer.y;


                Rectangle casillaMinimap = new Rectangle(offsetXMinimap + ((r.x - casillacentroX) * 14), offsetYMinimap + ((r.y - casillacentroY) * 14), 10, 10);
                //System.out.println(casillaMinimap);
                if (uiRecMinimap.contains(casillaMinimap)) {
                    if (player.salaPlayer == r) {
                        graphics2D.setPaint(Color.RED);
                        graphics2D.draw(casillaMinimap);
                        if (r.isClear()) {
                            graphics2D.fill(casillaMinimap);
                            continue;
                        }
                    }

                    graphics2D.setPaint(Color.BLACK);
                    if (r.isVisited()) {
                        if (r.isClear()) {
                            graphics2D.setPaint(Color.BLACK);
                            graphics2D.fill(casillaMinimap);
                        }
                    } else if (r.isNear()) {
                        graphics2D.setPaint(Color.gray);
                        graphics2D.draw(casillaMinimap);
                    }
                }
            }

            //DIBUJO DEL MAPAAAAAAA (LOS RECTANGULITOS) (ROJO DONDE ESTA EL PLAYER, NEGROS LOS QUE NO HA VISITADO Y NEGROS RELLENOS LOS QUE SI)
            else if (ui.map) {
                //CENTRO: 410,80
                int offsetXMinimap = 240;
                int offsetYMinimap = 230;

                int casillacentroX = player.salaPlayer.x;
                int casillacentroY = player.salaPlayer.y;


                Rectangle mapa = new Rectangle(offsetXMinimap + ((r.x - casillacentroX) * 20), offsetYMinimap + ((r.y - casillacentroY) * 20), 17, 17);
                //System.out.println(casillaMinimap);
                if (map.contains(mapa)) {
                    if (player.salaPlayer == r) {
                        if (r.isNear()) {
                            graphics2D.setPaint(Color.GREEN);
                            graphics2D.draw(mapa);

                        }
                        if (r.isVisited()) {
                            graphics2D.setPaint(Color.RED);
                            graphics2D.draw(mapa);
                            if (r.isClear()) {
                                graphics2D.fill(mapa);
                                continue;
                            }
                        }
                    }
                    //TODO
                    graphics2D.setPaint(Color.BLACK);
                    if (r.isVisited()) {
                        if (r.isClear()) {
                            graphics2D.setPaint(Color.BLACK);
                            graphics2D.fill(mapa);
                        }
                    } else if (r.isNear()) {
                        graphics2D.setPaint(Color.gray);
                        graphics2D.draw(mapa);
                    }

                }

            }


//                graphics2D.setPaint(Color.BLACK);
//                graphics2D.draw(casillaMinimap);



        }
    }

    private void escape(Image image) {

        setBorder(new EmptyBorder(0, 0, 0, 0));


        backgroundPanel.setBounds(-6, -14, WIDTH, HEIGHT);
        add(backgroundPanel);
        backgroundPanel.setLayout(null);
        backgroundPanel.setBackground(Color.black);



        JButton guardar_y_salir = new JButton("Guardar y salir");
        guardar_y_salir.setBounds(181, 250, 150, 30);
        backgroundPanel.add(guardar_y_salir);
        guardar_y_salir.setFocusable(false);

        JButton volver_al_juego = new JButton("Volver al juego");
        volver_al_juego.setBounds(181, 345, 150, 30);
        backgroundPanel.add(volver_al_juego);
        volver_al_juego.setFocusable(false);




        volver_al_juego.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuEsc = false;
            }
        });



        JLabel backgroundLabel;
        Image a = new ImageIcon(image)
                .getImage();
        backgroundLabel = new JLabel(new ImageIcon(a));
        backgroundLabel.setBounds(-140, -110, WIDTH, HEIGHT);
        backgroundPanel.add(backgroundLabel);

        repaint();


    }

    public static void main(String[] args) {
        Juego juego = new Juego("res/jsonsMapasPruebas/1.json", "resources/terrain_atlas.png");
        JFrame frame = new JFrame("Sloanegate");                           //Frame = Marco         Creacion de ventana
        frame.setSize(500, 529);                                                   //Tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //Accion cuando cerramos la ventana
        frame.setResizable(false);                                                        //Negamos que la ventana pueda ser modificada en tamaño
        frame.add(juego);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("res/img/icon.png").getImage());    //Define el icono
        juego.start();


    }

    void start() {

        mainTimer.start();
    }
}
