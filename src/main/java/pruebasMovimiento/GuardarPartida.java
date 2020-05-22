package pruebasMovimiento;


import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;


public class GuardarPartida extends JPanel {

    JLabel background;

    public GuardarPartida(Juego juego, String slot) {


        Image a = new ImageIcon(getClass().getClassLoader().getResource("img/guardarCargarBackground.png"))
                .getImage();
        background = new JLabel(new ImageIcon(a));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(background);


    }


    public void savePlayer(Player player) {

        JSONObject playerJsonObject = new JSONObject();
        JSONObject game = new JSONObject();

        playerJsonObject.put("y", player.getY());
        playerJsonObject.put("x",player.getX());
        playerJsonObject.put("hp",player.getHp());

        for (Entity e:player.salaPlayer.entities
             ) {

            JSONObject entitiy = new JSONObject();
            entitiy.put("name",e.getName());
            entitiy.put("x",e.getX());
            entitiy.put("y",e.getY());
            entitiy.put("hp",e.getHp());
            entitiy.put("img",e.getImg());
            entitiy.put("hitbox",e.getHitbox());
            entitiy.put("spritePos",e.getSpritesPos());

            playerJsonObject.put("entity",entitiy);

        }

        //playerJsonObject.put("entities",player.getAddEntities());

        game.put("player",playerJsonObject);

        FileWriter file = null;
        try {
            file = new FileWriter("jsonplayer.json");
            file.write(playerJsonObject.toJSONString());
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(playerJsonObject);

        System.out.println(game);
    }


    public static void main(String[] args) {

//        Juego juego = new Juego("res/jsonsMapasPruebas/1.json", "resources/terrain_atlas.png");
//
//        GuardarPartida g1 = new GuardarPartida(juego, "slot1");
//
//        g1.savePlayer(juego.getPlayer());

    }

}
