package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmmoCubeTest {
    private static AmmoCube a, b;

    private static boolean ammoCubeEquals(AmmoCube a, AmmoCube b) {
        try {
            Field f = AmmoCube.class.getDeclaredField("ammo");
            f.setAccessible(true);
            return Objects.deepEquals(f.get(a), f.get(b));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    @BeforeAll
    static void init() {
        a = new AmmoCube(1, 2, 3);
        b = new AmmoCube(2, 0, 0);
    }

    @Test
    void testZero() {
        assertTrue(ammoCubeEquals(new AmmoCube(), new AmmoCube(0, 0)));
        assertTrue(ammoCubeEquals(new AmmoCube(1), new AmmoCube(1, 0, 0)));
    }

    @Test
    void testSimpleSum() {
        assertTrue(ammoCubeEquals(new AmmoCube(3, 2, 3), a.add(b)));
    }

    @Test
    void testSimpleSub() {
        assertTrue(ammoCubeEquals(new AmmoCube(-1, 2, 3), a.sub(b)));
    }

    @Test
    void testCap() {
        assertTrue(ammoCubeEquals(new AmmoCube(2, 2, 2), a.add(b).cap(2)));
    }

    @Test
    void testGreater() {
        assertFalse(b.greaterEqThan(a));
        assertFalse(a.greaterEqThan(b));
        AmmoCube c = new AmmoCube(1);
        assertFalse(c.greaterEqThan(a));
        assertTrue(a.greaterEqThan(c));
    }

}