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

public class GameOver extends JPanel {

    JLabel background;
    boolean ready=false;

    public GameOver() {

        background = new JLabel();
        background.setBounds(-6, -14, WIDTH, HEIGHT);
        this.add(background);
        background.setLayout(null);
        background.setVisible(true);



    }

    private void uploadScore(Player player){

        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream(new File(ClassLoader.getSystemClassLoader().getResource("firebase/sloanegate-firebase-adminsdk-yaki9-3e58e67761.json").toURI()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

// Initialize the app with a service account, granting admin privileges
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://sloanegate.firebaseio.com")
                    .build();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FirebaseApp.initializeApp(options);

// As an admin, the app has access to read and write all data, regardless of Security Rules
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("scores").child("1");

        ref.setValue(3,null);

        ref.setValue(player.puntuacion+";"+ player.name +";"+player.level +";"+ Arrays.deepToString(player.getArmor())+";"+Arrays.deepToString(player.getWeapons()),null);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                ready=true;
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    public static void main(String[] args) {

        Player player=new Player(2,2,24);
        System.out.println();
        GameOver gameOver=new GameOver();
        gameOver.uploadScore(player);



        while (!gameOver.ready){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("esperando");
        }

    }
}
