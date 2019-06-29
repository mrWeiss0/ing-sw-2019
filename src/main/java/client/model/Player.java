package client.model;

public class Player {
    private String name;
    private int avatar;
    private int[] damages;
    private int[] marks;
    private int points;
    private int deaths;
    private Weapon[] weapons;
    private int[] ammo;
    private int nPowerup;
    private int[] coordinates;

    public Player(String name, int avatar){
        this.name=name;
        this.avatar=avatar;
    }

    public void setDamages(int[] damages) {
        this.damages = damages;
    }

    public void setMarks(int[] marks) {
        this.marks = marks;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setWeapons(Weapon[] weapons) {
        this.weapons = weapons;
    }

    public void setAmmo(int[] ammo) {
        this.ammo = ammo;
    }

    public void setnPowerup(int nPowerup) {
        this.nPowerup = nPowerup;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public String toString(){
        return name+" - "+avatar;
    }




}
