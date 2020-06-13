package pruebasMovimiento;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UI {

    /*
    UI = User Interface
    Esta estara formada por [ZONAS]:

        [ARRIBA IZQUIERDA]
                -Vida del jugador (arriba izquierda
                -Armadura del jugador
                -Experiencia del jugador
                -Energia del jugador (mana)

        [ABAJO IZQUIERDA]
                -Objetos y habilidades
                -Mapa
        [ARRIBA DERECHA]
                -Minimapa

        [CENTRO DERECHA]
                -Equipo

     */



    /*
    ESCAPE MENU:

    BOTON 1:

        GUARDAR Y SALIR

    BOTON 2:

        VOLVER AL JUEGO


     */

    public static boolean map;
    private BufferedImage UIImage;
    private BufferedImage UIImageMap;
    private Player player;
    private Rectangle energiaTotRect, experienciaTotRect, vidaTotalRect, armorTotRect;
    static private Rectangle minimapa, mapa;

    private Stroke stroke2;     //Esto sirve para hacer los rectangulos (las lineas y eso)






    UI(Player player) {

        try {
            UIImage = ImageIO.read(this.getClass().getClassLoader().getResource("img/UI_juego.png"));
            UIImageMap = ImageIO.read(this.getClass().getClassLoader().getResource("img/UI_juego_map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        minimapa = new Rectangle(358,23,115,115);
        mapa = new Rectangle(94,72,300,300);
        stroke2 = new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, new float[]{10f, 7f}, 3.0f);

        //INICIAMOS TODOS LOS RECTANGULOS DE PARAMETROS MAXIMOS SOLO
        this.player = player;

        this.vidaTotalRect = new Rectangle(41, 6, 100, 20);
        this.armorTotRect = new Rectangle(41, 33, 100, 20);
        this.energiaTotRect = new Rectangle(41, 63, 100, 20);
        this.experienciaTotRect = new Rectangle(41, 93, 100, 20);

    }

    static Rectangle getMinimapa() {
        return minimapa;
    }

    static Rectangle getMapa() {
        return mapa;
    }



    private void drawBarra(Graphics2D g, String tipo, Player p) {


        tipo = tipo.toLowerCase();
        Stroke defStroke = g.getStroke();


        g.setColor(Color.black);
        g.setStroke(stroke2);

        switch (tipo) {

            case "vida":
                Rectangle vidaActual = new Rectangle(40, 5, (int) ((double) p.hp / p.getMaxHp() * 100) + 2, 22);

                //DIBUJAMOS PRIMERO RECTANGULO DISCONTINUO
                g.draw(vidaTotalRect);

                //RECTANGULO RELLENO
                g.setStroke(defStroke);
                g.setPaint(Color.RED);
                g.fill(vidaActual);
                g.setPaint(Color.black);
                g.draw(vidaActual);
                break;

            case "armor":

                double danyobloq = 1.0 - p.getDmgRecived();

                //System.out.println(danyobloq);

                Rectangle armorActual = new Rectangle(40, 32, (int) (danyobloq * 100 + 2), 22);


                //DIBUJAMOS PRIMERO RECTANGULO DISCONTINUO
                g.draw(armorTotRect);

                //RECTANGULO RELLENO
                g.setStroke(defStroke);
                g.setPaint(Color.BLUE);
                g.fill(armorActual);
                g.setPaint(Color.black);
                g.draw(armorActual);

                break;

            case "energia":


                if (p.getEnergia() <= 0) {
                    g.draw(energiaTotRect);
                    g.setStroke(defStroke);
                    break;
                }

                Rectangle energAct = new Rectangle(40, 62, (int) (( p.getEnergia() /(double) p.getMaxEnergy()) * 100 + 2), 22);


                //DIBUJAMOS PRIMERO RECTANGULO DISCONTINUO
                g.draw(energiaTotRect);

                //RECTANGULO RELLENO
                g.setStroke(defStroke);
                g.setPaint(Color.YELLOW);
                g.fill(energAct);
                g.setPaint(Color.black);
                g.draw(energAct);

                break;

            case "exp":

                Rectangle expActual = new Rectangle(40, 92, (int)(experienciaTotRect.width*(p.getExperiencia()/(double)p.getExpMax())), 23);

                //DIBUJAMOS PRIMERO RECTANGULO DISCONTINUO
                g.draw(experienciaTotRect);

                //RECTANGULO RELLENO
                g.setStroke(defStroke);
                g.setPaint(Color.GREEN);
                g.fill(expActual);
                g.setPaint(Color.BLACK);
                g.draw(expActual);

                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.drawString(p.getLevel() + "", 55, 146);
                g.setStroke(defStroke);

                break;

            case "dinero":
                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.drawString(p.dinero + "", 405, 467);
                g.setStroke(defStroke);


                break;

            default:
                break;


        }
    }

    public BufferedImage getUIImage() {
        return UIImage;
    }


    private BufferedImage minimap(Graphics2D g){


        if (map) {

            g.draw(mapa);
            return UIImageMap;
        }

        g.draw(minimapa);
        return UIImage;

    }

    BufferedImage draw(Graphics2D graphics2D) {

        drawBarra(graphics2D, "vida", player);
        drawBarra(graphics2D, "energia",player);
        drawBarra(graphics2D, "armor",player);
        drawBarra(graphics2D, "exp",player);
        drawBarra(graphics2D, "dinero",player);

        return minimap(graphics2D);




    }

    void drawIcons(Graphics2D graphics2D){
        Armor[] armors=player.getArmor();
        for (int i = 0; i < 3; i++) {
            if(armors[i]!=null){
                armors[i].drawIcon(graphics2D,437,198+41*i);
            }
        }

        Weapon[] weapons=player.getWeapons();
        for (int i = 0; i < 2; i++) {
            if(weapons[i]!=null){
                weapons[i].drawIcon(graphics2D,17+weapons[i].getWeaponType()*41,439);
            }
        }

        for (int i = 0; i < 2; i++) {
            drawSkillIcon(graphics2D,i,106+42*i,439);
        }

    }

     private void drawSkillIcon(Graphics2D graphics2D, int id, int x, int y){
        // Width and height of sprite
        int sw = 30;
        int sh = 30;
        // Coordinates of desired sprite image
        int i = 30*id;
        int j = 30;
        graphics2D.drawImage(Player.ICONS, x,y, x+sw,y+sh, i, j, i+sw, j+sh, null);

        if(player.level<2+2*id){
            i=0;
            j=60;
            graphics2D.drawImage(Player.ICONS, x,y, x+sw,y+sh, i, j, i+sw, j+sh, null);
        }
    }
}
