package client.model;

import server.model.PowerUpType;

public class PowerUp {
    private final int color;
    private final int type;

    public PowerUp(int type, int color) {
        this.color = color;
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "powerUp: " + PowerUpType.values()[type].toString() + " " + color;
    }
}
