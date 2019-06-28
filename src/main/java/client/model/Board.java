package client.model;

import client.view.View;

import java.util.Arrays;

public class Board {
    private View view;
    private int mapType; //n° mappa da prendere V
    private int maxKills; // la dimensione della kill track V
    private int[] killTrack; //le figure che hanno ucciso e hanno preso un teschio V
    private boolean[] overkills; //array che dice per ogni posizione se c'è overkill V
    private Player[] players; //giocatori da displayare
    private Square[] squares; //gli square della mappa


    public Board(View view){
        this.view=view;
    }

    public void setMapType(int mapType){
        this.mapType=mapType;
    }

    public void setMaxKills(int maxKills){
        this.maxKills=maxKills;
    }

    public void setKillTrack(int[] killTrack){
        this.killTrack=killTrack;
    }

    public void setOverkills(boolean[] overkills){
        this.overkills=overkills;
    }

    public void setSquares(Square[] squares){
        this.squares=squares;
    }

    public int getMapType() {
        return mapType;
    }

    public int getMaxKills() {
        return maxKills;
    }

    public int[] getKillTrack() {
        return killTrack;
    }

    public boolean[] getOverkills() {
        return overkills;
    }

    public Square[] getSquares() {
        return squares;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Square[] getRoom(int id){
        return (Square[]) Arrays.stream(squares).filter(x->x.getRoom()==id).toArray();
    }
}
