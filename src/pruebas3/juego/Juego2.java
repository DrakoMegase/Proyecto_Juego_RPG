package pruebas3.juego;

import mapasPruebas2.Ventana;

import javax.swing.*;
import java.awt.*;

public class Juego2 extends Canvas {

    private static final long serialVersionUID = 1L;

    private static final int ANCHO = 320;
    private static final int ALTO = 320;

    private static JFrame ventana;

    private static final String NOMBRE = "Strum_2";


    public Juego2() {
        setPreferredSize(new Dimension(ANCHO,ALTO));                    //Dimensiones


        ventana = new JFrame();                                         //Nuevo JFrame
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         //Al cerrar la ventana, parara la ejecucion del programa
        ventana.setResizable(false);                                    //No permitimos que se pueda modificar el tamaño de ventana
        ventana.setLayout(new BorderLayout());                          //Añadimos un diseño de ventana añadiendole eun gestor
        ventana.add(this,BorderLayout.CENTER);                   //Añadimos el Canvas a la clase (para el usuario sera to-do 1, pero habra un canvas dentro de la ventana)
        ventana.pack();                                                 //Se ajusta el tamaño para evitar errores (ventana con canvas)
        ventana.setLocationRelativeTo(null);                            //Colocara la ventana en el centro al lanzarla
        ventana.setVisible(true);                                       //Hacemos la ventana visible

        //todo error en el constructor de cambiar el tamaño

    }


    public static void main(String[] args) {

        Juego2 juego = new Juego2();




    }

}
