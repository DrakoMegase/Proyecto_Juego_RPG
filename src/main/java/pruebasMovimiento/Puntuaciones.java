package pruebasMovimiento;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import static pruebasMovimiento.Menu.sound;

class Puntuaciones extends JPanel {

    private JLabel background;

    private ArrayList<String> scores=new ArrayList<>();

    private int pagina=0;

    private JButton anterior;
    private JButton siguiente;
    private LinkedList<Box> tabla=new LinkedList<>();
    private JLabel numPag;
    private boolean ready=false;




    Puntuaciones(Menu menu, JPanel backgroundpanel, Image a) {


        background = new JLabel(new ImageIcon(a));
        background.setBounds(-6, -20, WIDTH, HEIGHT);
        this.add(background);
        background.setLayout(null);

        Firebase.readScores(scores);

        JLabel titulo = new JLabel("<html><font color='white'>Puntuaciones</font></html>");
        titulo.setFont(new Font("Verdana", Font.BOLD, 30));
        titulo.setLocation(100,100);
        titulo.setBounds(120,0,300,300);
        titulo.setVisible(true);

        numPag = new JLabel("<html><font color='white'>"+(pagina+1)+"</font></html>");
        numPag.setFont(new Font("Verdana", Font.BOLD, 15));
        numPag.setLocation(100,100);
        numPag.setBounds(250, 500,300,300);
        numPag.setVisible(true);

        background.add(titulo);

        background.add(numPag);

        JButton menuPrincipal = new JButton("Menu principal");
        menuPrincipal.setBounds(350, 20, 150, 35);
        background.add(menuPrincipal);

        menuPrincipal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                menu.remove(background);

                menu.add(backgroundpanel);
                menu.setContentPane(backgroundpanel);

            }
        });

        anterior = new JButton("Anterior");
        anterior.setBounds(12, 450, 150, 35);
        background.add(anterior);

        anterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPage(pagina-1);

            }
        });

        siguiente = new JButton("Siguiente");
        siguiente.setBounds(350, 450, 150, 35);
        background.add(siguiente);

        siguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPage(pagina+1);

            }
        });

        /*Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });

        timer.start();*/

        setPage(pagina);


    }

    private void setPage(int page){
        pagina=page;
        Box box;

        numPag.setText("<html><font color='white'>"+(pagina+1)+"</font></html>");

        while (tabla.size()>0){
            box=tabla.get(0);
            background.remove(box);
            tabla.remove(box);
        }

        String score[];
        JLabel texto;
        for (int i = 5*page; i < scores.size() && i < 5*(1+page); i++) {
            box=Box.createHorizontalBox();
            box.setBounds(50,60+50*(i-5*page),400,300);
            score=scores.get(i).split(";");
            texto = new JLabel("<html><font color='white'>"+score[1]+"</font></html>");
            texto.setHorizontalAlignment(JLabel.LEFT);
            texto.setFont(new Font("Verdana", Font.BOLD, 20));
            texto.setVisible(true);
            box.add(texto);

            texto = new JLabel();

            JLabel finalTexto = texto;
            String[] finalScore = score;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    finalTexto.setIcon(new ImageIcon(scoreIcons(finalScore[3], finalScore[4])));
                    repaint();
                }
            });
            texto.setIcon(new ImageIcon(scoreIcons(score[3],score[4])));
            texto.setHorizontalAlignment(JLabel.CENTER);
            texto.setVisible(true);
            box.add(texto);

            texto = new JLabel("<html><font color='white'>"+score[0]+"</font></html>");
            texto.setHorizontalAlignment(JLabel.RIGHT);
            texto.setFont(new Font("Verdana", Font.BOLD, 20));
            texto.setVisible(true);
            box.add(texto);


            tabla.add(box);
            background.add(box);
        }

        if(page==0){
            anterior.setVisible(false);
        }else {
            anterior.setVisible(true);
        }

        if(scores.size()-(5*(page+1))<=0){
            siguiente.setVisible(false);
        }else {
            siguiente.setVisible(true);
        }
    }


    private BufferedImage scoreIcons(String armor, String weapons){
        Player player=new Player(0,0,0);
        String[] array=armor.replace("[", "").replace("]", "").replace(" ","").split(",");
        for (int i = 0; i < array.length; i++) {
            if(array[i]!=null&&!array[i].equals("null")){
                player.getArmor()[i]=Armor.createArmor(Integer.parseInt(array[i]));
            }
        }

        array=weapons.replace("[", "").replace("]", "").replace(" ","").split(",");
        for (int i = 0; i < array.length; i++) {
            if(array[i]!=null&&!array[i].equals("null")){
                player.getWeapons()[i]=Weapon.createWeapon(Integer.parseInt(array[i]));
            }
        }

        BufferedImage bufimage = new BufferedImage(180, 64,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D= (Graphics2D) bufimage.getGraphics();

        player.draw(graphics2D,0,0);

        for (Weapon weapon:player.getWeapons()){
            graphics2D.fillRect(74+40*weapon.getWeaponType(),20,30,30);
            weapon.drawIcon(graphics2D,74+40*weapon.getWeaponType(),20);
        }

        return bufimage;
    }

}
