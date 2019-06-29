package client.model;

public class Weapon {
    private boolean loaded;
    private final int id;

    public Weapon(int id) {
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
        return id + ": " + loaded;
    }
}
