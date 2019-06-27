package client.model;

public class Player {
    private String name;// nome
    private int character;//il personaggio scelto

    private int[] damages;
    private int[] marks; //marchi indicati come id dei giocatori(pos nella lista di player)
    private int points; //n° punti
    private int deaths; //n° morti da displayare
    private Weapon[] weapons; //ID delle weapon possedute
    private int[] ammo;
    private int nPowerup; //n° powerup da mostrare agli altri giocatori
    private int[] coordinates; //coordinate per displayarlo su una cella nella mappa

    public Player(String name, int character, int points, int deaths, Weapon[] weapons, int[] ammo, int nPowerup, int[] coordinates){
        this.name=name;
        this.character=character;
        this.points=points;
        this.deaths=deaths;
    }

    public String toString(){
        return name+" - "+character;
    }




}
