package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AmmoCubeTest {
    @Test
    void testSimpleSum() {
        AmmoCube a = new AmmoCube(new ArrayList<>(Arrays.asList(1, 2, 3)));
        AmmoCube b = new AmmoCube(new ArrayList<>(Arrays.asList(2, 0, 0)));
        AmmoCube c = a.add(b);
        ArrayList<Integer> r = new ArrayList<>(Arrays.asList(3, 2, 3));
        assertEquals(r, c.getAmmos());
    }

    @Test
    void testSumLimit() {
        AmmoCube a = new AmmoCube(new ArrayList<>(Arrays.asList(1, 2, 3)));
        AmmoCube b = new AmmoCube(new ArrayList<>(Arrays.asList(3, 0, 0)));
        AmmoCube c = a.add(b);
        ArrayList<Integer> r = new ArrayList<>(Arrays.asList(4, 2, 3));
        assertEquals(r, c.getAmmos());
    }

    @Test
    void testSimpleSub() {
        AmmoCube a = new AmmoCube(new ArrayList<>(Arrays.asList(1, 2, 3)));
        AmmoCube b = new AmmoCube(new ArrayList<>(Arrays.asList(1, 0, 0)));
        AmmoCube c = a.sub(b);
        ArrayList<Integer> r = new ArrayList<>(Arrays.asList(0, 2, 3));
        assertEquals(r, c.getAmmos());
    }

    @Test
    void testSubLimit() {
        AmmoCube a = new AmmoCube(new ArrayList<>(Arrays.asList(1, 2, 3)));
        AmmoCube b = new AmmoCube(new ArrayList<>(Arrays.asList(3, 0, 0)));
        AmmoCube c = a.sub(b);
        ArrayList<Integer> r = new ArrayList<>(Arrays.asList(-2, 2, 3));
        assertEquals(r, c.getAmmos());
    }

    @Test
    void testCap() {
        AmmoCube a = new AmmoCube(new ArrayList<>(Arrays.asList(1, 2, 3)));
        AmmoCube b = new AmmoCube(new ArrayList<>(Arrays.asList(3, 0, 0)));
        AmmoCube c = a.add(b);
        c = c.cap(3);
        ArrayList<Integer> r = new ArrayList<>(Arrays.asList(3, 2, 3));
        assertEquals(r, c.getAmmos());
    }

    @Test
    void testGreater() {
        AmmoCube a = new AmmoCube(new ArrayList<>(Arrays.asList(1, 2, 3)));
        AmmoCube b = new AmmoCube(new ArrayList<>(Arrays.asList(3, 0, 0)));
        assertFalse(b.greaterThan(a));
        assertFalse(a.greaterThan(b));
        a = new AmmoCube(new ArrayList<>(Arrays.asList(1, 2, 3)));
        b = new AmmoCube(new ArrayList<>(Arrays.asList(1, 0, 0)));
        assertFalse(b.greaterThan(a));
        assertTrue(a.greaterThan(b));
    }

}