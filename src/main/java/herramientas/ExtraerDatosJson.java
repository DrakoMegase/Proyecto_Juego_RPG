package herramientas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pruebasMovimiento.Salida;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

final public class ExtraerDatosJson {





    static public String extraerValorJson(String ruta, String clave){

        //Tenemos por parametros la ruta y la clave a buscar.


        JSONParser parser = new JSONParser();                               //Inicializacion y creacion de un parserJson
        String valorADevolver = null;                                       //Inicializacion y creacion de un string
        try {
//            JSONObject obj = (JSONObject) parser.parse(new FileReader(ruta));   //Metemos en un JSONObject el archivo json pasado por parametro gracias al parser
            JSONObject obj = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(ExtraerDatosJson.class.getClassLoader().getResourceAsStream(ruta))));   //Metemos en un JSONObject el archivo json pasado por parametro gracias al parser
            valorADevolver = obj.get(clave).toString();                         //buscamos en el objeto la clave deseada y lo convertimos a string para evitar problemas de compatibilidad entre ints, doubles, long o strings
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return valorADevolver;                  //si necesitaramos convertirlo a double/int o long ya podremos hacerlo fuera del metodo.

    }

    static public ArrayList<JSONObject> arraysSprites(String rutaJson){


        ArrayList<JSONObject> sprites = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONObject arrayADevolver = null;
        try {
            arrayADevolver = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(ExtraerDatosJson.class.getClassLoader().getResourceAsStream(rutaJson))));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }


       JSONArray jsonArray = (JSONArray) arrayADevolver.get("layers");

        for (Object o:jsonArray
             ) {

            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.get("type").equals("tilelayer")){

                sprites.add(jsonObject);
            }
        }
        return sprites;
    }

    static public int[][] devolverNumSpritesTotal(ArrayList capaSprites){

        int contador = 0;

        JSONObject jsonObjecta = (JSONObject) capaSprites.get(0);
        JSONArray intsa = (JSONArray) jsonObjecta.get("data");
        int spritePorCapa = intsa.size();
        int numCapa = capaSprites.size();



        //INT [CAPA][ID SPRITE]
        int[][] arrayInts = new int[numCapa][spritePorCapa];
        for (int i = 0; i < numCapa ; i++) {

            JSONObject jsonObject = (JSONObject) capaSprites.get(i);
            JSONArray ints = (JSONArray) jsonObject.get("data");

            for (int j = 0; j < spritePorCapa; j++) {
//                System.out.println(arrayInts[i][j]);

                arrayInts[i][j] = (int) Long.parseLong(ints.get(j).toString());
                //System.out.println(arrayInts[i][j]);
                contador++;
            }


        }


        return arrayInts;

    }







    static ArrayList<Rectangle>objetosMapa(String ruta){
        ArrayList<Rectangle> rectangleArrayList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONObject arrayADevolver = null;
        //JSONArray arrayADevolverArrayJson = new JSONArray();
        try {
            arrayADevolver = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(ExtraerDatosJson.class.getClassLoader().getResourceAsStream(ruta))));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        int aa = 0;
        JSONArray jsonArray = (JSONArray) arrayADevolver.get("layers");
        for (Object o:jsonArray
        ) {


            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.get("type").equals("objectgroup")){
                if (jsonObject.get("name").equals("Salidas") || jsonObject.get("name").equals("salidas")) {
                    break;
                }

               JSONArray objects = (JSONArray) jsonObject.get("objects");

                for (Object a:objects
                     ) {


                    JSONObject jsonObjectArrayObjects = (JSONObject) a;

                    int width = (int) Double.parseDouble(jsonObjectArrayObjects.get("width").toString());
                    int height = (int) Double.parseDouble(jsonObjectArrayObjects.get("height").toString());
                    int x = (int) Double.parseDouble(jsonObjectArrayObjects.get("x").toString());
                    int y = (int) Double.parseDouble(jsonObjectArrayObjects.get("y").toString());

                    //System.out.println("x  " + x + " y  " + y  + "  width "  + width + " height " + height);

                    if (width <= 0) width = 1;
                    if (height <= 0) height = 1;

                    rectangleArrayList.add(new Rectangle(x,y,width,height));


                }


            }
        }




        return rectangleArrayList;
    }


    public static HashMap<String,Salida> salidasMapa(String ruta){
        HashMap<String,Salida> salidaHashMap = new HashMap<>();

        JSONParser parser = new JSONParser();
        JSONObject arrayADevolver = null;
        //JSONArray arrayADevolverArrayJson = new JSONArray();
        try {
            arrayADevolver = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(ExtraerDatosJson.class.getClassLoader().getResourceAsStream(ruta))));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }


        int aa = 0;
        Salida salida;
        JSONArray jsonArray = (JSONArray) arrayADevolver.get("layers");
        for (Object o:jsonArray
        ) {


            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.get("type").equals("objectgroup")){
                if (jsonObject.get("name").equals("Salidas") || jsonObject.get("name").equals("salidas")) {

                    //System.out.println(jsonObject);


                    JSONArray objects = (JSONArray) jsonObject.get("objects");

                for (Object a:objects
                ) {

                    JSONObject jsonObjectArrayObjects = (JSONObject) a;

                    int width = (int) Double.parseDouble(jsonObjectArrayObjects.get("width").toString());
                    int height = (int) Double.parseDouble(jsonObjectArrayObjects.get("height").toString());
                    int x = (int) Double.parseDouble(jsonObjectArrayObjects.get("x").toString());
                    int y = (int) Double.parseDouble(jsonObjectArrayObjects.get("y").toString());
                    String key = jsonObjectArrayObjects.get("type").toString();

                    //System.out.println("x  " + x + " y  " + y  + "  width "  + width + " height " + height);

                    if (width <= 0) width = 1;
                    if (height <= 0) height = 1;

                    salida=new Salida(new Rectangle(x,y,width,height));

                    salidaHashMap.put(key,salida);


                }

                }
            }
        }




        return salidaHashMap;
    }


}
