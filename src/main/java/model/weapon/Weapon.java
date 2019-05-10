package model.weapon;

import model.AmmoCube;
import model.Grabbable;

import java.util.*;

public class Weapon implements Grabbable {
    private final AmmoCube pickupCost;
    private final AmmoCube reloadCost;
    private final List<FireMode> fireModes;
    private boolean loaded = false;

    protected Weapon(Builder builder) {
        pickupCost = builder.pickupCost;
        reloadCost = builder.reloadCost;
        fireModes = Collections.unmodifiableList(builder.fireModes);
    }

    public List<FireMode> getFireModes() {
        return fireModes;
    }

    public AmmoCube getPickupCost() {
        return pickupCost;
    }

    public AmmoCube getReloadCost() {
        return reloadCost;
    }

    public void load() {
        loaded = true;
    }

    public void unload() {
        loaded = false;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean validateFireModes(List<FireMode> selectedModes) {
        return selectedModes.size() == 1 && fireModes.contains(selectedModes.get(0));
    }

    public static class Builder {
        private final List<FireMode> fireModes = new ArrayList<>();
        private AmmoCube pickupCost = new AmmoCube();
        private AmmoCube reloadCost = new AmmoCube();

        public Builder pickupCost(AmmoCube cost) {
            pickupCost = cost;
            return this;
        }

        public Builder reloadCost(AmmoCube cost) {
            reloadCost = cost;
            return this;
        }

        public Builder fireModes(FireMode... fireModes) {
            return fireModes(Arrays.asList(fireModes));
        }

        public Builder fireModes(Collection<FireMode> fireModes) {
            this.fireModes.addAll(fireModes);
            return this;
        }

        public Weapon build() {
            return new Weapon(this);
        }
    }
}
