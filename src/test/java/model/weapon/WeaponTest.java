package model.weapon;

import model.AmmoCube;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {
    public static AmmoCube pc, lc;
    public static FireMode[] fm;
    private Weapon weapon;

    @BeforeAll
    static void ba() {
        pc = new AmmoCube(1, 2);
        lc = new AmmoCube(2);
        fm = new FireMode[]{
                new FireMode(),
                new FireMode(),
                new FireMode()
        };
    }

    @BeforeEach
    void init() {
        weapon = new Weapon(pc, lc);
        for (FireMode f : fm)
            weapon.addFireMode(f);
    }

    @Test
    void testGeneric() {
        weapon.load();
        assertTrue(weapon.isLoaded());
        weapon.unload();
        assertFalse(weapon.isLoaded());
        assertEquals(weapon.getPickupCost(), pc);
        assertEquals(weapon.getReloadCost(), lc);
    }

    @Test
    void testAdd() {
        assertArrayEquals(fm, weapon.getFireModes().toArray());
        assertTrue(weapon.getFireModes().containsAll(Arrays.asList(fm)));
    }

    @Test
    void validateEmpty() {
        assertFalse(weapon.validateFireModes(new ArrayList<>()));
    }

    @Test
    void validatePresent() {
        assertTrue(weapon.validateFireModes(Collections.singletonList(fm[0])));
        assertTrue(weapon.validateFireModes(Collections.singletonList(fm[1])));
        assertTrue(weapon.validateFireModes(Collections.singletonList(fm[2])));
    }

    @Test
    void validateMultiple() {
        assertFalse(weapon.validateFireModes(Stream.of(fm[0], fm[1]).collect(Collectors.toList())));
    }

    @Test
    void validateNotPresent() {
        assertFalse(weapon.validateFireModes(Collections.singletonList(new FireMode())));
    }
}