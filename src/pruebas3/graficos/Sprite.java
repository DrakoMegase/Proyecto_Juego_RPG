package pruebas3.graficos;

public class Sprite {

    private final int lado;

    private int x;
    private int y;

    public int[] pixeles;

    private final HojaSprites hoja;

    public Sprite(int lado, final int columna, final int fila, HojaSprites hoja) {
        this.lado = lado;
        this.hoja = hoja;

        this.x = columna * lado;        // x = 0, x = 1, x = 2 son columnas
        this.y = fila * lado;           //y aqui son filas


        for (int i = 0; i < lado ; i++) {
            for (int j = 0; j < lado ; j++) {

                pixeles[(x + y) * lado] = hoja.pixeles[(x + this.x) + (y + this.y) * hoja.getAncho()];
            }
        }
     }
}
