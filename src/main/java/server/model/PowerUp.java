package server.model;

import server.model.board.SpawnSquare;
import server.model.weapon.FireStep;

import java.util.List;
import java.util.function.Consumer;

/**
 * The <code>PowerUp</code> class represents a generic PowerUp card containing
 * ammo and linked to a SpawnPoint; these are <code>final</code> as to reflect
 * a PowerUp's card static nature in the game.
 * <p>
 * It provides methods to get its associated ammo and spawn.
 */
public class PowerUp {
    private final AmmoCube ammo;
    private final SpawnSquare spawn;
    private final Consumer<PowerUp> discard;
    private final List<FireStep> fireSteps;
    private final PowerUpType type;

    /**
     * Constructs a PowerUp with the specified ammo and spawnpoint.
     *
     * @param ammo the ammo that this PowerUp can be traded for
     * @param spawn the spawnpoint that this PowerUp can be used to spawn at
     */
    public PowerUp(AmmoCube ammo, SpawnSquare spawn, Consumer<PowerUp> discard, List<FireStep> steps, PowerUpType type) {
        this.ammo = ammo;
        this.spawn = spawn;
        this.discard = discard;
        this.fireSteps = steps;
        this.type = type;
    }

    /**
     * Returns the ammo this PowerUp can be traded for.
     *
     * @return the ammo this PowerUp cn be traded for
     */
    public AmmoCube getAmmo() {
        return ammo;
    }

    /**
     * Returns the spawnpoint this PowerUp can be used to spawn at
     *
     * @return the spawnpoint this PowerUp can be used to spawn at
     */
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
