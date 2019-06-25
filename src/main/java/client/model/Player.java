package client.model;

public class Player {
    private String name;// nome
    private int[] damages;
    private int[] marks; //marchi indicati come id dei giocatori(pos nella lista di player)
    private int points; //n° punti
    private int deaths; //n° morti da displayare
    private int[] weapons; //ID delle weapon possedute
    private int[] ammo;
    private int nPowerup; //n° powerup da mostrare agli altri giocatori
    private int[] possibleActions; //riferimenti agli indici nella lista delle azioni possibili della figure
    private int[] coordinates; //coordinate per displayarlo su una cella nella mappa
    private int character;//il personaggio scelto
}
