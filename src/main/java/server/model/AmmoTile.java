package server.model;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <code>AmmoTile</code> class represents a tile containing ammo, a PowerUp
 * supplier and a consumer to which discard itself; these are <code>final</code>
 * as to reflect an AmmoTile's static nature in game.
 * <p>
 * The class provides methods to get ammo and draw an optional PowerUp from a
 * supplier, as well as a method to discard the AmmoTile to its deck once its
 * contents are exhausted.
 * <p>
 * It also implements the empty Grabbable interface, which allows an AmmoTile
 * to be grabbed.
 */
public class AmmoTile implements Grabbable {
    private final AmmoCube ammo;
    private final Supplier<PowerUp> powerUp;
    private final Consumer<AmmoTile> discard;

    /**
     * Constructs an AmmoTile with the specified AmmoCube, a PowerUp supplier
     * and a consumer deck to which this AmmoTile is to be discarded.
     *
     * @param ammo    the amount of ammo to be given
     * @param powerUp the supplier for the PowerUp
     * @param discard the consumer for the discard
     */
    public AmmoTile(AmmoCube ammo, Supplier<PowerUp> powerUp, Consumer<AmmoTile> discard) {
        this.ammo = ammo;
        this.powerUp = powerUp;
        this.discard = discard;
    }

    /**
     * Returns a PowerUp from the PowerUp deck if this tile has been generated
     * with it, else returns <code>null</code>.
     *
     * @return a PowerUp if the tile has been generated with it, else
     * null
     */
    public Optional<PowerUp> getPowerUp() {
        return Optional.ofNullable(powerUp.get());
    }

    /**
     * Returns the AmmoCube contained within this AmmoTile.
     *
     * @return the AmmoCube contained within this AmmoTile
     */
    public AmmoCube getAmmo() {
        return ammo;
    }

    /**
     * Adds this to the AmmoTile's deck discard pile
     */
    public void discard() {
        discard.accept(this);
    }
}
