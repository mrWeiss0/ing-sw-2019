package model.weapon;

import model.AmmoCube;
import model.Grabbable;

import java.util.ArrayList;
import java.util.List;

public class Weapon implements Grabbable {
    private AmmoCube pickupCost;
    private AmmoCube reloadCost;
    private boolean loaded = false;
    private List<FireMode> fireModes = new ArrayList<>();

    public Weapon() {
        this(new AmmoCube(), new AmmoCube());
    }

    public Weapon(AmmoCube pickupCost, AmmoCube reloadCost) {
        this.pickupCost = pickupCost;
        this.reloadCost = reloadCost;
    }

    public void addFireMode(FireMode fireMode) {
        fireModes.add(fireMode);
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
}
