package mapGenPruebasJson;

import herramientas.CargarRecursos;
import org.json.simple.JSONArray;               //permite crear arrays JSON
import org.json.simple.JSONObject;              //permite crear objetos JSON
import org.json.simple.parser.ParseException;   //permite tratar excepciones
import org.json.simple.parser.JSONParser;       //permite leer JSON

import java.awt.*;
import java.util.ArrayList;

public class TiledMap {


    private int anchoMapaTiles;
    private int altoMapaTiles;
    private Point puntoInicial;

    private ArrayList<CapaSprites> capasSprites;
    private ArrayList<CapaColisiones> capasColisiones;


    public TiledMap(final String ruta) {


        String contenido = CargarRecursos.jsonToString(ruta);

        //ANCHO Y ALTO

        JSONObject globalJSON = obtenerObjetoJSON(contenido);       //Todoo el mapa en objeto json
        anchoMapaTiles = obtenerIntDesdeJson(globalJSON, "width");
        altoMapaTiles = obtenerIntDesdeJson(globalJSON, "height");

        //System.out.println("ancho : " + anchoMapaTiles + " alto : " + altoMapaTiles);       //prueba


        //puntos start donde apareceremos PUNTO INICIAL
        JSONObject puntoInicial = obtenerObjetoJSON(globalJSON.get("start").toString());
        this.puntoInicial = new Point(obtenerIntDesdeJson(puntoInicial, "x"),
                obtenerIntDesdeJson(puntoInicial, "y"));
        //System.out.println(this.puntoInicial);
        //podria ponerse un punto inicial por defecto si este no esta en el JSON

        //CAPAS
        JSONArray capas = obtenerArrayJSON(globalJSON.get("layers").toString());


        this.capasSprites = new ArrayList<>();
        this.capasColisiones = new ArrayList<>();

        //INICIAR CAPAS

        for (int i = 0; i < capas.size(); i++) {

            //Guardamos de cada capa su ancho, alto, x, y y su tipo

            JSONObject datosCapa = obtenerObjetoJSON(capas.get(i).toString());
            int anchoCapa = obtenerIntDesdeJson(datosCapa, "width");
            int altocapa = obtenerIntDesdeJson(datosCapa, "height");
            int xCapa = obtenerIntDesdeJson(datosCapa, "x");
            int yCapa = obtenerIntDesdeJson(datosCapa, "y");
            String tipo = datosCapa.get("type").toString();

            //Guardamos cada casilla en funcion de lo que sea (si objeto, o sprite)
            switch (tipo) {
                case "tilelayer":
                    JSONArray sprites = obtenerArrayJSON(datosCapa.get("data").toString());
                    int[] spritesCapa = new int[sprites.size()];

                    for (int j = 0; j < sprites.size() ; j++) {
                        int codigoSprite = Integer.parseInt(sprites.get(j).toString());
                        spritesCapa[j] = codigoSprite -1;

                    }
                    this.capasSprites.add(new CapaSprites(anchoCapa,altocapa,xCapa,yCapa, spritesCapa));
                    break;

                case "objectgroup":
                    JSONArray rectangulos = obtenerArrayJSON(datosCapa.get("objects").toString());
                    Rectangle[] rectangulosCapa = new Rectangle[rectangulos.size()];

                    for (int j = 0; j < rectangulos.size() ; j++) {
                        JSONObject datosRectangulo = obtenerObjetoJSON(rectangulos.get(j).toString());
                        int x = obtenerIntDesdeJson(datosRectangulo, "x");
                        int y = obtenerIntDesdeJson(datosRectangulo, "y");
                        int ancho = obtenerIntDesdeJson(datosRectangulo, "width");
                        int alto = obtenerIntDesdeJson(datosRectangulo, "height");

                        if (x == 0) x = 1;
                        if (y == 0) y = 1;
                        if (ancho == 0) ancho = 1;
                        if (alto == 0) alto = 1;

                        Rectangle rectangulo = new Rectangle(x,y,ancho,alto);
                        rectangulosCapa[j] = rectangulo;

                    }

                    this.capasColisiones.add(new CapaColisiones(anchoCapa, altocapa, xCapa, yCapa, rectangulosCapa));
                    break;
            }
        }

        System.out.println(this.capasSprites.size());



    }


    private JSONObject obtenerObjetoJSON(final String codigoJSON) {

        JSONParser lector = new JSONParser();
        JSONObject objetoJSON = null;

        try {
            Object recuperado = lector.parse(codigoJSON);
            objetoJSON = (JSONObject) recuperado;
        } catch (ParseException e) {
            System.out.println("Posicion " + e.getPosition());
            System.out.println(e);
        }

        return objetoJSON;
    }

    private JSONArray obtenerArrayJSON(final String codigoJSON) {

        JSONParser lector = new JSONParser();
        JSONArray arrayJSON = null;

        try {
            Object recuperado = lector.parse(codigoJSON);
            arrayJSON = (JSONArray) recuperado;
        } catch (ParseException e) {
            System.out.println("Posicion " + e.getPosition());
            System.out.println(e);
        }

        return arrayJSON;
    }

    private int obtenerIntDesdeJson(final JSONObject objetoJson, final String clave) {
        if (objetoJson.get(clave).toString().equals("") || objetoJson.get(clave).toString() == null ){
            return 0;
        }

        double result =Double.parseDouble(objetoJson.get(clave).toString());

        return (int)result;
    }


    public void aa(String ruta) {



        JSONObject globalJSON = obtenerObjetoJSON(ruta);       //Todoo el mapa en objeto json

    }

    public static void main(String[] args) {

        TiledMap t1 = new TiledMap("src/mapGenPruebasJson/lib/mapaMedieval.tmx");
        t1.aa("src/mapGenPruebasJson/lib/mapaMedieval.tmx");
    }

}



