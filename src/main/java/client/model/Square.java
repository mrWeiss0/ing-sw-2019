package client.model;

import client.view.View;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Square {

    private final View view;
    private final int[] coordinates;
    private final boolean spawn;
    private final int room;
    private int tileID;
    private int[] weapons = new int[]{-1, -1, -1};
    private int[][] pcost;


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
        this.tileID = id;
    }

    public int[] getWeapons() {
        return weapons;
    }

    public void setWeapons(int[] weapons, int[][] pcost) {
        this.weapons = weapons;
        this.pcost = pcost;
        view.displaySquareContent(this);
    }

    public int getTileId() {
        return tileID;
    }

    @Override
    public String toString() {
        return "s: " + spawn + " at (" + coordinates[0] + "," + coordinates[1] + ") : " + (spawn ?
                IntStream.range(0, weapons.length).mapToObj(x -> weapons[x] + " [" + Arrays.stream(pcost[x])
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining(",")) + "] ")
                        .collect(Collectors.joining(" "))
                : "TileID:" + tileID);
    }
}
