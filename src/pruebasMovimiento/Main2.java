package pruebasMovimiento;

import javax.swing.*;

import static pruebasMovimiento.Main.WINDOWX;
import static pruebasMovimiento.Main.WINDOWY;

public class Main2 {


    public static void main(String[] args) {

        Pantalla pantalla1 = new Pantalla("res/json/mapa5.json","resources/terrain_atlas.png");

        JFrame frame = new JFrame("Titulo de la ventana");                           //Frame = Marco         Creacion de ventana
        frame.setSize(WINDOWX, WINDOWY);                                                   //Tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //Accion cuando cerramos la ventana
        frame.setResizable(false);                                                        //Negamos que la ventana pueda ser modificada en tamaño
        frame.add(pantalla1);
        frame.setVisible(true);
//        System.out.println(pantalla1.spritesObjectArrayList.get(0).getSpriteImg());
    }


}
