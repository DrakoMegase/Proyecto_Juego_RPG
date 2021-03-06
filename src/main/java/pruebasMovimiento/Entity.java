package pruebasMovimiento;



import javax.sound.sampled.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class Entity implements Comparable<Entity> {

    Image img;
    boolean canBeMoved, remove = false;
    int x;
    int y;
    int velX = 0;
    int velY = 0;
    int hp;
    boolean canBeDamaged;
    boolean damageWait=false;
    long damageTime=0;
    private int[] spritesPos;
    Rectangle hitbox;
    String name;
    private static int count = 0;

    public Image getImg() {
        return img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Entity(int x, int y, int hitWidth, int hitHeight) {
        this.x = x;
        this.y = y;


        hitbox = new Rectangle(x, y, hitWidth, hitHeight);

    }


    Entity(int x, int y, int hp, String img, int hitX, int hitY, int hitWidth, int hitHeight, boolean canBeMoved, boolean canBeDamaged) {
        this.x = x;
        this.y = y;

        this.hp = hp;

        this.canBeDamaged = canBeDamaged;

        name = "Entity: " + count++;

        if (img.contains(":")) {
            /*
             * img:tipo:xSprite:ySprite:width:height:nFrames
             * tipo 0=imagen unica, 1=animacion unica, 2=animaciones para cada direccion
             *
             */
            String[] split = img.split(":");
            this.img = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(split[0]));
            spritesPos = new int[6];
            for (int i = 0; i < spritesPos.length; i++) {
                spritesPos[i] = Integer.parseInt(split[i + 1]);
            }

        } else {
            this.img = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(img));
        }
        this.canBeMoved = canBeMoved;


        hitbox = new Rectangle(x + hitX, y + hitY, hitWidth, hitHeight);

    }

    void damage(int dmg) {
        if (canBeDamaged&&!damageWait) {
            hp -= dmg;
            damageTime=System.currentTimeMillis();
            damageWait=true;

            if(this instanceof Enemy){
                playSound("sounds/hit.wav");
            }else if(this instanceof Player){
                playSound("sounds/hurt.wav");
            }

        }
    }


    public void update() {

    }


    boolean push(int x, int y) {
        int newX = hitbox.x + x;
        int newY = hitbox.y + y;

        if (canBeMoved && !outOfBounds(newX, newY)) {
            move(x, y);
        }

        return canBeMoved && !outOfBounds(newX, newY);
    }

    private boolean outOfBounds(int x, int y) {
        return x < 0 || x > Juego.HEIGHT - hitbox.width || y < 0 || y > Juego.WIDTH - hitbox.height;
    }

    public void draw(Graphics2D graphics2D, int offSetX, int offSetY) {
        if (img != null) {

            if (spritesPos != null) {
                drawAnimation(graphics2D, offSetX, offSetY);
            } else {
                graphics2D.drawImage(img, x - offSetX, y - offSetY, null);
            }
        }
    }

    private void drawAnimation(Graphics2D graphics2D, int offSetX, int offSetY) {
        int multiOr = 0;
        if (spritesPos[0] > 1) {
            if (velY < 0) {
                multiOr = 1;
            } else if (velX > 0) {
                multiOr = 2;
            } else if (velX < 0) {
                multiOr = 3;
            }
        }

        int multyMov = 0;
        if (spritesPos[0] != 0 && (velX != 0 || velY != 0)) {
            multyMov = (int) Math.abs(System.currentTimeMillis() / 150) % spritesPos[5];
        }

        // Width and height of sprite
        int sw = spritesPos[3];
        int sh = spritesPos[4];
        // Position of sprite on screen
        int px = x - offSetX;
        int py = y - offSetY;
        // Coordinates of desired sprite image
        int i = spritesPos[1] + sw * multyMov;
        int j = spritesPos[2] + sh * multiOr;
        graphics2D.drawImage(img, px, py, px + sw, py + sh, i, j, i + sw, j + sh, null);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    void setPos(int x, int y) {

        int antiguaX = this.getX();
        int antiguay = this.getY();

        this.setX(x);
        this.setY(y);

        this.hitbox.x += x - antiguaX;
        this.hitbox.y += y - antiguay;

    }

    void move(int x, int y) {
        this.x += x;
        this.y += y;
        hitbox.translate(x, y);

    }

    @Override
    public int compareTo(Entity o) {
        return this.hitbox.y - o.hitbox.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return x == entity.x &&
                y == entity.y &&
                Objects.equals(hitbox, entity.hitbox) &&
                Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, hitbox, name);
    }

    protected void checkCollisions(LinkedList<Entity> entities, int count) {
        int[] force;


            for (Entity entity : entities) {
                if (!this.equals(entity) && entity.canBeMoved && !(entity instanceof Projectile)) {
                    force = intersect(this.hitbox, entity.hitbox);
                    if (force != null) {
                        if(entity instanceof Enemy){
                            Enemy enemy=(Enemy)entity;
                            damage(enemy.damage);
                        }

                        if (!entity.push(force[0], force[1])) {
                            this.push(-force[0], -force[1]);
                            if (force[0] == 0) {
                                this.velX = 0;
                            } else {
                                this.velY = 0;
                            }

                            if (count < 7){
                                count++;
                                for (int i = entities.indexOf(this); i >= 0; i--) {
                                    entities.get(i).checkCollisions(entities, count);
                                }

                            }else{
                                damage(5);
                                entity.damage(1);
                            }
                        }
                    }
                }
            }

    }



    static int[] intersect(Rectangle hitbox1, Rectangle hitbox2) {

        int[] force = null;

        if (hitbox1.intersects(hitbox2)) {
            Rectangle intersect = hitbox1.intersection(hitbox2);

            force = new int[2];

            if (intersect.x == hitbox2.x) {
                force[0] = intersect.width;
            } else {
                force[0] = -intersect.width;
            }

            if (intersect.y == hitbox2.y) {
                force[1] = intersect.height;
            } else {
                force[1] = -intersect.height;
            }

            if (intersect.width > intersect.height) {
                force[0] = 0;
            } else {
                force[1] = 0;
            }
        }
        return force;
    }

    public int getHp() {
        return hp;
    }

    public boolean isRemove() {
        return remove;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "img=" + img +
                ", canBeMoved=" + canBeMoved +
                ", remove=" + remove +
                ", x=" + x +
                ", y=" + y +
                ", velX=" + velX +
                ", velY=" + velY +
                ", hp=" + hp +
                ", canBeDamaged=" + canBeDamaged +
                ", spritesPos=" + Arrays.toString(spritesPos) +
                ", hitbox=" + hitbox +
                ", name='" + name + '\'' +
                '}';
    }

    static void playSound(String res){
        Clip clip = null;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(ClassLoader.getSystemClassLoader().getResourceAsStream(res)));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(Menu.sound); // Reduce volume by 10 decibels.
        clip.start();

    }
}
