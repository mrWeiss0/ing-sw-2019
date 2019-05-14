package model;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * <code>AmmoCube</code> is a class used to represent players' available ammo
 * as well as all ammo costs in the game.
 * <p>
 * Ammo costs are divided in colors. This class provides methods necessary for
 * useful mathematical operation, such as sum or subtraction between ammo cube.
 */
public class AmmoCube {
    private final int[] ammo;

    /**
     * Constructs an AmmoCube containing the given amount of ammo for each
     * color.
     *
     * @param ammo
     */
    public AmmoCube(int... ammo) {
        int len = ammo.length;
        while (len > 0 && ammo[len - 1] == 0) --len;
        this.ammo = Arrays.copyOf(ammo, len);
    }

    /**
     * Adds the specified <code>AmmoCube</code> to this, summing only cubes of
     * equal color.
     *
     * @param toAdd the AmmoCube to be added
     * @return  the added AmmoCube
     */
    public AmmoCube add(AmmoCube toAdd) {
        return new AmmoCube(rangeAmmo(toAdd).
                map(i -> value(i) + toAdd.value(i)).toArray());
    }

    /**
     * Subtracts the specified <code>AmmoCube</code> to this, subtracting only
     * cubes of equal color.
     *
     * @param toSub the AmmoCube to be subbed
     * @return   the subbed AmmoCube
     */
    public AmmoCube sub(AmmoCube toSub) {
        return new AmmoCube(rangeAmmo(toSub).
                map(i -> value(i) - toSub.value(i)).toArray());
    }

    /**
     * Caps the amount of ammo to the given number for each color of this
     * AmmoCube.
     *
     * @param c the max amount of ammo for each color
     * @return  the AmmoCube once capped
     */
    public AmmoCube cap(int c) {
        return new AmmoCube(Arrays.stream(ammo).map(x -> Math.min(c, x)).toArray());
    }

    /**
     * Checks if this AmmoCube contains more ammo than the specified for each
     * ammo color.
     *
     * @param other the AmmoCube checked
     * @return true if this AmmoCube contains more ammo then the specified for
     *         each color
     */
    public boolean greaterEqThan(AmmoCube other) {
        return this.rangeAmmo(other).allMatch(i -> value(i) >= other.value(i));
    }

    //TODO is this useful?
    public int size() {
        return ammo.length;
    }

    /**
     * Returns the amount of ammo for a given color.
     *
     * @param i the index of the wanted color
     * @return  the amount of ammo for the given color
     */
    public int value(int i) {
        return i < ammo.length ? ammo[i] : 0;
    }

    //TODO is this useful?
    /**
     * Returns a string of numbers representing the amount of ammo of this
     * AmmoCube for every ammo color.
     *
     * @return a string of number representing the amount of ammo
     */
    @Override
    public String toString() {
        return Arrays.toString(ammo);
    }

    private IntStream rangeAmmo(AmmoCube other) {
        return IntStream.range(0, Math.max(size(), other.size()));
    }
}
