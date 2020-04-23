package pruebasMovimiento;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {


    private static final int WINDOWX = 1000;
    private static final int WINDOWY = 1000;

    public static void main(String[] args) {

        Pantalla pantalla1 = new Pantalla("res/json/mapa6.json","resources/terrain_atlas.png");

        JFrame frame = new JFrame("Minimoit RPG");                           //Frame = Marco         Creacion de ventana
        frame.setSize(WINDOWX, WINDOWY);                                                   //Tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //Accion cuando cerramos la ventana
        frame.setResizable(false);                                                        //Negamos que la ventana pueda ser modificada en tamaño
        frame.add(pantalla1);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("res/img/icon.png").getImage());    //Define el icono
//        System.out.println(pantalla1.spritesObjectArrayList.get(0).getSpriteImg());
    }


}
