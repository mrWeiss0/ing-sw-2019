package model.weapon;

import model.AmmoCube;

import java.util.*;

public class OptionalWeapon extends Weapon {
    private Map<FireMode, FireMode> dependency;

    public OptionalWeapon() {
        super();
        dependency = new HashMap<>();
    }

    public OptionalWeapon(AmmoCube pickupCost, AmmoCube reloadCost) {
        super(pickupCost, reloadCost);
    }

    @Override
    public boolean validateFireModes(List<FireMode> selectedModes) {
        Set<FireMode> set = new HashSet<>();
        boolean baseMode = false;
        for (FireMode f : selectedModes) {
            // Must contain base mode
            if (f == getFireModes().get(0))
                baseMode = true;
            // Must not contain duplicates
            if (!set.add(f)) return false;
            // Dependencies must be respected
            FireMode depend = dependency.get(f);
            if (depend != null && !set.contains(depend)) return false;
        }
        return baseMode;
    }

    public void addDependency(FireMode after, FireMode before) {
        if (getFireModes().contains(after) && getFireModes().contains(before))
            dependency.put(after, before);
    }
}
