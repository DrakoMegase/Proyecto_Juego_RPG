package pruebasMovimiento;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyAdapt extends KeyAdapter {

    Player player;

    KeyAdapt(Player player) {
        this.player = player;
    }

    public void keyPressed(KeyEvent e) {

        player.keyPressed(e);

    }

    public void keyReleased(KeyEvent e) {

        player.keyReleased(e);


    }

}
