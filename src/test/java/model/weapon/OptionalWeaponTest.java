package model.weapon;

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
    private static FireMode[] fm;
    private static Weapon[] weapons;

    @BeforeAll
    static void ba() {
        fm = new FireMode[]{
                new FireMode(),
                new FireMode(),
                new FireMode()
        };
        final OptionalWeapon[] wp = new OptionalWeapon[]{
                new OptionalWeaponMock(), // No dependencies
                new OptionalWeaponMock(), // Depend on base
                new OptionalWeaponMock(), // Chain dependency
                new OptionalWeaponMock()  // Not depend on base
        };
        for (final Weapon w : wp)
            for (final FireMode f : fm)
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
        for (final Weapon w : weapons)
            assertFalse(w.validateFireModes(new ArrayList<>()));
    }

    @Test
    void validateBase() {
        for (final Weapon w : weapons)
            assertTrue(w.validateFireModes(Collections.singletonList(fm[0])));
    }

    @Test
    void validateMultiple() {
        final Weapon w = weapons[0];
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
        final Weapon w = weapons[0];
        assertFalse(w.validateFireModes(Stream.of(fm[0], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[0], fm[1], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0], fm[0]).collect(Collectors.toList())));
    }

    @Test
    void validateBaseDep() {
        final Weapon w = weapons[1];
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
        final Weapon w = weapons[2];
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
        final Weapon w = weapons[3];
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
        for (final Weapon w : weapons) {
            assertFalse(w.validateFireModes(Collections.singletonList(fm[1])));
            assertFalse(w.validateFireModes(Collections.singletonList(fm[2])));
            assertFalse(w.validateFireModes(Stream.of(fm[1], fm[2]).collect(Collectors.toList())));
            assertFalse(w.validateFireModes(Stream.of(fm[2], fm[1]).collect(Collectors.toList())));
        }
    }

    @Test
    void validateNotPresent() {
        for (final Weapon w : weapons)
            assertFalse(w.validateFireModes(Stream.of(fm[0], new FireMode()).collect(Collectors.toList())));
    }
}