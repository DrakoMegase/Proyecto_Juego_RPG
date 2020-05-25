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

    static private int nivel=1;

    //Atributos graficos
    static private UI ui;                                       //spriteSheet UIBuffImg
    static private Rectangle uiRecMinimap;
    static private Rectangle map;
    //static private int[][] spriteInts;                       //los numeritos de los sprites todo esto no me acaba
    private Stroke defStroke;     //Esto sirve para hacer los rectangulos (las lineas y eso)
    private JPanel backgroundPanel;

    //Atributos de camara
    static private int offSetX = 0;
    static private int offSetY = 0;

    //Atributos del juego
    static final private int TIMERDELAY = 10;        //Delay del timer
    static private Timer mainTimer;            //Declaracion de un timer
    private static ArrayList<Room> salas;
    private static Room salaActual;
    static boolean menuEsc;
    protected static boolean paintSt;
    private static JPanel padre;


    private Image imagenEscape;
    static Rectangle slash;

    //Constructor de la clase Juego
    Juego(String rutaJson) {

        //INICIALIZACION DE ENTITIES

//        Juego.padre = padre;

        player = new Player(400, 400, 24);

        cargarNuevoNivel(player);

        /*for (int i = 0; i < salasint.length; i++) {
            for (int j = 0; j < salasint.length; j++) {

                System.out.print(salasint[j][i] + "\t");

            }
            System.out.println();
        }*/


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
        imagenEscape = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/guardar1.png")))
                .getImage();
        backgroundPanel = new JPanel();


        //INICIALIZACION DE LAS LISTAS QUE USAREMOS
        //DAMOS LAS ENTITIES AL PLAYER

        //INICIACION DE LA UIBuffImg (siempre despies del player)


        //backgroundPanel = new JPanel();
        ui = new UI(player);
        uiRecMinimap = UI.getMinimapa();
        map = UI.getMapa();


//        player.setAddEntities(entitiesJuego);



        addKeyListener(new KeyAdapt(player));


        mainTimer = new Timer(TIMERDELAY, this);
        //mainTimer.start();  //Con esto ponemos a ejectuarse en bucle el actionPerfomed() de abajo.

        WIDTH=salaActual.width;
        HEIGHT=salaActual.height;

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

    private static void cargarNuevoNivel(Player player){
        salas = new ArrayList<>();
        Room[][] level = MapGenerator.generateMap(5*(1+nivel));
        Room sala;
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                if (level[i][j] != null) {
                    sala = level[i][j];
                    sala.inicializarSala(nivel);
                    salas.add(sala);
                    if (sala.salaClass == 0) {
                        salaActual = sala;
                        player.salaPlayer=sala;
                        salaActual.entities.add(player);
                    }
                }

            }
        }

        for (Room r : salas
        ) {
            if (r.player != null) {
                player.salaPlayer = r;
                r.setVisited(true);
            }

        }

        for (String s : player.salaPlayer.salidas.keySet()
        ) {

            Room r = player.salaPlayer.salidas.get(s).getConexion().getOrigen();
            r.setNear(true);

        }
    }

    static void siguienteNivel(){
        nivel++;
        player.hp=player.getMaxHp();
        cargarNuevoNivel(player);
    }


    private static void cargarSala(Room room, String exit) {


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


        //Limpiamos nuestras listas
        salaActual.entities.remove(player);


        salaActual=room;
        //Añadimos al jugador
        salaActual.entities.add(player);
        WIDTH=salaActual.width;
        HEIGHT=salaActual.height;

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

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        boolean clear=true;

        for (int i = 0; i < salaActual.entities.size(); ) {
            Entity entity = salaActual.entities.get(i);
            entity.update();
            if(!player.salaPlayer.clear&&clear&&entity instanceof Enemy){
                clear=false;
            }

            if (entity.remove) {
                salaActual.entities.remove(entity);
            } else {
                i++;
            }
        }

        if(!player.salaPlayer.clear&&clear){
            player.salaPlayer.clear=true;
        }


        Iterator<Entity> iterator = salaActual.entities.iterator();

        salaActual.entities.sort(new CompareNearEntities(salaActual.entities));
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            entity.checkCollisions(salaActual.entities, 0);
        }

        if (player.hp <= 0) {
            //Canbedamaged del player esta en false todo
            salaActual.entities.remove(player);
            System.out.println("FIN DE LA PARTIDA vida jugador es = " + player.hp);
//            padre.remove(this);
            GameOver gameOver = new GameOver();

        }

        if (player.salaPlayer.clear&&salaActual.salidas != null) {
            Set<String> keys = salaActual.salidas.keySet();
            Salida salida;
            for (String key : keys) {
                salida = salaActual.salidas.get(key);
                if (salida != null && !(salida instanceof Portal) && player.hitbox.intersects(salida.getArea())) {
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
            if(!s.equals("portal")) {
                Room r = player.salaPlayer.salidas.get(s).getConexion().getOrigen();
                r.setNear(true);
            }

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

        graphics2D.drawImage(salaActual.backgroundSala, -offSetX, -offSetY, null);

        if(salaActual.salidas.containsKey("portal")){
            ((Portal)salaActual.salidas.get("portal")).draw(graphics2D,offSetX,offSetY);
        }

        for (int i = 0; i < salaActual.objetosMapa.size(); i++) {
            salaActual.objetosMapa.get(i).drawIcon(offSetX,offSetY,graphics2D);
        }


        if (defStroke == null) {
            defStroke = graphics2D.getStroke();
        }

        //SEGUNDA PINTADA: ENTITIES
        salaActual.entities.sort(Entity::compareTo);
        for (Entity entity : salaActual.entities) {
            entity.draw(graphics2D, offSetX, offSetY);
            Rectangle rectangle = (Rectangle) entity.hitbox.clone();
            rectangle.x -= offSetX;
            rectangle.y -= offSetY;
            graphics2D.draw(rectangle);
            //graphics2D.draw(entity.hitbox);
        }

        Set<String> keys = salaActual.salidas.keySet();
        Salida salida;
        for (String key : keys) {
            salida = salaActual.salidas.get(key);
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
        graphics2D.drawImage(salaActual.detailsSala, -offSetX, -offSetY, null);
        graphics2D.drawImage(ui.draw(graphics2D), 0, 0, null);

        ui.drawIcons(graphics2D);


        for (Room r : salas
        ) {


            //DIBUJO DEL MINIMAPA (LOS RECTANGULITOS) (ROJO DONDE ESTA EL PLAYER, NEGROS LOS QUE NO HA VISITADO Y NEGROS RELLENOS LOS QUE SI)
            if (!UI.map) {
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
            else{
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
//        JFrame frame = new JFrame("Sloanegate");                           //Frame = Marco         Creacion de ventana
//        Juego juego = new Juego("res/jsonsMapasPruebas/1.json", "resources/terrain_atlas.png", frame);
//        frame.setSize(500, 529);                                                   //Tamaño de la ventana
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //Accion cuando cerramos la ventana
//        frame.setResizable(false);                                                        //Negamos que la ventana pueda ser modificada en tamaño
//        frame.add(juego);
//        frame.setVisible(true);
//        frame.setIconImage(new ImageIcon("res/img/icon.png").getImage());    //Define el icono
//        juego.start();
    }



    void start() {

        mainTimer.start();
    }


    //GETTERS

    public static Player getPlayer() {
        return player;
    }
}
