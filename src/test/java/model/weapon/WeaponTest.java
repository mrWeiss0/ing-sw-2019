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
    public static FireMode[] fm;
    public static Weapon weapon;

    @BeforeAll
    static void ba() {
        fm = new FireMode[]{
                new FireMode(),
                new FireMode(),
                new FireMode()
        };
        weapon = new Weapon(new AmmoCube(1, 2), new AmmoCube(2));
        for (FireMode f : fm)
            weapon.addFireMode(f);
    }

    @BeforeEach
    void init() {
    }

    @Test
    void testGeneric() {
        weapon.load();
        assertTrue(weapon.isLoaded());
        weapon.unload();
        assertFalse(weapon.isLoaded());
        assertEquals(weapon.getPickupCost(), new AmmoCube(1, 2));
        assertEquals(weapon.getReloadCost(), new AmmoCube(2));
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