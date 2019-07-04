package client.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Weapon {
    private final int id;
    private final String name;
    private final int[] reloadCost;
    private boolean loaded;

    public Weapon(int id, String name, int[] reloadCost) {
        this.name = name;
        this.reloadCost = reloadCost;
        this.id = id;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + " " + name + (loaded ? " LOADED" : " Not loaded, reload cost: ["
                + Arrays.stream(reloadCost)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(","))
                + "] ");
    }
}
