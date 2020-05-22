package pruebasMovimiento;

import java.awt.*;

public class Boss extends Enemy{

    private long skillTime=0;
    private boolean skill=false;
    private int shootCount=0;
    private int maxHp;

    Boss(int x, int y, int hp, String img, int hitX, int hitY, int hitWidth, int hitHeight, boolean canBeMoved, boolean canBeDamaged, Player player, int velMov, int movPath, int damage, int id) {
        super(x, y, hp, img, hitX, hitY, hitWidth, hitHeight, canBeMoved, canBeDamaged, player, velMov, movPath, damage, id);
        maxHp=hp;
    }

    @Override
    public void update() {

        if(damageWait&&System.currentTimeMillis()-damageTime>300){
            damageWait=false;
            damageTime=0;
        }

        if(!skill&&hp<maxHp/2&&System.currentTimeMillis()-skillTime>=4000){
            skill=true;
            canBeMoved=false;
            skillTime=System.currentTimeMillis();


            skillActivation();
        }

        if(!skill){
            move(velX,velY);
        }

        if(skill){
            skill();
        }else {
            adjustMovement();
        }

        if(hp<=0){
            remove=true;
            player.experiencia+=exp;
            player.salaPlayer.salidas.put("portal",Portal.newPortal());
        }

    }

    private void skillActivation(){
        switch (id){
            case 6:
                img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/bosses/demon2.png"));
                break;
            case 7:
                velMov+=1;
                img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/bosses/bat2.png"));
                movPath=0;
                break;
            case 8:
                img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/bosses/mage-2.png"));
                break;
        }
    }


    private void skill(){
        switch (id){
            case 6:

                if(System.currentTimeMillis()-skillTime>100*(1+shootCount)){

                    int i=shootCount%8;
                    int movX=-velMov-1;
                    int movY=-velMov-1;
                    if(i%4==0){
                        movX=0;
                    }else if(i-4<0){
                        movX*=-1;
                    }

                    if(i==2||i==6){
                        movY=0;
                    }else if(i>2&&i<6){
                        movY*=-1;
                    }

                    String img="img/projectiles/bolaRoja.png:1:0:0:64:64:4";
                    Projectile projectile=new Projectile(hitbox.x,hitbox.y-20,20, img,20,21,25,25,false,false,movX,movY,this,player.salaPlayer.entities,damage);
                    player.salaPlayer.entities.add(projectile);
                    shootCount++;

                    if(shootCount==48){
                        skillTime=System.currentTimeMillis();
                        canBeMoved=true;
                        skill=false;
                        shootCount=0;
                    }


                }
                break;
            case 7:
                if(System.currentTimeMillis()-skillTime>4000){
                    skillTime=System.currentTimeMillis();
                    skill=false;
                    movPath=2;
                    velMov-=1;
                    img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/bosses/bat.png"));
                }else {
                    move(velX,velY);
                    adjustMovement();
                    if(Math.abs(player.hitbox.x-hitbox.x)>Math.abs(player.hitbox.y-hitbox.y)){
                        velY=0;
                    }else {
                        velX=0;
                    }
                }
                break;
            case 8:
                if(System.currentTimeMillis()-skillTime>100*(1+shootCount)){

                    int i=(shootCount+12)/12;
                    int movX1=0;
                    int movY1=0;
                    int movX2=0;
                    int movY2=0;
                    if(i!=2){
                        movX1=1;
                        movX2=-1;
                    }

                    if(i!=1){
                        movY1=1-2*(i/4);
                        movY2=-1+2*(i/4);
                    }

                    String img="img/projectiles/bolaVerde.png:1:0:0:64:64:4";
                    Projectile projectile1=new Projectile(hitbox.x,hitbox.y-20,20, img,20,21,25,25,false,false,movX1,movY1,this,player.salaPlayer.entities,damage);
                    Projectile projectile2=new Projectile(hitbox.x,hitbox.y-20,20, img,20,21,25,25,false,false,movX2,movY2,this,player.salaPlayer.entities,damage);
                    player.salaPlayer.entities.add(projectile1);
                    player.salaPlayer.entities.add(projectile2);
                    shootCount++;

                    if(shootCount==48){
                        skillTime=System.currentTimeMillis();
                        this.img=Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("img/bosses/mage-1.png"));
                        canBeMoved=true;
                        skill=false;
                        shootCount=0;
                    }

                }
                break;
        }
    }

}
