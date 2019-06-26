package client.model;

public class Board {

    private int mapType; //n° mappa da prendere
    private Player[] players; //giocatori da displayare
    private Square[] squares; //gli square della mappa
    private Room[] rooms;
    private int maxKills; // la dimensione della kill track
    private int[] killTrack; //le figure che hanno ucciso e hanno preso un teschio
    private boolean[] overkills; //array che dice per ogni posizione se c'è overkill
}
