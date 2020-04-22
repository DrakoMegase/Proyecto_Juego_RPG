package pruebasMovimiento;

import javax.swing.*;

public class Main {


    final static int WINDOWX = 600;
    final static int WINDOWY = 600;


    public static void main(String[] args) {


        /* JFrame es una ventana
         * JPanel es un contenedor de componentes de interfaz grafica
         *
         * Un JFrame puede contener muchos JPanel y un JPanel puede contener muchos JTextField, JButtom, JLabel, JComboBox, etc.
         * Entonces usas un JFrame para definir una ventana y usas JPanel para añadir controles GUI al JFrame.
         *
         */


        JFrame frame = new JFrame("Titulo de la ventana");                           //Frame = Marco         Creacion de ventana
        frame.setSize(WINDOWX, WINDOWY);                                                   //Tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                             //Accion cuando cerramos la ventana
        frame.setResizable(false);                                                        //Negamos que la ventana pueda ser modificada en tamaño
        frame.add(new Panel());
        frame.setVisible(true);                                                            //Acemos visible la ventana (si no lo declaramos, la ventana no se vera)



    }
}
