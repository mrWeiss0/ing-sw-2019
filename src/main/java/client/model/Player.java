package client.model;

public class Player {
    private String name;// nome V
    private int avatar;//il personaggio scelto V

    private int[] damages;//
    private int[] marks; //marchi indicati come id dei giocatori(pos nella lista di player)
    private int points; //n° punti
    private int deaths; //n° morti da displayare
    private Weapon[] weapons; //ID delle weapon possedute
    private int[] ammo;
    private int nPowerup; //n° powerup da mostrare agli altri giocatori
    private int[] coordinates; //coordinate per displayarlo su una cella nella mappa

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
