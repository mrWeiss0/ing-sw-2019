package model.weapon;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class OptionalWeaponTest {
    private static FireMode[] fm;
    private static List<Weapon> weapons = new ArrayList<Weapon>();

    @BeforeAll
    static void ba() {
        fm = new FireMode[]{
                new FireMode(),
                new FireMode(),
                new FireMode()
        };
        weapons.add(new OptionalWeapon.Builder()
                .fireModes(fm)
                .build());
        weapons.add(new OptionalWeapon.Builder()
                .fireModes(fm)
                .dependency(fm[1], fm[0])
                .dependency(fm[2], fm[0])
                .build());
        weapons.add(new OptionalWeapon.Builder()
                .fireModes(fm)
                .dependency(fm[1], fm[0])
                .dependency(fm[2], fm[1])
                .build());
        weapons.add(new OptionalWeapon.Builder()
                .fireModes(fm)
                .dependency(fm[2], fm[0])
                .build());
        // Wrong dependencies
        assertThrows(IllegalArgumentException.class, () -> new OptionalWeapon.Builder().fireModes(fm[0]).dependency(fm[0], new FireMode()).build());
        assertThrows(IllegalArgumentException.class, () -> new OptionalWeapon.Builder().fireModes(fm[0]).dependency(new FireMode(), fm[0]).build());
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
        Weapon w = weapons.get(0);
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
        Weapon w = weapons.get(0);
        assertFalse(w.validateFireModes(Stream.of(fm[0], fm[0]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[0], fm[1], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0], fm[1]).collect(Collectors.toList())));
        assertFalse(w.validateFireModes(Stream.of(fm[1], fm[0], fm[0]).collect(Collectors.toList())));
    }

    @Test
    void validateBaseDep() {
        Weapon w = weapons.get(1);
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
        Weapon w = weapons.get(2);
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
        Weapon w = weapons.get(3);
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

    @Test
    void validateNotPresent() {
        for (Weapon w : weapons)
            assertFalse(w.validateFireModes(Stream.of(fm[0], new FireMode()).collect(Collectors.toList())));
    }
}