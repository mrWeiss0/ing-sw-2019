package model;

import java.util.Arrays;
import java.util.stream.IntStream;

public class AmmoCube {
    private final int[] ammo;

    public AmmoCube() {
        this.ammo = new int[]{};
    }

    public AmmoCube(int... ammo) {
        this.ammo = Arrays.copyOf(ammo, ammo.length);
    }

    public AmmoCube add(AmmoCube toAdd) {
        return new AmmoCube(this.rangeAmmo(toAdd).
                map(i -> value(i) + toAdd.value(i)).toArray());
    }

    public AmmoCube sub(AmmoCube toSub) {
        return new AmmoCube(this.rangeAmmo(toSub).
                map(i -> value(i) - toSub.value(i)).toArray());
    }

    public AmmoCube cap(int c) {
        return new AmmoCube(Arrays.stream(ammo).map(x -> Math.min(c, x)).toArray());
    }

    public boolean greaterThan(AmmoCube other) {
        return this.rangeAmmo(other).filter(i -> value(i) < other.value(i)).count() == 0;

    }

    public int[] value() {
        return ammo;
    }

    public int value(int i) {
        return i < ammo.length ? ammo[i] : 0;
    }

    private IntStream rangeAmmo(AmmoCube other) {
        return IntStream.range(0, Math.max(ammo.length, other.value().length));
    }
}
