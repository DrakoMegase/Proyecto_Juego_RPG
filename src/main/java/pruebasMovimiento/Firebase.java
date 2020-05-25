package pruebasMovimiento;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class Firebase {


    private static boolean uploadScore(Player player){

        final boolean[] booleans = { false, false };

       initializeApp();

// As an admin, the app has access to read and write all data, regardless of Security Rules
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("scores");

        DatabaseReference refCount=ref.child("count");

        final Integer[] count = {null};

        refCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                count[0] =Integer.parseInt(dataSnapshot.getValue().toString());
                booleans[0] =true;
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        int time=0;

        while (!booleans[0] &&time<5000){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time+=100;
        }

        if(time>=5000){
            return false;
        }

        time=0;

        booleans[0] =false;

        DatabaseReference refUpload=ref.child(count[0]+"");
        count[0]++;

        refUpload.setValue(player.puntuacion + ";" + player.name + ";" + player.level + ";" + Arrays.deepToString(player.getArmor()) + ";" + Arrays.deepToString(player.getWeapons()), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                booleans[0]=true;
            }
        });

        refCount.setValue(count[0], new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                booleans[1]=true;
            }
        });

        while (!(booleans[0]&&booleans[1]) &&time<5000){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time+=100;
        }


        return !(time>=5000);
    }

    static boolean readScores(ArrayList<String> arrayList){

        initializeApp();

// As an admin, the app has access to read and write all data, regardless of Security Rules
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("scores");

        final boolean[] booleans={false};


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> document = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator=document.iterator();
                while (iterator.hasNext()){
                    DataSnapshot data=iterator.next();
                    if(!data.getKey().equals("count")) {
                        String object = data.getValue().toString();
                        arrayList.add(object);
                    }
                }
                Comparador comparador= new Comparador();
                arrayList.sort(comparador);
                booleans[0]=true;
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        int time=0;

        while (!booleans[0] &&time<5000){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time+=100;
        }

        return !(time>=5000);
    }

    private static void initializeApp(){
        if(FirebaseApp.getApps().size()==0) {
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
        }
    }

    static class Comparador implements java.util.Comparator<String>{

        @Override
        public int compare(String o1, String o2) {
            String[] s1=o1.split(";");
            String[] s2=o2.split(";");


            return Integer.parseInt(s1[0])-Integer.parseInt(s2[0]);
        }
    }

    public static void main(String[] args) {

        Player player=new Player(2,2,24);

        System.out.println(Firebase.uploadScore(player));

        ArrayList<String> list=new ArrayList<>();

        if(Firebase.readScores(list)){
            for (String score:list){
                System.out.println(score);
            }
        }else {
            System.out.println("Big F");
        }

    }
}
