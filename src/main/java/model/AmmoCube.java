package model;

import java.util.Arrays;
import java.util.stream.IntStream;

public class AmmoCube {
    private final int[] ammo;

    public AmmoCube(int... ammo) {
        int len = ammo.length;
        while (len > 0 && ammo[len - 1] == 0) --len;
        this.ammo = Arrays.copyOf(ammo, len);
    }

    public AmmoCube add(AmmoCube toAdd) {
        return new AmmoCube(rangeAmmo(toAdd).
                map(i -> value(i) + toAdd.value(i)).toArray());
    }

    public AmmoCube sub(AmmoCube toSub) {
        return new AmmoCube(rangeAmmo(toSub).
                map(i -> value(i) - toSub.value(i)).toArray());
    }

    public AmmoCube cap(int c) {
        return new AmmoCube(Arrays.stream(ammo).map(x -> Math.min(c, x)).toArray());
    }

    public boolean greaterEqThan(AmmoCube other) {
        return this.rangeAmmo(other).allMatch(i -> value(i) >= other.value(i));
    }

    public int size() {
        return ammo.length;
    }

    public int value(int i) {
        return i < ammo.length ? ammo[i] : 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(ammo);
    }

    private IntStream rangeAmmo(AmmoCube other) {
        return IntStream.range(0, Math.max(size(), other.size()));
    }
}
