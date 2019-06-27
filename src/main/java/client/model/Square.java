package client.model;

public class Square {
    private int[] coordinates;// per displayare (identificato in base a indice nella lista lato server)
    private boolean type; //se spawn o no
    private int[] ammo;
    private boolean powerup;
    private int[] weapons;
    private int room;

    @Override
    public String toString() {
        return "s: "+type+" at ("+coordinates[0]+","+coordinates[1]+")";
    }

    public int getRoom(){
        return room;
    }
}
