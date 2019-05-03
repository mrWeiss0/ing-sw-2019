package model;

import java.util.Arrays;
import java.util.stream.IntStream;

public class AmmoCube {
    private final int[] ammo;

    public AmmoCube() {
        this.ammo = new int[]{};
    }

    public AmmoCube(int... ammo) {
        int len = ammo.length;
        while (len > 0 && ammo[len - 1] == 0) --len;
        this.ammo = Arrays.copyOf(ammo, len);
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

    public boolean greaterEqThan(AmmoCube other) {
        return this.rangeAmmo(other).allMatch(i -> value(i) >= other.value(i));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AmmoCube))
            return false;
        AmmoCube other = (AmmoCube) obj;
        return this.rangeAmmo(other).
                allMatch(i -> value(i) == other.value(i));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(ammo);
    }

    public int length() {
        return ammo.length;
    }

    public int value(int i) {
        return i < ammo.length ? ammo[i] : 0;
    }

    private IntStream rangeAmmo(AmmoCube other) {
        return IntStream.range(0, Math.max(length(), other.length()));
    }
}
