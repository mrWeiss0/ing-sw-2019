package model.weapon;

import model.mock.MockOptionalWeapon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionalWeaponTest {
    public static FireMode[] fm;
    public static Weapon[] weapons;

    @BeforeAll
    static void ba() {
        fm = new FireMode[]{
                new FireMode(),
                new FireMode(),
                new FireMode()
        };
        OptionalWeapon[] wp = new OptionalWeapon[]{
                new MockOptionalWeapon(), // No dependencies
                new MockOptionalWeapon(), // Depend on base
                new MockOptionalWeapon(), // Chain dependency
                new MockOptionalWeapon()  // Not depend on base
        };
        for (Weapon w : wp)
            for (FireMode f : fm)
                w.addFireMode(f);
        wp[1].addDependency(fm[1], fm[0]);
        wp[1].addDependency(fm[2], fm[0]);
        wp[2].addDependency(fm[1], fm[0]);
        wp[2].addDependency(fm[2], fm[1]);
        wp[3].addDependency(fm[2], fm[0]);
        // Wrong dep ignored
        wp[0].addDependency(fm[0], new FireMode());
        wp[1].addDependency(fm[0], new FireMode());
        wp[2].addDependency(fm[0], new FireMode());
        wp[3].addDependency(fm[0], new FireMode());
        wp[0].addDependency(new FireMode(), fm[0]);
        wp[1].addDependency(new FireMode(), fm[0]);
        wp[2].addDependency(new FireMode(), fm[0]);
        wp[3].addDependency(new FireMode(), fm[0]);
        weapons = Arrays.copyOf(wp, wp.length, Weapon[].class);
    }

    @Test
    void validateEmpty() {
        for (Weapon w : weapons)
            assertFalse(w.validateFireModes(new ArrayList<>()));
    }

    @Test
    void validateBase() {
        for (Weapon w : weapons)
            assertTrue(w.validateFireModes(Collections.singletonList(fm[0])));
    }

    @Test
    void validateMultiple() {
        Weapon w = weapons[0];
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[1]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[2]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[1], fm[0]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[2], fm[0]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[1], fm[2]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[2], fm[1]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[1], fm[0], fm[2]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[2], fm[0], fm[1]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[2], fm[1], fm[0]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[1], fm[2], fm[0]).collect(Collectors.toList())));
    }

    @Test
    void validateRepeating() {
        Weapon w = weapons[0];
        assertFalse(w.validateFireModes(Stream.of(fm[0], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[0], fm[1], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0], fm[0]).collect(Collectors.toList())));
    }

    @Test
    void validateBaseDep() {
        Weapon w = weapons[1];
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[1]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[2]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[0]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[1], fm[2]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[2], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0], fm[2]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[0], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[1], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[2], fm[0]).collect(Collectors.toList())));
    }

    @Test
    void validateChainDep() {
        Weapon w = weapons[2];
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[0], fm[2]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[0]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[1], fm[2]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[0], fm[2], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0], fm[2]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[0], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[1], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[2], fm[0]).collect(Collectors.toList())));
    }

    @Test
    void validateNotBaseDep() {
        Weapon w = weapons[3];
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[1]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[2]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[1], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[0]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[1], fm[2]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[0], fm[2], fm[1]).collect(Collectors.toList())));
        assertTrue(w.validateFireModes(Stream.of(fm[1], fm[0], fm[2]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[0], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[2], fm[1], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[2], fm[0]).collect(Collectors.toList())));
    }

    @Test
    void validateNotBase() {
        for (Weapon w : weapons) {
            assertFalse(w.validateFireModes(Collections.singletonList(fm[1])));
            assertFalse(w.validateFireModes(Collections.singletonList(fm[2])));
            assertFalse(w.validateFireModes(Stream.of(fm[1], fm[2]).collect(Collectors.toList())));
            assertFalse(w.validateFireModes(Stream.of(fm[2], fm[1]).collect(Collectors.toList())));
        }
    }
}