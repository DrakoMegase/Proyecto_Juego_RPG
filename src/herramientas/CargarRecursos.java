package herramientas;

import java.io.*;

public class CargarRecursos {



    public static String jsonToString(String ruta) {

        String fileAsString = null;

        try {
            InputStream is = new FileInputStream(ruta);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            fileAsString = sb.toString();
            //System.out.println("Contents : " + fileAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return fileAsString;
    }


    public static void main(String[] args) {

        //jsonToString("src/mapGenPruebasJson/lib/mapaDesierto.tmx");

    }


}
