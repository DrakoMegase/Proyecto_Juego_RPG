package pruebasMovimiento;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.util.*;


public class GuardarPartida extends JPanel {

    JLabel background;

    public GuardarPartida(Juego juego, String slot) {


        Image a = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/guardarCargarBackground.png")))
                .getImage();
        background = new JLabel(new ImageIcon(a));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(background);


    }

    static void save(int slot){
        JSONObject save=new JSONObject();

        savePlayer(Juego.player,save);

        saveMap(Juego.getSalas(),save);

        FileWriter file = null;
        try {
            file = new FileWriter("save"+slot+".json");
            file.write(save.toJSONString());
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void savePlayer(Player player, JSONObject save) {

        JSONObject playerJsonObject = new JSONObject();

        playerJsonObject.put("y", player.getY());
        playerJsonObject.put("x",player.getX());
        playerJsonObject.put("hp",player.getHp());
        playerJsonObject.put("dinero",player.dinero);
        playerJsonObject.put("exp",player.experiencia);
        playerJsonObject.put("lvl",player.level);
        playerJsonObject.put("puntuacion",player.puntuacion);
        playerJsonObject.put("armor", Arrays.deepToString(player.getArmor()));
        playerJsonObject.put("weapons", Arrays.deepToString(player.getWeapons()));
        playerJsonObject.put("idSala",player.salaPlayer.getIdSala());

        save.put("player",playerJsonObject);

    }

    private static void saveMap(ArrayList<Room> salas, JSONObject save){

        JSONObject mapJsonObject = new JSONObject();
        JSONObject sala;
        JSONObject entities;
        JSONObject entity;
        JSONObject salidas;
        JSONObject salida;
        JSONObject conexion;
        Enemy enemy;
        Salida exit;

        for(Room room:salas){
            sala=new JSONObject();
            sala.put("id",room.getIdSala());
            sala.put("x",room.x);
            sala.put("y",room.y);
            sala.put("type",room.salaType);
            sala.put("class",room.salaClass);
            sala.put("clear",room.clear);
            sala.put("visited",room.isVisited());
            sala.put("near",room.isNear());
            entities=new JSONObject();

            int count=0;
            for(Entity e:room.entities){
                if(e instanceof Enemy){
                    entity=new JSONObject();
                    enemy=(Enemy)e;

                    entity.put("id",enemy.id);
                    entity.put("x",enemy.x);
                    entity.put("y",enemy.y);

                    entities.put(""+count++,entity);
                }
            }
            sala.put("entities",entities);

            salidas=new JSONObject();
            Set<String> keySet= room.salidas.keySet();

            for (String key:keySet){
                if(!key.equals("portal")) {
                    exit = room.salidas.get(key);
                    salida = new JSONObject();
                    conexion = new JSONObject();

                    salida.put("val", exit.getVal());

                    conexion.put("val", exit.getConexion().getVal());
                    conexion.put("sala", exit.getConexion().getOrigen().getIdSala());

                    salida.put("conexion", conexion);

                    salidas.put(exit.getVal() + "", salida);
                }
            }
            sala.put("salidas",salidas);

            JSONObject objetosMapa=new JSONObject();
            JSONObject objeto;
            count=0;
            for (ItemProperties itemProperties:room.objetosMapa){
                objeto=new JSONObject();
                if(itemProperties instanceof Buyable){
                    objeto.put("class","Buyable");
                    Buyable buyable=(Buyable)itemProperties;
                    if(buyable.item instanceof Weapon){
                        objeto.put("itemClass","Weapon");
                    }else {
                        objeto.put("itemClass","Armor");
                    }

                    objeto.put("itemId",buyable.item.id);
                }else if(itemProperties instanceof Weapon){
                    objeto.put("class","Weapon");
                    Weapon weapon=(Weapon)itemProperties;
                    objeto.put("itemId",weapon.id);
                }else {
                    objeto.put("class","Armor");
                    Armor armor=(Armor)itemProperties;
                    objeto.put("itemId",armor.id);
                }
                objeto.put("x",itemProperties.getHitbox().x);
                objeto.put("y",itemProperties.getHitbox().y);
                objetosMapa.put(count+++"",objeto);

            }
            sala.put("objetosMapa",objetosMapa);


            mapJsonObject.put(room.getIdSala(),sala);
        }

        save.put("nivel",Juego.getNivel());
        System.out.println(mapJsonObject.size());
        save.put("mapa",mapJsonObject);

    }

    static public Juego loadSave(int slot, Menu menu){


        JSONParser parser = new JSONParser();
        JSONObject gameSave = null;
        try {
            gameSave = (JSONObject) parser.parse(new FileReader("save"+slot+".json"));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonPlayer=(JSONObject) gameSave.get("player");
        int idSalaActual=Integer.parseInt(jsonPlayer.get("idSala").toString());
        Player player=loadPlayer(jsonPlayer);

        int nivel=Integer.parseInt(gameSave.get("nivel").toString());

        ArrayList<Room> salas=loadMap((JSONObject) gameSave.get("mapa"),player,idSalaActual,nivel);

        System.out.println("CLASE JUEGO LOADSAVE");

        return new Juego(player,salas,nivel,menu);


    }

    static private Player loadPlayer(JSONObject save){
        Weapon [] weapons=new Weapon[2];
        Armor [] armors=new Armor[3];
        String[] string=save.get("weapons").toString().substring(1,save.get("weapons").toString().length()-1).replace(" ","").split(",");
        for (int i = 0; i < string.length; i++) {
            if(string[i]!=null&&!string[i].equals("null")) {
                weapons[i] = Weapon.createWeapon(Integer.parseInt(string[i]));
            }
        }
        string=save.get("armor").toString().substring(1,save.get("armor").toString().length()-1).replace(" ","").split(",");
        for (int i = 0; i < string.length; i++) {
            if(string[i]!=null&&!string[i].equals("null")) {
                armors[i] = Armor.createArmor(Integer.parseInt(string[i]));
            }
        }
//            public Player(int x, int y, int hp, Weapon[] weapons, Armor[] armor, int experiencia, int puntuacion, int level, int dinero) {
        return new Player( Integer.parseInt(save.get("x").toString()),Integer.parseInt(save.get("y").toString()),Integer.parseInt(save.get("hp").toString()),weapons,armors,Integer.parseInt(save.get("exp").toString()),Integer.parseInt(save.get("puntuacion").toString()),Integer.parseInt(save.get("lvl").toString()),Integer.parseInt(save.get("dinero").toString()));
    }

    static private ArrayList<Room> loadMap(JSONObject save, Player player, int idSala, int nivel){

        HashMap<String,Room> salas=new HashMap<>();
        LinkedList<Room> listaSalas=new LinkedList<>();
        JSONObject sala;
        Room room;
        Room.setContador(0);

        for (int i = 0; i < save.size(); i++) {
            sala=(JSONObject) save.get(i+"");
            room=new Room(Integer.parseInt(sala.get("type").toString()));
            room.setVisited((boolean)sala.get("visited"));
            room.clear=(boolean)sala.get("clear");
            room.setNear((boolean)sala.get("near"));
            room.x=Integer.parseInt(sala.get("x").toString());
            room.y=Integer.parseInt(sala.get("y").toString());
            room.salaClass=Integer.parseInt(sala.get("class").toString());

            JSONObject entities=(JSONObject)sala.get("entities");
            JSONObject entity;
            for (int j = 0; j < entities.size(); j++) {
                entity=(JSONObject)entities.get(j+"");
                room.entities.add(Enemy.createEnemy(Integer.parseInt(entity.get("id").toString()),Integer.parseInt(entity.get("x").toString()),Integer.parseInt(entity.get("y").toString()),player));
            }

            if(idSala==room.getIdSala()){
                player.salaPlayer=room;
                room.entities.add(player);
            }
            JSONObject objetosMapa=(JSONObject)sala.get("objetosMapa");
            JSONObject objeto;
            ItemProperties item=null;
            for (int j = 0; j < objetosMapa.size(); j++) {
                objeto=(JSONObject)objetosMapa.get(j+"");
                switch (objeto.get("class").toString()){
                    case "Buyable":
                        if(objeto.get("itemClass").toString().equals("Weapon")){
                            item=Weapon.createWeapon(Integer.parseInt(objeto.get("itemId").toString()));
                        }else {
                            item=Armor.createArmor(Integer.parseInt(objeto.get("itemId").toString()));
                        }
                        item=new Buyable(item);
                        break;
                    case "Armor":
                        item=Armor.createArmor(Integer.parseInt(objeto.get("itemId").toString()));
                        break;
                    case "Weapon":
                        item=Weapon.createWeapon(Integer.parseInt(objeto.get("itemId").toString()));
                        break;
                }
                item.getHitbox().x=Integer.parseInt(objeto.get("x").toString());
                item.getHitbox().y=Integer.parseInt(objeto.get("y").toString());
                room.objetosMapa.add(item);
            }

            JSONObject salidas=(JSONObject)sala.get("salidas");
            Salida salida;
            for (String key:(Set<String>)salidas.keySet()) {
                salida=new Salida(room,Integer.parseInt(key));
                room.salidas.put(key,salida);
            }

            salas.put(""+i,room);
            listaSalas.add(room);
        }

        JSONObject salidas;
        JSONObject conexionJSON;
        Salida salidaSala;
        Salida salidaConexion;


        for (int i = 0; i < listaSalas.size(); i++) {
            room=listaSalas.get(i);
            salidas=(JSONObject) ((JSONObject) save.get(i+"")).get("salidas");
            room.inicializarSala(nivel,false);

            for (String key:(Set<String>)salidas.keySet()) {
                conexionJSON =(JSONObject) ((JSONObject) salidas.get(key)).get("conexion");
                salidaSala=room.salidas.get(key);
                salidaConexion=salas.get(conexionJSON.get("sala").toString()).salidas.get(conexionJSON.get("val").toString());
                salidaSala.setConexion(salidaConexion);
                salidaConexion.setConexion(salidaSala);
            }
        }

        return new ArrayList<>(listaSalas);
    }

    public static void main(String[] args) {
    }

}
