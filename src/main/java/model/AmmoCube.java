package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AmmoCube {
    private final List<Integer> ammo;

    public AmmoCube() {
        this.ammo = new ArrayList<>();
    }

    public AmmoCube(List<Integer> ammo) {
        this.ammo = ammo;
    }

    public AmmoCube add(AmmoCube toAdd) {
        return new AmmoCube(this.rangeAmmo(toAdd).
                mapToObj(i -> ammo.get(i) + toAdd.getAmmo().get(i)).collect(Collectors.toList()));
    }

    public AmmoCube sub(AmmoCube toSub) {
        return new AmmoCube(this.rangeAmmo(toSub).
                mapToObj(i -> ammo.get(i) - toSub.getAmmo().get(i)).collect(Collectors.toList()));
    }

    public AmmoCube cap(int c) {
        return new AmmoCube(ammo.stream().map(x -> Integer.min(c, x)).collect(Collectors.toList()));
    }

    public boolean greaterThan(AmmoCube other) {
        return this.rangeAmmo(other).filter(i -> ammo.get(i) < other.getAmmo().get(i)).count() == 0;

    }

    public List<Integer> getAmmo() {
        return ammo;
    }

    private IntStream rangeAmmo(AmmoCube other) {
        return IntStream.range(0, Math.min(ammo.size(), other.getAmmo().size()));
    }
}
