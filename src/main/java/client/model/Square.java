package client.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Square {
    private int[] coordinates;// per displayare (identificato in base a indice nella lista lato server)
    private boolean spawn; //se spawn o no
    private int room;
    private int[] ammo;
    private boolean powerup;
    private Weapon[] weapons;


    public Square(int[] coordinates, boolean spawn, int room){
        this.coordinates=coordinates;
        this.spawn=spawn;
        this.room=room;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public boolean isSpawn(){
        return spawn;
    }

    public int getRoom(){
        return room;
    }

    public int[] getAmmo() {
        return ammo;
    }

    public Weapon[] getWeapons() {
        return weapons;
    }

    public void setAmmo(int[] ammo) {
        this.ammo = ammo;
    }

    public void setWeapons(Weapon[] weapons) {
        this.weapons = weapons;
    }

    public void setPowerup(boolean powerup) {
        this.powerup = powerup;
    }

    @Override
    public String toString() {
        return "s: "+spawn+" at ("+coordinates[0]+","+coordinates[1]+") : "+(spawn?
                Arrays.stream(weapons)
                        .map(Weapon::toString)
                        .collect(Collectors.joining(" "))
                :Arrays.stream(ammo)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining(" "))+" pup: "+powerup);
    }


}
