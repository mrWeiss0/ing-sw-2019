package server.model;

import server.model.board.SpawnSquare;
import server.model.weapon.FireStep;

import java.util.List;
import java.util.function.Consumer;

public class PowerUp {
    private final AmmoCube ammo;
    private final SpawnSquare spawn;
    private final Consumer<PowerUp> discard;
    private final List<FireStep> fireSteps;
    private final PowerUpType type;

    public PowerUp(AmmoCube ammo, SpawnSquare spawn, Consumer<PowerUp> discard, List<FireStep> steps, PowerUpType type) {
        this.ammo = ammo;
        this.spawn = spawn;
        this.discard = discard;
        this.fireSteps = steps;
        this.type = type;
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

    public PowerUpType getType() {
        return type;
    }

    public List<FireStep> getFireSteps() {
        return fireSteps;
    }
}
