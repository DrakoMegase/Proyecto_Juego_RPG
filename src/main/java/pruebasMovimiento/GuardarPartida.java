package pruebasMovimiento;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class GuardarPartida extends JPanel {

    private JLabel background;
    private final static String SAVESFOLDER ="saves";

    GuardarPartida(Juego juego) {
        Image a = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/guardarCargarBackground.png")))
                .getImage();
        background = new JLabel(new ImageIcon(a));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        this.add(background);

        GuardarPartida guardarPartida=this;

        ActionListener actionListener=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton jButton= (JButton) e.getSource();
                String text=jButton.getText();

                if(!text.startsWith("NEW")){
                    int response=JOptionPane.showConfirmDialog(null,"Este hueco ya tiene una partida guardada.\nQuieres sobreescribirla?");
                    if(response!=JOptionPane.OK_OPTION){
                        return;
                    }
                }
                Juego.menu.remove(background);
                Juego.menu.remove(guardarPartida);
                Juego.menuEsc=false;
                GuardarPartida.save(Integer.parseInt(text.substring(text.length()-1)));
                Juego.mainTimer.stop();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Juego.menu.add(Menu.panelPadre);
                Juego.menu.add(Menu.backgroundPanel);
                Juego.menu.setContentPane(Menu.backgroundPanel);
                Juego.menu.musica("music/menu.wav");
                //System.out.println("MENU PRINCIPAL");
                repaint();
            }
        };

        File file;
        for (int i = 1; i < 4; i++) {
            file=new File(SAVESFOLDER +"/save"+i+".json");
            JButton g1;
            if(file.exists()) {
                g1 = new JButton("SAVE " + i);
            }else {
                g1 = new JButton("NEW SAVE " + i);
            }
            if (i == 3) {
                g1.setForeground(Color.black);
            } else {
                g1.setForeground(Color.white);
            }
            g1.setBounds(57 + 160 * (i - 1), 220, 80, 70);
            background.add(g1);
            g1.setOpaque(false);
            g1.setContentAreaFilled(false);
            g1.addActionListener(actionListener);
        }

        JButton menuPrincipal = new JButton("Atras");
        menuPrincipal.setBounds(350, 20, 150, 35);
        background.add(menuPrincipal);
        menuPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                juego.menu.remove(background);
                juego.menu.remove(guardarPartida);
                juego.menu.setContentPane(juego);
                validate();
                juego.setVisible(true);
                repaint();

            }
        });

        repaint();



    }

    static private void save(int slot){

        File file=new File(SAVESFOLDER);
        boolean canSave=true;
        if(!file.exists()||!file.isDirectory()){
            canSave=file.mkdir();
        }

        if(canSave) {
            JSONObject save = new JSONObject();

            savePlayer(Juego.player, save);

            saveMap(Juego.getSalas(), save);

            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(SAVESFOLDER + "/save" + slot + ".json");
                fileWriter.write(save.toJSONString());
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Error: no se ha podido Guardar");
            }
        }else {
            JOptionPane.showMessageDialog(null,"Error: no se ha podido Guardar");
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

        playerJsonObject.put("mounstruosKill", Juego.mounstruosKilled);

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

    static Juego loadSave(int slot, Menu menu){


        JSONParser parser = new JSONParser();
        JSONObject gameSave = null;
        try {
            gameSave = (JSONObject) parser.parse(new FileReader(SAVESFOLDER +"/save"+slot+".json"));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonPlayer=(JSONObject) gameSave.get("player");
        int idSalaActual=Integer.parseInt(jsonPlayer.get("idSala").toString());
        Player player=loadPlayer(jsonPlayer);

        int nivel=Integer.parseInt(gameSave.get("nivel").toString());

        ArrayList<Room> salas=loadMap((JSONObject) gameSave.get("mapa"),player,idSalaActual,nivel);

        System.out.println("CLASE JUEGO LOADSAVE");

        long mKilled = (long) jsonPlayer.get("mounstruosKill");

        return new Juego(player,salas,nivel,menu,mKilled);


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

}
