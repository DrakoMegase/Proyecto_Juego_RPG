package MapGen;

import pruebasMovimiento.Panel;

import javax.swing.*;

public class Main {


    public static void main(String[] args) {

        JFrame frame = new JFrame("Titulo de la ventana");                           //Frame = Marco         Creacion de ventana
        frame.setSize(300,300);                                                   //Tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //Accion cuando cerramos la ventana
        frame.setResizable(false);                                                        //Negamos que la ventana pueda ser modificada en tamaño
        frame.setVisible(true);                                                            //Acemos visible la ventana (si no lo declaramos, la ventana no se vera)


    }
}
