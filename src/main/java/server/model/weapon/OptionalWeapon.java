package server.model.weapon;

import server.model.AmmoCube;

import java.util.*;

public class OptionalWeapon extends Weapon {
    private final Map<FireMode, FireMode> dependency;

    private OptionalWeapon(Builder builder) {
        super(builder);
        for (Map.Entry<FireMode, FireMode> e : builder.dependency.entrySet())
            if (!(getFireModes().contains(e.getKey()) && getFireModes().contains(e.getValue())))
                throw new IllegalArgumentException("Invalid dependency " + e);
        dependency = Collections.unmodifiableMap(builder.dependency);
    }

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

    @SuppressWarnings("squid:S2176")
    public static class Builder extends Weapon.Builder {
        private final Map<FireMode, FireMode> dependency = new HashMap<>();

        @Override
        public Builder pickupCost(AmmoCube cost) {
            super.pickupCost(cost);
            return this;
        }

        @Override
        public Builder reloadCost(AmmoCube cost) {
            super.reloadCost(cost);
            return this;
        }

        @Override
        public Builder fireModes(FireMode... fireModes) {
            return fireModes(Arrays.asList(fireModes));
        }

        @Override
        public Builder fireModes(Collection<FireMode> fireModes) {
            super.fireModes(fireModes);
            return this;
        }

        public Builder dependency(FireMode after, FireMode before) {
            dependency.put(after, before);
            return this;
        }

        @Override
        public Weapon build() {
            return new OptionalWeapon(this);
        }
    }
}