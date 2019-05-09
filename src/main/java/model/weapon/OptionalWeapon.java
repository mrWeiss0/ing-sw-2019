package model.weapon;

import model.AmmoCube;

import java.util.*;

public class OptionalWeapon extends Weapon {
    private final Map<FireMode, FireMode> dependency = new HashMap<>();

    public OptionalWeapon(AmmoCube pickupCost, AmmoCube reloadCost) {
        super(pickupCost, reloadCost);
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

    public void addDependency(FireMode after, FireMode before) {
        if (getFireModes().contains(after) && getFireModes().contains(before))
            dependency.put(after, before);
    }
}
