package server.model;

@SuppressWarnings("squid:ClassVariableVisibilityCheck")
public class AmmoTileImage {
    public int[] ammo;
    public boolean powerUp;
    public int id;

    public AmmoTileImage(int id, boolean powerUp, int... ammo) {
        this.id=id;
        this.ammo = ammo;
        this.powerUp = powerUp;
    }
}
