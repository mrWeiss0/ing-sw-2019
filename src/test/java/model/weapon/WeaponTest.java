package model.weapon;

import model.AmmoCube;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {
    private static FireMode[] fm;
    private static Weapon weapon;

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
    static void ba() {
        fm = new FireMode[]{
                new FireMode(),
                new FireMode(),
                new FireMode()
        };
        weapon = new Weapon.Builder()
                .pickupCost(new AmmoCube(1, 2))
                .reloadCost(new AmmoCube(2))
                .fireModes(fm)
                .build();
    }

    @Test
    void testGeneric() {
        weapon.load();
        assertTrue(weapon.isLoaded());
        weapon.unload();
        assertFalse(weapon.isLoaded());
        assertTrue(ammoCubeEquals(weapon.getPickupCost(), new AmmoCube(1, 2)));
        assertTrue(ammoCubeEquals(weapon.getReloadCost(), new AmmoCube(2)));
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