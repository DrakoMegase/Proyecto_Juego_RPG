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
    int width;
    int height;
    private static int contador = 0;
    BufferedImage backgroundSala;
    BufferedImage detailsSala;
    LinkedList<Entity>entities;
    private int distancia;
    HashMap<String,Salida> salidas;
    boolean clear;
    static private int[][] spriteInts;                   //los numeritos de los sprites
    private static BufferedImage spriteSheet;
    Player player;
    LinkedList<ItemProperties> objetosMapa;
    private boolean visited;
    private boolean near;

    public int getIdSala() {
        return idSala;
    }

    Room(int salaType) {
        this.salaType = salaType;
        this.idSala = contador;
        salidas = new HashMap<>();
        entities = new LinkedList<>();
        objetosMapa = new LinkedList<>();
        contador++;
    }

    void inicializarSala(int nivel, boolean addEntities) {



        String rutaJsonRoom="";

        switch (nivel){
            case 0:
                rutaJsonRoom="jsonsestilo3/"+salaType+".json";
                break;
            case 1:
                rutaJsonRoom="jsonsestilo2/"+salaType+".json";
                break;
            case 2:
                rutaJsonRoom="jsonsMapasPruebas/"+salaType+".json";
                break;
        }

        String rutaSpriteSheet="img/terrain_atlas.png";

        TILESIZE = Integer.parseInt(extraerValorJson(rutaJsonRoom, "tileheight"));
        ROWS = Integer.parseInt(extraerValorJson(rutaJsonRoom, "height"));
        COLUMNS = Integer.parseInt(extraerValorJson(rutaJsonRoom, "width"));
        width = COLUMNS * TILESIZE;
        height = ROWS * TILESIZE;

        spriteInts = devolverNumSpritesTotal(arraysSprites(rutaJsonRoom));  //Poner un iterador que separe las capas HECHO
        try {
            spriteSheet = ImageIO.read(this.getClass().getClassLoader().getResource(rutaSpriteSheet));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundSala = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        detailsSala = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);


        ManipulacionDatos.rectanglesToEntityObjects(rutaJsonRoom, entities);
//        System.out.println(x+"-"+y+" "+ entities.size());

        if(salaClass==2){
            entities.add(new Entity(width / 2 - 16, height / 2 + 50, 20, "img/enemies/BlackSmith.png", 8, 44, 16, 10, false, false));
        }

        Random random=new Random();

        if(addEntities) {
            switch (salaClass) {
                case 0:

                    System.out.println("inicio:" + x + "-" + y);

                    break;
                case 1:
                    int min = 300;
                    int max = height - min;
                    int extra = random.nextInt(1) * 5;
                    for (int i = 0; i < 4 + extra; i++) {
                        int posx = max, posy = max;
                        if (i % 2 == 0) {
                            posx = min;
                        }
                        if ((i / 2) % 2 == 0) {
                            posy = min;
                        }

                        entities.add(Enemy.createEnemy(nivel * 2 + random.nextInt(2), posx, posy, Juego.player));
                    }
                    break;
                case 2:
                    System.out.println("tienda:" + x + "-" + y);

                    int j=0;
                    if (nivel != 0) {
                        int randomNum = (int) (random.nextInt(1) * 2);

                        Weapon weapon2 = Weapon.createWeapon(5 * randomNum + nivel);
                        weapon2.getHitbox().x = width / 2;
                        weapon2.getHitbox().y = height / 2 + 100;
                        objetosMapa.add(new Buyable(weapon2));
                    }else{
                        j=1;
                    }
                    for (int i = 0; i < 3-j; i++) {
                        Armor armor = Armor.createArmor(i + (3 * nivel));
                        armor.getHitbox().x = width / 2 + 40 * (1 + i);
                        armor.getHitbox().y = height / 2 + 100;
                        objetosMapa.add(new Buyable(armor));
                    }

                    break;
                case 3:

                    System.out.println("boss:" + x + "-" + y);
                    switch (nivel) {
                        case 2:
                            entities.add(Enemy.createEnemy(6, width / 2, height / 2, Juego.player));
                            entities.add(Enemy.createEnemy(4, width / 2, height / 2 + 100, Juego.player));
                            entities.add(Enemy.createEnemy(4, width / 2 - 50, height / 2 - 50, Juego.player));
                            entities.add(Enemy.createEnemy(4, width / 2 + 50, height / 2 - 50, Juego.player));
                            break;

                        case 1:
                            entities.add(Enemy.createEnemy(8, width / 2, height / 2, Juego.player));
                            break;

                        case 0:
                            entities.add(Enemy.createEnemy(7, width / 2, height / 2, Juego.player));
                    }

                    break;

            }
        }


        HashMap<String, Salida> salidasJSON = salidasMapa(rutaJsonRoom);
        Set<Map.Entry<String,Salida>> set= salidas.entrySet();
        Salida salida;
        System.out.println(salaType+"-"+set.size());
        for (Map.Entry<String,Salida> key : set) {
            if(key!=null&&key.getValue()!=null&&key.getKey()!=null) {
                salida=salidasJSON.get(key.getKey());
                if(salida!=null) {
                    key.getValue().setArea(salida.getArea());
                }
            }
        }

        if(salaClass==3&&clear&&!salidas.containsKey("portal")){
            spawnNextLevelPortal();
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

    void spawnNextLevelPortal(){
        Portal portal=Portal.newPortal(width,height);
        salidas.put("portal",portal);
        Entity entity;
        boolean moved=false;
        for (int i = 0; i < entities.size()&&!moved; i++) {
            entity=entities.get(i);
            if(!(entity instanceof Player)&&!(entity instanceof Enemy)&&portal.getArea().intersects(entity.hitbox)){
                int [] force=Entity.intersect(portal.getArea(),entity.hitbox);
                portal.getArea().x-=force[0];
                portal.getArea().y-=force[1];
                moved=true;
            }
        }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return idSala == room.idSala;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSala);
    }

    @Override
    public String toString() {
        return "Room{" +
                "idSala=" + idSala +
                ", salidas=" + salidas +
                ", clear=" + clear +
                "} \n";
    }

    public static void setContador(int contador) {
        Room.contador = contador;
    }
}
