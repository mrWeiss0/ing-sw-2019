package model;

@SuppressWarnings("squid:ClassVariableVisibilityCheck")
public class PowerUpImage {
    public int color;
    public PowerUpType type;
    public PowerUpImage(int color, PowerUpType type) {
        this.color = color;
        this.type=type;
    }
}
