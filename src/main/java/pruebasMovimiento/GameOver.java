package pruebasMovimiento;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

class GameOver extends JPanel {

    private JLabel background;
    boolean ready=false;

    GameOver() {

        background = new JLabel();
        background.setBounds(-6, -14, WIDTH, HEIGHT);
        this.add(background);
        background.setLayout(null);
        background.setVisible(true);



    }

}
