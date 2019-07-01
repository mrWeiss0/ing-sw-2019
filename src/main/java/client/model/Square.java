package client.model;

import client.view.View;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Square {

    private View view;
    private int[] coordinates;
    private boolean spawn;
    private int room;
    private int tileID;
    private int[] weapons;


    public Square(View view, int[] coordinates, boolean spawn, int room) {
        this.coordinates = coordinates;
        this.spawn = spawn;
        this.room = room;
        this.view = view;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public boolean isSpawn() {
        return spawn;
    }

    public int getRoom() {
        return room;
    }

    public void setTileID(int id) {
        this.tileID=id;
    }

    public int[] getWeapons() {
        return weapons;
    }

    public void setWeapons(int[] weapons) {
        this.weapons = weapons;
        view.displaySquareContent(this);
    }

    public int getTileId() {
        return tileID;
    }

    @Override
    public String toString() {
        return "s: " + spawn + " at (" + coordinates[0] + "," + coordinates[1] + ") : " + (spawn ?
                Arrays.stream(weapons)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining(" "))
                : "TileID:"+tileID);
    }
}
