package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoCubeTest {
    private static AmmoCube a, b;

    @BeforeAll
    static void init() {
        a = new AmmoCube(1, 2, 3);
        b = new AmmoCube(2, 0, 0);
    }

    @Test
    void testSimpleSum() {
        assertEquals(new AmmoCube(3, 2, 3), a.add(b));
    }

    @Test
    void testSimpleSub() {
        assertEquals(new AmmoCube(-1, 2, 3), a.sub(b));
    }

    @Test
    void testCap() {
        assertEquals(new AmmoCube(2, 2, 2), a.add(b).cap(2));
    }

    @Test
    void testGreater() {
        assertFalse(b.greaterEqThan(a));
        assertFalse(a.greaterEqThan(b));
        AmmoCube c = new AmmoCube(1);
        assertFalse(c.greaterEqThan(a));
        assertTrue(a.greaterEqThan(c));
    }

    @Test
    void testEquals() {
        assertNotEquals(a, new Object());
        assertNotEquals(a, b);
        assertEquals(a, a);
        assertEquals(a, new AmmoCube(1, 2, 3));
        assertEquals(a.hashCode(), new AmmoCube(1, 2, 3).hashCode());
        assertEquals(new AmmoCube(1), new AmmoCube(1, 0, 0));
        assertEquals(new AmmoCube(), new AmmoCube(0, 0));
        assertEquals(new AmmoCube(1).hashCode(), new AmmoCube(1, 0, 0).hashCode());
    }

}