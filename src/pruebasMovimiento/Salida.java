package pruebasMovimiento;

import java.awt.*;

public class Salida {


    private Rectangle area;
    private Salida conexion;

    public Salida(Rectangle area) {
        this.area = area;
    }


    public Rectangle getArea() {
        return area;
    }

    public Salida getConexion() {
        return conexion;
    }

    public void setConexion(Salida conexion) {
        this.conexion = conexion;
    }
}
