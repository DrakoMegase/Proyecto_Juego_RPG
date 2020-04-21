package pruebasMovimiento;

import java.util.LinkedList;

public class Prop extends Entity{

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
