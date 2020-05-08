package pruebasMovimiento;

import javax.imageio.ImageIO;
import java.awt.*;
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


    BufferedImage UIImage;
    Player player;
    double danyobloq, vidaMaxima, energiaMax, expMax;
    Rectangle energiaTotRect, experienciaTotRect, vidaTotalRect, armorTotRect;

    Stroke stroke2;     //Esto sirve para hacer los rectangulos (las lineas y eso)


    public UI(Player player) {

        try {
            UIImage = ImageIO.read(new File("res/img/UI_juego.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stroke2 = new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, new float[]{10f, 7f}, 3.0f);

        //INICIAMOS TODOS LOS RECTANGULOS DE PARAMETROS MAXIMOS SOLO
        this.player = player;
        this.vidaMaxima = player.hp;
        this.energiaMax = player.getEnergia();

        this.vidaTotalRect = new Rectangle(41, 6, 100, 20);
        this.armorTotRect = new Rectangle(41, 33, 100, 20);
        this.energiaTotRect = new Rectangle(41, 63, 100, 20);
        this.experienciaTotRect = new Rectangle(41, 93, 100, 20);

    }

    void drawBarra(Graphics2D g, String tipo, Player p) {


        tipo = tipo.toLowerCase();
        Stroke defStroke = g.getStroke();


        g.setColor(Color.black);
        g.setStroke(stroke2);

        switch (tipo) {

            case "vida":
                Rectangle vidaActual = new Rectangle(40, 5, (int) ((double) player.hp / vidaMaxima * 100) + 2, 22);

                //DIBUJAMOS PRIMERO RECTANGULO DISCONTINUO
                g.draw(vidaTotalRect);

                //RECTANGULO RELLENO
                g.setStroke(defStroke);
                g.setPaint(Color.RED);
                g.fill(vidaActual);
                g.draw(vidaActual);
                break;

            case "armor":

                danyobloq = 1.0 - (100.0 / (100.0 + (double) player.getArmorInt()));

                //System.out.println(danyobloq);

                Rectangle armorActual = new Rectangle(40, 32, (int) (danyobloq * 100 + 2), 22);


                //DIBUJAMOS PRIMERO RECTANGULO DISCONTINUO
                g.draw(armorTotRect);

                //RECTANGULO RELLENO
                g.setStroke(defStroke);
                g.setPaint(Color.BLUE);
                g.fill(armorActual);
                g.draw(armorActual);

                break;

            case "energia":


                if (player.getEnergia() <= 0){
                    g.draw(energiaTotRect);
                    g.setStroke(defStroke);
                    break;
                }

                Rectangle energAct = new Rectangle(40, 62, (int) ((double) player.getEnergia() / energiaMax * 100) + 2, 22);


                //DIBUJAMOS PRIMERO RECTANGULO DISCONTINUO
                g.draw(energiaTotRect);

                //RECTANGULO RELLENO
                g.setStroke(defStroke);
                g.setPaint(Color.YELLOW);
                g.fill(energAct);
                g.draw(energAct);

                break;

            case "exp":
                Rectangle expActual = new Rectangle(40, 92, (int) ((double) player.getExperiencia() * 10) + 2, 23);

                //DIBUJAMOS PRIMERO RECTANGULO DISCONTINUO
                g.draw(experienciaTotRect);

                //RECTANGULO RELLENO
                g.setStroke(defStroke);
                g.setPaint(Color.GREEN);
                g.fill(expActual);
                g.draw(expActual);
                break;
            default:
                break;

        }


    }

    public BufferedImage getUIImage() {
        return UIImage;
    }
}
