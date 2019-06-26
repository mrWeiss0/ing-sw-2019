package client.model;

public class Board {
    private int mapType; //n° mappa da prendere
    private Player[] players; //giocatori da displayare
    private Square[] squares; //gli square della mappa
    private int maxKills; // la dimensione della kill track
    private int currentPlayer;
    private int[] possibleActions; //riferimenti agli indici nella lista delle azioni possibili della figure
    private int[] killTrack; //le figure che hanno ucciso e hanno preso un teschio
    private boolean[] overkills; //array che dice per ogni posizione se c'è overkill
    private PowerUp[] powerups;
}
