package server.model;

@SuppressWarnings("squid:ClassVariableVisibilityCheck")
public class AmmoTileImage {
    public int[] ammo;
    public boolean powerUp;

    public AmmoTileImage(boolean powerUp, int... ammo) {
        this.ammo = ammo;
        this.powerUp = powerUp;
    }
}
