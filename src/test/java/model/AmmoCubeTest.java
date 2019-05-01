package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoCubeTest {
    @Test
    void testSimpleSum() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(2, 0, 0);
        AmmoCube c = a.add(b);
        assertArrayEquals(new int[]{3, 2, 3}, c.value());
    }

    @Test
    void testSumLimit() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(3, 0, 0);
        AmmoCube c = a.add(b);
        assertArrayEquals(new int[]{4, 2, 3}, c.value());
    }

    @Test
    void testSimpleSub() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(1, 0, 0);
        AmmoCube c = a.sub(b);
        assertArrayEquals(new int[]{0, 2, 3}, c.value());
    }

    @Test
    void testSubLimit() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(3, 0, 0);
        AmmoCube c = a.sub(b);
        assertArrayEquals(new int[]{-2, 2, 3}, c.value());
    }

    @Test
    void testCap() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(3, 0, 0);
        AmmoCube c = a.add(b);
        c = c.cap(3);
        assertArrayEquals(new int[]{3, 2, 3}, c.value());
    }

    @Test
    void testGreater() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(3, 0, 0);
        assertFalse(b.greaterThan(a));
        assertFalse(a.greaterThan(b));
        a = new AmmoCube(1, 2, 3);
        b = new AmmoCube(1, 0, 0);
        assertFalse(b.greaterThan(a));
        assertTrue(a.greaterThan(b));
    }

    @Test
    void testZero() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(2);
        AmmoCube c = b.add(a);
        assertEquals(0, b.value(1));
        assertArrayEquals(new int[]{3, 2, 3}, c.value());
    }

}