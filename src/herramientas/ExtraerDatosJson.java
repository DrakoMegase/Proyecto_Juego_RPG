package herramientas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

final public class ExtraerDatosJson {

    public static ArrayList<JSONObject> spritesCapas = new ArrayList<JSONObject>();




    static public JSONArray extraerArrayJSON(String ruta, String clave){

        JSONParser parser = new JSONParser();
        JSONArray arrayADevolver = null;
        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(ruta));
            arrayADevolver = (JSONArray) obj.get(clave);
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayADevolver;

    }


    static public String extraerValorJson(String ruta, String clave){

        //Tenemos por parametros la ruta y la clave a buscar.


        JSONParser parser = new JSONParser();                               //Inicializacion y creacion de un parserJson
        String valorADevolver = null;                                       //Inicializacion y creacion de un string
        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(ruta));   //Metemos en un JSONObject el archivo json pasado por parametro gracias al parser
            valorADevolver = obj.get(clave).toString();                         //buscamos en el objeto la clave deseada y lo convertimos a string para evitar problemas de compatibilidad entre ints, doubles, long o strings
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return valorADevolver;                  //si necesitaramos convertirlo a double/int o long ya podremos hacerlo fuera del metodo.

    }


    static public JSONArray extraerArrayJsonDesdeObjectJson(JSONArray jsonArray, String clave, String atributoClave){

        int objetSize = jsonArray.size();

        for (int i = 0; i < objetSize ; i++) {

            JSONObject objetoAMirar = (JSONObject) jsonArray.get(i);

            if (objetoAMirar.get(clave).equals(atributoClave)){

                return (JSONArray) objetoAMirar.get(0);

            }else {
                System.out.println("NO ENCONTRADO");
            }


        }


        return null;
    }



    static public ArrayList<JSONObject> arraysSprites(String rutaJson){


        ArrayList<JSONObject> sprites = new ArrayList();

        JSONParser parser = new JSONParser();
        JSONObject arrayADevolver = null;
        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(rutaJson));
            arrayADevolver = obj;
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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


    static public ArrayList arraysObjects(String rutaJson){



        JSONParser parser = new JSONParser();
        JSONObject arrayADevolver = null;
        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(rutaJson));
            arrayADevolver = obj;
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONArray jsonArray = (JSONArray) arrayADevolver.get("layers");

        for (Object o:jsonArray
        ) {

            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.get("type").equals("objectgroup")){

                spritesCapas.add(jsonObject);
            }
        }
        return spritesCapas;
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
                arrayInts[i][j] = Integer.parseInt(ints.get(j).toString());
                //System.out.println(arrayInts[i][j]);
                contador++;
            }


        }


        return arrayInts;

    }


    static public int contarCapasSprites(){




        return 0;

    }

    public static void main(String[] args) {

//        System.out.println(arraysSprites("src/json/mapaPruebasIvan.json"));
//        System.out.println(arraysObjects("src/json/mapaPruebasIvan.json"));

        System.out.println(devolverNumSpritesTotal(arraysSprites("src/json/mapa2.json")).length);
        //System.out.println(arraysSprites("src/json/mapa2.json"));

    }


}
