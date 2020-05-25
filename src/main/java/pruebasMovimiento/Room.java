package pruebasMovimiento;

import herramientas.ManipulacionDatos;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static herramientas.ExtraerDatosJson.*;
import static herramientas.ExtraerDatosJson.extraerValorJson;
import static pruebasMovimiento.Juego.*;

public class Room {

    private int idSala;
    int salaType;
    int salaClass;
    int x;
    int y;
    private static int contador = 0;
    BufferedImage backgroundSala;
    BufferedImage detailsSala;
    LinkedList<Entity>entities;
    private int distancia;
    HashMap<String,Salida> salidas;
    boolean clear;
    static private int[][] spriteInts;                   //los numeritos de los sprites todo esto no me acaba
    private static BufferedImage spriteSheet;
    Player player;
    LinkedList<ItemProperties> objetosMapa;
    private boolean visited;
    private boolean near;

    Room(int salaType) {
        this.salaType = salaType;
        this.idSala = contador;
        salidas = new HashMap<>();

        if (contador == 0) {
            player = Juego.player;
        }
        contador++;


    }

    Room(String rutaJsonRoom, String rutaSpriteSheet) {


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
        salidas = new HashMap<>();



        ManipulacionDatos.rectanglesToEntityObjects(rutaJsonRoom, entities);
//        salidas.addAll(salidasMapa(rutaJsonRoom));



        printBackground(backgroundSala, spriteInts);
        printBackgroundDetails(detailsSala, spriteInts);

        if (contador == 0) {
            player = Juego.player;
        }

        contador++;
        this.idSala = contador;
    }

    void inicializarSala(int nivel) {

        String rutaJsonRoom="res/jsonsestilo2/"+salaType+".json";
        String rutaSpriteSheet="res/img/terrain_atlas.png";

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
        objetosMapa = new LinkedList<>();


        ManipulacionDatos.rectanglesToEntityObjects(rutaJsonRoom, entities);


        switch (salaClass) {
            case 0:
                Weapon weapon=Weapon.createWeapon(4);
                weapon.getHitbox().x=WIDTH/2;
                weapon.getHitbox().y=WIDTH/2;
                System.out.println("inicio:"+x+"-"+y);
                objetosMapa.add(weapon);

                for (int i = 0; i < 3; i++) {
                    Armor armor = Armor.createArmor(i);
                    armor.getHitbox().x = WIDTH / 2 + 40 * (1 + i);
                    armor.getHitbox().y = WIDTH / 2;
                    objetosMapa.add(armor);
                }

                break;
            case 1:
                int max = HEIGHT - 100;
                int min = 100;
                int extra = (int) (Math.random() * 5);
                for (int i = 0; i < 4 + extra; i++) {
                    int posx = max, posy = max;
                    if (i % 2 == 0) {
                        posx = min;
                    }
                    if ((i / 2) % 2 == 0) {
                        posy = min;
                    }

                    entities.add(Enemy.createEnemy(nivel * 2 + (int) (Math.random() * 2), posx, posy, Juego.player));
                }
                break;
            case 2:
                System.out.println("tienda:"+x+"-"+y);
                entities.add(new Entity(WIDTH/2-16,HEIGHT/2-50,20,"img/enemies/BlackSmith.png",8,44,16,10,false,false));

                Weapon weapon2=Weapon.createWeapon(6);
                weapon2.getHitbox().x=WIDTH/2;
                weapon2.getHitbox().y=WIDTH/2;
                objetosMapa.add(new Buyable(weapon2));
                for (int i = 0; i < 3; i++) {
                    Armor armor = Armor.createArmor(i);
                    armor.getHitbox().x = WIDTH / 2 + 40 * (1 + i);
                    armor.getHitbox().y = WIDTH / 2;
                    objetosMapa.add(new Buyable(armor));
                }

                break;
            case 3:

                System.out.println("boss:"+x+"-"+y);
                switch (nivel){
                    case 2:
                        entities.add(Enemy.createEnemy(6, WIDTH / 2, HEIGHT / 2, Juego.player));
                        entities.add(Enemy.createEnemy(4, WIDTH / 2, HEIGHT / 2 + 100, Juego.player));
                        entities.add(Enemy.createEnemy(4, WIDTH / 2 - 50, HEIGHT / 2 - 50, Juego.player));
                        entities.add(Enemy.createEnemy(4, WIDTH / 2 + 50, HEIGHT / 2 - 50, Juego.player));
                        break;

                    case 1:
                        entities.add(Enemy.createEnemy(8,WIDTH/2,HEIGHT/2,Juego.player));
                        break;

                    case 0:
                        entities.add(Enemy.createEnemy(7, WIDTH / 2, HEIGHT / 2, Juego.player));
                }

                break;

        }


        HashMap<String, Salida> salidas1 = salidasMapa(rutaJsonRoom);
        Set<String> keySet = salidas1.keySet();
        for (String key :
                keySet) {
            salidas.get(key).setArea(salidas1.get(key).getArea());
        }

        printBackground(backgroundSala, spriteInts);
        printBackgroundDetails(detailsSala, spriteInts);
    }


    private BufferedImage printBackground(BufferedImage imageBuffer, int[][] spriteInts) {


        int capas = spriteInts.length;
        int spritesPorCapa = COLUMNS * ROWS;

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        for (int j = 0; j < capas - 1; j++) {
            int x = 0;
            int y = -1;

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

    boolean isVisited() {
        return visited;
    }

    void setVisited(boolean visited) {
        this.visited = visited;
    }

    boolean isNear() {
        return near;
    }

    void setNear(boolean near) {
        this.near = near;
    }

    private BufferedImage printBackgroundDetails(BufferedImage imageBuffer, int spriteInts[][]) {


        int capas = spriteInts.length;
        int spritesPorCapa = COLUMNS * ROWS;

        Graphics2D graphics = (Graphics2D) imageBuffer.getGraphics();

        int x = 0;
        int y = -1;

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

    void setSalaType(int salaType) {
        this.salaType = salaType;
    }

    int getDistancia() {
        return distancia;
    }

    void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    boolean isClear() {
        return clear;
    }

    @Override
    public String toString() {
        return "Room{" +
                "idSala=" + idSala +
                ", salidas=" + salidas +
                ", clear=" + clear +
                "} \n";
    }


}
