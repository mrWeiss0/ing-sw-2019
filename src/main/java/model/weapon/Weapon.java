package model.weapon;

import model.AmmoCube;
import model.Grabbable;

import java.util.*;

/**
 * The <code>Weapon</code> class represents a weapon and its costs for pickup
 * and reload, as well as its FireModes; these are <code>final</code> as to reflect
 * a Weapon's static nature in the game. A Weapon also can be either loaded or
 * unloaded.
 * <p>
 * The class also provides a <code>Builder</code> to allow a new Weapon's setup
 * and creation. A Weapon may only be instantiated through the <code>Weapon.Builder</code>
 * class.
 * <p>
 * It also provides methods to load/unload a weapon and to check the whether
 * its loaded.
 * <p>
 * It also provides a method to check if a given list of FireModes is viable
 * for shooting.
 */
public class Weapon implements Grabbable {
    private final AmmoCube pickupCost;
    private final AmmoCube reloadCost;
    private final List<FireMode> fireModes;
    private boolean loaded = false;

    /**
     * Constructs a Weapon using the specified Builder.
     *
     * @param builder this Weapon's Builder
     */
    protected Weapon(Builder builder) {
        pickupCost = builder.pickupCost;
        reloadCost = builder.reloadCost;
        fireModes = Collections.unmodifiableList(builder.fireModes);
    }

    /**
     * Returns the list of this weapon's FireModes
     *
     * @return the list of this weapon's FireModes
     */
    public List<FireMode> getFireModes() {
        return fireModes;
    }

    /**
     * Returns the ammo needed to pickup this weapon
     *
     * @return the ammo needed to pickup this weapon
     */
    public AmmoCube getPickupCost() {
        return pickupCost;
    }

    /**
     * Returns the ammo needed to reload this weapon
     *
     * @return the ammo needed to reload this weapon
     */
    public AmmoCube getReloadCost() {
        return reloadCost;
    }

    /**
     * Sets this weapon as loaded.
     */
    public void load() {
        loaded = true;
    }

    /**
     * Sets this weapon as unloaded.
     */
    public void unload() {
        loaded = false;
    }

    /**
     * Checks if this weapon is loaded.
     *
     * @return true if this weapon is loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Checks if the specified FireMode's list is viable according to this
     * Weapon's rules. For a list to be vaid it should contain a single
     * FireMode which itself should belong to the weapon's available FireModes.
     *
     * @param selectedModes the list of FireMode to check for availability
     * @return true if the FireMode list is valid
     */
    public boolean validateFireModes(List<FireMode> selectedModes) {
        return selectedModes.size() == 1 && fireModes.contains(selectedModes.get(0));
    }

    /**
     * The <code>Weapon.Builder</code> class allows the construction of a new
     * <code>Weapon</code>.
     */
    public static class Builder {
        private final List<FireMode> fireModes = new ArrayList<>();
        private AmmoCube pickupCost = new AmmoCube();
        private AmmoCube reloadCost = new AmmoCube();

        /**
         * Sets the pickup cost for the weapon to be built.
         *
         * @param cost the pickup cost for the weapon to be built
         * @return  this builder
         */
        public Builder pickupCost(AmmoCube cost) {
            pickupCost = cost;
            return this;
        }

        /**
         * Sets reload cost for the weapon to be built.
         *
         * @param cost the reload cost for the weapon to be built
         * @return  this builder
         */
        public Builder reloadCost(AmmoCube cost) {
            reloadCost = cost;
            return this;
        }

        /**
         * Sets the specified FireModes for the weapon to be built.
         *
         * @param fireModes the FireModes for the weapon to be built
         * @return  this builder
         */
        public Builder fireModes(FireMode... fireModes) {
            return fireModes(Arrays.asList(fireModes));
        }

        /**
         * Sets the specified FireModes' collection for this weapon's builder
         *
         * @param fireModes the FireModes' collection for the weapon to be
         *                  built
         * @return  this builder
         */
        public Builder fireModes(Collection<FireMode> fireModes) {
            this.fireModes.addAll(fireModes);
            return this;
        }

        /**
         * Returns an instance of <code>Weapon</code> created from the fields
         * set on this builder.
         */
        public Weapon build() {
            return new Weapon(this);
        }
    }
}
