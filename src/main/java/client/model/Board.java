package client.model;

import client.view.View;

import java.util.Arrays;

public class Board {
    private View view;
    private int mapType;
    private int maxKills;
    private int[] killTrack;
    private boolean[] overkills;
    private Player[] players;
    private Square[] squares;


    public Board(View view){
        this.view=view;
    }

    public void setMapType(int mapType){
        this.mapType=mapType;
        view.displayMapType(mapType);
    }

    public void setMaxKills(int maxKills){
        this.maxKills=maxKills;
        view.displayMaxKills(maxKills);
    }

    public void setKillTrack(int[] killTrack){
        this.killTrack=killTrack;
    }

    public void setOverkills(boolean[] overkills){
        this.overkills=overkills;
        view.displayKillTrack(killTrack,overkills);
    }

    public void setSquares(Square[] squares){
        this.squares=squares;
        view.displaySquares(squares);
    }

    public void setPlayers(Player[] players){
        this.players=players;
        view.displayPlayers(players);
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
