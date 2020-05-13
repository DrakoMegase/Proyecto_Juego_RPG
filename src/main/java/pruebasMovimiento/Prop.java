package pruebasMovimiento;

import java.util.LinkedList;

public class Prop extends Entity{

    /**
     *
     * @param x posicion x en la pantalla
     * @param y posicion y en la pantalla
     * @param hp vida
     * @param img imagen recurso
     * @param hitX posicion x de la hitbox dentro del sprite
     * @param hitY posicion y de la hitbox dentro del sprite
     * @param hitWidth ancho de la hitbox
     * @param hitHeight alto de la hitbox
     * @param canBeMoved si puede ser movido/empujado
     * @param canBeDamaged si puede recibir daño
     */

    public Prop(int x, int y, int hp, String img, int hitX, int hitY, int hitWidth, int hitHeight, boolean canBeMoved, boolean canBeDamaged) {
        super(x, y, hp, img, hitX, hitY, hitWidth, hitHeight, canBeMoved, canBeDamaged);
    }

    @Override
    public void update() {
        if(hp<0){
            remove=true;
        }
    }
}
