package pruebasMovimiento;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;

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

    static private int nivel = 0;


    //Atributos graficos
    static private UI ui;                                       //spriteSheet UIBuffImg
    static private Rectangle uiRecMinimap;
    static private Rectangle map;
    //static private int[][] spriteInts;                       //los numeritos de los sprites
    private Stroke defStroke;     //Esto sirve para hacer los rectangulos (las lineas y eso)
    private JPanel backgroundPanel;
    private GameOver gameOver;
    //Atributos de camara
    static private int offSetX = 0;
    static private int offSetY = 0;
    static private Juego juego;

    //Atributos del juego
    static final private int TIMERDELAY = 10;        //Delay del timer
    static Timer mainTimer;            //Declaracion de un timer
    private static ArrayList<Room> salas;
    private static Room salaActual;
    static boolean menuEsc;
    private int contador;
    private boolean endgame;
    static int mounstruosKilled;
    private Image imagenEscape;
    static Rectangle slash;

    static Menu menu;

    //Constructor de la clase Juego
    Juego(Menu menu) {

        mounstruosKilled = 0;
        juego=this;


        nivel=0;
        menu.musica("music/nivel"+(nivel+1)+".wav");

        Juego.menu = menu;


        player = new Player(400, 400, 24);

        cargarNuevoNivel(player);


        setLayout(new BorderLayout());                          //Añadimos un diseño de ventana añadiendole eun gestor
        setVisible(true);                                       //Hacemos la ventana visible
        setBackground(Color.black);
        setFocusable(true);                                     //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        setLayout(null);



        //TODO
        imagenEscape = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/guardar1.png")))
                .getImage();
        backgroundPanel = new JPanel();


        //INICIALIZACION DE LAS LISTAS QUE USAREMOS
        //DAMOS LAS ENTITIES AL PLAYER

        //INICIACION DE LA UIBuffImg (siempre despies del player)


        ui = new UI(player);
        uiRecMinimap = UI.getMinimapa();
        map = UI.getMapa();


        mainTimer = new Timer(TIMERDELAY, this);
        WIDTH = salaActual.width;
        HEIGHT = salaActual.height;

    }

    Juego(Player player, ArrayList<Room> salas, int nivel, Menu menu, long mounstruosKilled) {

        menu.musica("music/nivel"+(nivel+1)+".wav");

        juego=this;

        Juego.menu = menu;

        Juego.mounstruosKilled = (int) mounstruosKilled;
        Juego.nivel = nivel;
        Juego.salas = salas;
        Juego.player = player;
        Juego.salaActual = player.salaPlayer;


        setLayout(new BorderLayout());                          //Añadimos un diseño de ventana añadiendole eun gestor
        setVisible(true);                                       //Hacemos la ventana visible
        setBackground(Color.black);
        setFocusable(true);                                     //Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        setLayout(null);

        //TODO
        imagenEscape = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/guardar1.png")))
                .getImage();
        backgroundPanel = new JPanel();


        //INICIALIZACION DE LAS LISTAS QUE USAREMOS
        //DAMOS LAS ENTITIES AL PLAYER

        //INICIACION DE LA UIBuffImg (siempre despies del player)


        ui = new UI(player);
        uiRecMinimap = UI.getMinimapa();
        map = UI.getMapa();

        mainTimer = new Timer(TIMERDELAY, this);

        WIDTH = salaActual.width;
        HEIGHT = salaActual.height;
        mainTimer.start();



    }

    private ArrayList<Room> cargarSalasNivel(ArrayList<Room> salas) {


        return salas;
    }

    private void camaraUpdate() {

        offSetX = player.getX() - this.getWidth() / 2 + player.hitbox.width;
        if(offSetX<0){
            offSetX=0;
        }else if(offSetX+this.getWidth()>salaActual.width){
            offSetX=salaActual.width-this.getWidth();
        }
        offSetY = player.getY() - this.getHeight() / 2 + player.hitbox.height;
        if(offSetY<0){
            offSetY=0;
        }else if(offSetY+this.getHeight()>salaActual.height){
            offSetY=salaActual.height-this.getHeight();
        }

    }

    private static void cargarNuevoNivel(Player player) {
        salas = new ArrayList<>();
        Room[][] level = MapGenerator.generateMap(5 * (1 + nivel));
        for(Room[] array:level) {
            for (Room sala : array) {
                if(sala!=null) {
                    sala.inicializarSala(nivel, true);
                    salas.add(sala);
                    if (sala.salaClass == 0) {
                        salaActual = sala;
                        player.salaPlayer = sala;
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

    static void siguienteNivel() {
        nivel++;
        System.out.println("siguiente nivel");
        player.hp = player.getMaxHp();
        if(nivel!=3) {
            player.puntuacion+=1000;
            cargarNuevoNivel(player);
            menu.musica("music/nivel"+(nivel+1)+".wav");
        }else {
            player.puntuacion+=2000;
        }
    }


    private static void cargarSala(Room room, String exit) {


        /*
        Al cargar una sala utilizaremos el siguiente criterio:
        Las salas estan formadas por 3 elementos (a grosso modo):
        1.El background (una BufferedImage)
        2.Los detalles (donde el personaje se pinta antes que estos) Otra bufferedImage
        3.Todos los entities, aqui tenemos tanto los obstaculos como los enemies y el jugador.
        */

        //Limpiamos nuestras listas
        salaActual.entities.remove(player);

        salaActual = room;
        //Añadimos al jugador
        salaActual.entities.add(player);
        WIDTH = salaActual.width;
        HEIGHT = salaActual.height;

        switch (exit) {
            case "2":
                player.setPos(player.x, 100);
                break;
            case "1":
                player.setPos(player.x, HEIGHT - 100 - player.hitbox.height);
                break;
            case "4":
                player.setPos(WIDTH - 100 - player.hitbox.width, player.y);
                break;
            case "3":
                player.setPos(100, player.y);
                break;

        }

        if(salaActual.salaClass==3&&!salaActual.clear){
            menu.musica("music/boss.wav");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        boolean clear = true;

        if (player.hp <= 0 || nivel==3) {
            gameover();
        }

        if (menuEsc) {
            //JPANEL y añadir ahi el menu de pausa.

            escape(imagenEscape);
            repaint();
            return;
        }

        for (int i = 0; i < salaActual.entities.size(); ) {
            Entity entity = salaActual.entities.get(i);
            entity.update();
            if (!player.salaPlayer.clear && clear && entity instanceof Enemy) {
                clear = false;
            }

            if (entity.remove) {
                salaActual.entities.remove(entity);
                mounstruosKilled++;
            } else {
                i++;
            }
        }

        if (!player.salaPlayer.clear && clear) {
            player.salaPlayer.clear = true;
            Entity.playSound("sounds/clearRoom.wav");
        }

        Iterator<Entity> iterator = salaActual.entities.iterator();

        salaActual.entities.sort(new CompareNearEntities(salaActual.entities));
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            entity.checkCollisions(salaActual.entities, 0);
        }

        if (player.salaPlayer.clear && salaActual.salidas != null) {
            Set<String> keys = salaActual.salidas.keySet();
            Salida salida;
            for (String key : keys) {
                salida = salaActual.salidas.get(key);
                if (salida != null && !(salida instanceof Portal) && salida.getArea()!=null && player.hitbox.intersects(salida.getArea())) {
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
            if (!s.equals("portal")) {
                Room r = player.salaPlayer.salidas.get(s).getConexion().getOrigen();
                r.setNear(true);
            }
        }


        repaint();

    }

    private void gameover() {

        if (contador > 150 && !endgame) {
            gameOver = new GameOver(mounstruosKilled, player, menu);
            menu.remove(this);
            menu.setContentPane(gameOver);
            if(nivel==3){
                menu.musica("music/victory.wav");
            }
            validate();
            menu.setVisible(true);
            endgame = true;

        }

        if (endgame) {
            gameOver.aparicion(contador);
        }
        salaActual.entities.remove(player);


        contador++;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        remove(backgroundPanel);
        camaraUpdate();
        Graphics2D graphics2D = (Graphics2D) g;
        if (menuEsc) {
            escape(imagenEscape);
            repaint();
            return;
        }

        //PRIMERA PINTADA: FONDO

        graphics2D.drawImage(salaActual.backgroundSala, -offSetX, -offSetY, null);

        if (salaActual.salidas.containsKey("portal")) {
            ((Portal) salaActual.salidas.get("portal")).draw(graphics2D, offSetX, offSetY);
        }

        for (int i = 0; i < salaActual.objetosMapa.size(); i++) {
            ItemProperties itemProperties = salaActual.objetosMapa.get(i);
            Rectangle rectangle = (Rectangle) itemProperties.getHitbox().clone();
            rectangle.x -= offSetX;
            rectangle.y -= offSetY;
            graphics2D.setColor(Color.DARK_GRAY);
            graphics2D.fill(rectangle);
            itemProperties.drawIcon(offSetX, offSetY, graphics2D);
        }
        graphics2D.setColor(Color.black);


        if (defStroke == null) {
            defStroke = graphics2D.getStroke();
        }


        //SEGUNDA PINTADA: ENTITIES
        salaActual.entities.sort(Entity::compareTo);
        for (Entity entity : salaActual.entities) {
            entity.draw(graphics2D, offSetX, offSetY);
        }

        for (int i = 0; i < salaActual.objetosMapa.size(); i++) {
            if(player.hitbox.intersects(salaActual.objetosMapa.get(i).getHitbox())){
                graphics2D.setColor(Color.white);
                g.setFont(new Font("TimesRoman", Font.BOLD, 15));
                graphics2D.drawString("Espacio",player.x-offSetX+5,player.y-offSetY+10);
            }
        }

        if(salaActual.salidas.containsKey("portal")){
            if(player.hitbox.intersects(salaActual.salidas.get("portal").getArea())){
                graphics2D.setColor(Color.white);
                g.setFont(new Font("TimesRoman", Font.BOLD, 15));
                graphics2D.drawString("Espacio",player.x-offSetX+5,player.y-offSetY+10);
            }
        }

        //TERCER PINTADA: DETALLES
        graphics2D.drawImage(salaActual.detailsSala, -offSetX, -offSetY, null);
        graphics2D.drawImage(ui.draw(graphics2D), 0, 0, null);

        ui.drawIcons(graphics2D);

        drawMinimap(graphics2D);
    }

    private void drawMinimap(Graphics2D graphics2D){
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
                if (uiRecMinimap.contains(casillaMinimap)) {
                    if (player.salaPlayer == r) {
                        graphics2D.setPaint(Color.RED);
                        graphics2D.draw(casillaMinimap);
                        if (r.isClear()) {
                            graphics2D.fill(casillaMinimap);
                            continue;
                        }
                    }

                    if (r.isVisited()) {
                        if (r.isClear()) {
                            Color color;
                            switch (r.salaClass) {
                                case 0:
                                    color = Color.gray;
                                    break;
                                case 2:
                                    color = Color.YELLOW.darker();
                                    break;
                                case 3:
                                    color = Color.MAGENTA.darker();
                                    break;

                                default:
                                    color = Color.black;
                            }

                            graphics2D.setPaint(color);
                            graphics2D.fill(casillaMinimap);
                        }
                    } else if (r.isNear()) {
                        graphics2D.setPaint(Color.gray);
                        graphics2D.draw(casillaMinimap);
                    }
                }
            }

            //DIBUJO DEL MAPAAAAAAA (LOS RECTANGULITOS) (ROJO DONDE ESTA EL PLAYER, NEGROS LOS QUE NO HA VISITADO Y NEGROS RELLENOS LOS QUE SI)
            else {
                int offsetXMinimap = 240;
                int offsetYMinimap = 230;

                int casillacentroX = player.salaPlayer.x;
                int casillacentroY = player.salaPlayer.y;


                Rectangle mapa = new Rectangle(offsetXMinimap + ((r.x - casillacentroX) * 20), offsetYMinimap + ((r.y - casillacentroY) * 20), 17, 17);
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
                    graphics2D.setPaint(Color.BLACK);
                    if (r.isVisited()) {
                        if (r.isClear()) {
                            Color color;
                            switch (r.salaClass) {
                                case 0:
                                    color = Color.gray;
                                    break;
                                case 2:
                                    color = Color.YELLOW.darker();
                                    break;
                                case 3:
                                    color = Color.MAGENTA.darker();
                                    break;

                                default:
                                    color = Color.black;
                            }

                            graphics2D.setPaint(color);
                            graphics2D.fill(mapa);
                        }
                    } else if (r.isNear()) {
                        graphics2D.setPaint(Color.gray);
                        graphics2D.draw(mapa);
                    }

                }

            }
        }
    }

    private void escape(Image image) {

        backgroundPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        add(backgroundPanel);
        backgroundPanel.setLayout(null);
        backgroundPanel.setBackground(Color.black);

        JButton guardar_y_salir = new JButton("Guardar y salir");
        guardar_y_salir.setBounds(181, 215, 150, 30);
        backgroundPanel.add(guardar_y_salir);
        guardar_y_salir.setFocusable(false);
        guardar_y_salir.setOpaque(false);
        guardar_y_salir.setContentAreaFilled(false);
        guardar_y_salir.setForeground(Color.WHITE);

        guardar_y_salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuardarPartida guardarPartida=new GuardarPartida(juego);
                menu.remove(juego);
                menu.setContentPane(guardarPartida);
                validate();
                menu.setVisible(true);
            }
        });

        JButton volver_al_juego = new JButton("Volver al juego");
        volver_al_juego.setBounds(181, 310, 150, 30);
        volver_al_juego.setOpaque(false);
        volver_al_juego.setContentAreaFilled(false);
        volver_al_juego.setForeground(Color.WHITE);
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
        backgroundLabel.setBounds(0, -30, this.getWidth(), this.getHeight()+30);
        backgroundPanel.add(backgroundLabel);

        repaint();


    }

    void start() {

        System.out.println("STARTING GAME");
        mainTimer.start();

    }


    //GETTERS

    public static Player getPlayer() {
        return player;
    }

    static ArrayList<Room> getSalas() {
        return salas;
    }

    static int getNivel() {
        return nivel;
    }
}
