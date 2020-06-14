package pruebasMovimiento;

import java.awt.*;

public class Salida {


    private Rectangle area;
    private Salida conexion;
    private Room origen;
    private int val;


    public Salida(Rectangle area) {
        this.area = area;
    }

    Salida(Room origen, int val) {
        this.origen = origen;
        origen.salidas.put(""+val,this);
        this.val=val;
    }

    Rectangle getArea() {
        return area;
    }

    void setArea(Rectangle area) {
        this.area = area;
    }

    Salida getConexion() {
        return conexion;
    }

    void setConexion(Salida conexion) {
        this.conexion = conexion;
    }

    Room getOrigen() {
        return origen;
    }

    int getVal() {
        return val;
    }

}
