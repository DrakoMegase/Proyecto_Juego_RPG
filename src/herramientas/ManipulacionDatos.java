package herramientas;

import org.w3c.dom.css.Rect;
import pruebasMovimiento.Entity;

import java.awt.*;
import java.util.ArrayList;

final public class ManipulacionDatos {


    static public ArrayList<Entity> rectanglesToEntityObjects(ArrayList<Rectangle> rectangleArrayList, String ruta){

        ExtraerDatosJson.objetosMapa(rectangleArrayList,ruta);
        ArrayList<Entity>objetosBackground = new ArrayList<>();


        for (Rectangle r:rectangleArrayList
            ) {


            Entity e = new Entity((int)r.getX(),(int)r.getY(),(int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
            objetosBackground.add(e);

        }


        return objetosBackground;
    }



}
