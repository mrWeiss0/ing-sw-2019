package model.weapon;

import model.AmmoCube;
import model.Grabbable;

import java.util.ArrayList;
import java.util.List;

public class Weapon implements Grabbable {
    private AmmoCube pickupCost;
    private AmmoCube reloadCost;
    private boolean loaded;
    private List<FireMode> fireModes;

    public Weapon() {
        this(new AmmoCube(), new AmmoCube());
    }

    public Weapon(AmmoCube pickupCost, AmmoCube reloadCost) {
        this.pickupCost = pickupCost;
        this.reloadCost = reloadCost;
        loaded = false;
        fireModes = new ArrayList<>();
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

    public boolean validateFireModes(List<FireMode> selectedModes) {
        // TODO
        return true;
    }
}
