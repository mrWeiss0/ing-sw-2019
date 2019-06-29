package client.model;

import client.view.View;

import java.util.Arrays;

public class Board {
    private final View view;
    private int mapType;
    private int maxKills;
    private int[] killTrack;
    private boolean[] overkills;
    private Player[] players;
    private Square[] squares;


    public Board(View view) {
        this.view = view;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
        view.displayMapType(mapType);
    }

    public int getMaxKills() {
        return maxKills;
    }

    public void setMaxKills(int maxKills) {
        this.maxKills = maxKills;
        view.displayMaxKills(maxKills);
    }

    public int[] getKillTrack() {
        return killTrack;
    }

    public void setKillTrack(int[] killTrack) {
        this.killTrack = killTrack;
    }

    public boolean[] getOverkills() {
        return overkills;
    }

    public void setOverkills(boolean[] overkills) {
        this.overkills = overkills;
        view.displayKillTrack(killTrack, overkills);
    }

    public Square[] getSquares() {
        return squares;
    }

    public void setSquares(Square[] squares) {
        this.squares = squares;
        view.displaySquares(squares);
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
        view.displayPlayers(players);
    }

    public Square[] getRoom(int id) {
        return (Square[]) Arrays.stream(squares).filter(x -> x.getRoom() == id).toArray();
    }
}
