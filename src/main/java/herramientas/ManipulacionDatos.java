package herramientas;

import pruebasMovimiento.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

final public class ManipulacionDatos {


    static public void rectanglesToEntityObjects(String ruta, LinkedList<Entity> entities){
        ArrayList<Rectangle> rectangleArrayList = ExtraerDatosJson.objetosMapa(ruta);

        for (Rectangle r:rectangleArrayList
            ) {

            entities.add(new Entity((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight()));

        }


    }




}
