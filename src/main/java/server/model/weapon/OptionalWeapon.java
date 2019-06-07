package server.model.weapon;

import server.model.AmmoCube;

import java.util.*;

/**
 * The <code>OptionalWeapon</code> class extends the <code>Weapon</code> class
 * providing the ability to validate multiple FireModes at once; dependencies
 * are now checked so that only specific FireModes' combination may be used.
 * <p>
 * A new <code>Builder</code> is provided so that FireModes' dependencies may
 * be set using its method. As per a regular Weapon, an OptionalWeapon may be
 * instantiated through its Builder.
 */
public class OptionalWeapon extends Weapon {
    private final Map<FireMode, FireMode> dependency;

    private OptionalWeapon(Builder builder) {
        super(builder);
        for (Map.Entry<FireMode, FireMode> e : builder.dependency.entrySet())
            if (!(getFireModes().contains(e.getKey()) && getFireModes().contains(e.getValue())))
                throw new IllegalArgumentException("Invalid dependency " + e);
        dependency = Collections.unmodifiableMap(builder.dependency);
    }

    /**
     * Checks if the specified FireMode's list is viable according to this
     * Weapon's rules. For a list to be valid it should contain only FireModes
     * that are available for this Weapon; it should also contain exactly one
     * base FireMode and all dependencies should be respected so that if a
     * FireMode is dependent on another, the latter should precede the former.
     *
     * @param selectedModes the list of FireMode to check for availability
     * @return true if the list is valid
     */
    @Override
    public boolean validateFireModes(List<FireMode> selectedModes) {
        Set<FireMode> set = new HashSet<>();
        boolean baseMode = false;
        for (FireMode f : selectedModes) {
            FireMode depend = dependency.get(f);
            if (!(getFireModes().contains(f) && // Must be in this weapon
                    set.add(f) && // Must not contain duplicates
                    (depend == null || set.contains(depend)))) // Dependencies must be respected
                return false;
            // Must contain base mode
            if (!baseMode && f == getFireModes().get(0))
                baseMode = true;
        }
        return baseMode;
    }

    /**
     * The <code>OptionalWeapon.Builder</code> class allows the construction
     * of a new <code>OptionalWeapon</code>.
     */
    @SuppressWarnings("squid:S2176")
    public static class Builder extends Weapon.Builder {
        private final Map<FireMode, FireMode> dependency = new HashMap<>();

        /**
         * Sets the pickup cost for the weapon to be built.
         *
         * @param cost the pickup cost for the weapon to be built
         * @return this builder
         */
        @Override
        public Builder pickupCost(AmmoCube cost) {
            super.pickupCost(cost);
            return this;
        }

        /**
         * Sets reload cost for the weapon to be built.
         *
         * @param cost the reload cost for the weapon to be built
         * @return this builder
         */
        @Override
        public Builder reloadCost(AmmoCube cost) {
            super.reloadCost(cost);
            return this;
        }

        /**
         * Sets the specified FireModes for the weapon to be built.
         *
         * @param fireModes the FireModes for the weapon to be built
         * @return this builder
         */
        @Override
        public Builder fireModes(FireMode... fireModes) {
            return fireModes(Arrays.asList(fireModes));
        }

        /**
         * Sets the specified FireModes' collection for this weapon's builder
         *
         * @param fireModes the FireModes' collection for the weapon to be
         *                  built
         * @return this builder
         */
        @Override
        public Builder fireModes(Collection<FireMode> fireModes) {
            super.fireModes(fireModes);
            return this;
        }

        /**
         * Sets the first FireMode specified as dependent from the second.
         *
         * @param after  the dependent FireMode
         * @param before the previous FireMode dependee
         * @return this builder
         */
        public Builder dependency(FireMode after, FireMode before) {
            dependency.put(after, before);
            return this;
        }

        /**
         * Returns an instance of <code>Weapon</code> created with the fields
         * set on this builder.
         *
         * @return the weapon instantiated
         */
        @Override
        public Weapon build() {
            return new OptionalWeapon(this);
        }
    }
}
