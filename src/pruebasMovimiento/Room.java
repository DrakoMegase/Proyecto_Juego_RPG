package pruebasMovimiento;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Objects;

public class Room {



    private static int contadorId = 0;
    private int idRoom;
    private BufferedImage background;
    private BufferedImage details;
    private Player player;                  //Declaracion de un player
    protected LinkedList<Entity> entities;

    private boolean backNull;
    private boolean detailsNull;
    private boolean isUsed;


    public Room(BufferedImage background, BufferedImage details, Player player, LinkedList<Entity>entities) {
        this.idRoom = contadorId;
        this.background = background;
        this.details = details;
        this.player = player;
        this.entities = entities;

        if (background != null){
            backNull = true;
        }
        if (details != null){
            detailsNull = true;
        }


        contadorId++;
    }



    public int getIdRoom() {
        return idRoom;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public void setBackground(BufferedImage background) {
        this.background = background;
    }

    public BufferedImage getDetails() {
        return details;
    }

    public void setDetails(BufferedImage details) {
        this.details = details;
    }

    public Player getPlayer() {
        return player;
    }


    public LinkedList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(LinkedList<Entity> entities) {
        this.entities = entities;
    }

    public boolean isBackNull() {
        return backNull;
    }


    public boolean isDetailsNull() {
        return detailsNull;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public String toString() {
        return "Room{" +
                "idRoom=" + idRoom +
                ", background=" + backNull + ", backDetails = " + detailsNull +
                ", player=" + player +
                ", entities=" + entities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return idRoom == room.idRoom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRoom);
    }
}
