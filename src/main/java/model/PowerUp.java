package model;

import model.board.SpawnSquare;

public class PowerUp {
    private final AmmoCube ammo;
    private final SpawnSquare spawn;

    public PowerUp(AmmoCube ammo, SpawnSquare spawn) {
        this.ammo = ammo;
        this.spawn = spawn;
    }

    public AmmoCube getAmmo() {
        return ammo;
    }

    public SpawnSquare getSpawn() {
        return spawn;
    }
}
