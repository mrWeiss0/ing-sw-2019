package model;

import model.board.SpawnSquare;

import java.util.function.Consumer;

public class PowerUp {
    private final AmmoCube ammo;
    private final SpawnSquare spawn;
    private final Consumer<PowerUp> discard;

    public PowerUp(AmmoCube ammo, SpawnSquare spawn, Consumer<PowerUp> discard) {
        this.ammo = ammo;
        this.spawn = spawn;
        this.discard = discard;
    }

    public AmmoCube getAmmo() {
        return ammo;
    }

    public SpawnSquare getSpawn() {
        return spawn;
    }

    public void discard() {
        discard.accept(this);
    }

}
