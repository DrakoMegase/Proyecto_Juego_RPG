package pruebas3.juego;

import oracle.jrockit.jfr.JFR;
import pruebas3.control.Teclado;

import javax.swing.*;
import java.awt.*;

public class Juego extends Canvas implements Runnable {


    private static final long serialVersionUID = 1L;

    private static final int ANCHO = 320;
    private static final int ALTO = 320;


    private static boolean running = false;

    private static final String NOMBRE = "Juego";

    private static int aps = 0;
    private static int fps = 0;

    private static JFrame ventana;
    private static Thread thread;

    private static Teclado teclado;


    public Juego(){

        setSize(new Dimension(ANCHO,ALTO));


       teclado = new Teclado();
       addKeyListener(teclado);


        ventana = new JFrame(NOMBRE);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setLayout(new BorderLayout());
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        setSize(new Dimension(ANCHO,ALTO));

    }




   private synchronized void iniciar(){
        running = true;

        thread = new Thread(this, "Graficos");
        thread.start();

   }

   private synchronized void detener(){

        running = false;

       try {
           thread.join();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }

   private void actualizar(){
        teclado.actualizar();


        if (teclado.arriba){
            System.out.println("Arriba");
        }
       if (teclado.abajo){
           System.out.println("Abajo");
       }
       if (teclado.derecha){
           System.out.println("der");
       }
       if (teclado.izquierda){
           System.out.println("izq");
       }


        aps++;
   }

   private void mostar(){
        fps++;
   }

    @Override
    public void run() {

        final int NS_POR_SEGUNDO = 1000000000;
        final byte APS_OBJETIVO = 60;
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

        long referenciaActualizacion = System.nanoTime();
        long referenciaContador = System.nanoTime();

        double tiempoTranscurrido;
        double delta = 0;

        requestFocus();

        running = true;
        while (running){
            final long inicioBucle = System.nanoTime();
            tiempoTranscurrido = inicioBucle - referenciaActualizacion;
            referenciaActualizacion = inicioBucle;

            delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;

            while (delta >= 1){
                actualizar();
                delta--;
            }
            mostar();
            if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO){
                ventana.setTitle(NOMBRE + " || APS : " + aps + " || FPS : " + fps);
                aps = 0;
                fps = 0;
            }


        }

    }

    public static void main(String[] args) {
        Juego j1 = new Juego();
        j1.run();


    }

}
