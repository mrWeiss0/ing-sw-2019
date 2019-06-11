package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.board.*;
import server.model.weapon.OptionalWeapon;
import server.model.weapon.Weapon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GrabTest {
    private Figure f;
    private AbstractSquare[] squares;
    private Grabbable[] items;
    private PowerUp pup;
    private AmmoTile discard;

    @BeforeEach
    void init() {
        f = new Figure(10, 12, 3, 3, 2, 1);
        Weapon.Builder weapon = new Weapon.Builder();
        Weapon.Builder optionalWeapon = new OptionalWeapon.Builder();
        squares = new AbstractSquare[]{new AmmoSquare(new Room(), new int[]{}), new SpawnSquare(new Room(), new int[]{}, 3)};
        pup = new PowerUp(null, null, null, null);
        items = new Grabbable[]{
                weapon.build(),
                optionalWeapon.build(),
                weapon.build(),
                optionalWeapon.build(),
                new AmmoTile(new AmmoCube(1, 2, 3), () -> pup, (i) -> discard = i),
                new AmmoTile(new AmmoCube(1), () -> new PowerUp(null, null, null, null), (i) -> discard = i)
        };
    }

    @Test
    void testEmpty() {
        List<Grabbable> grabbed = squares[0].peek();
        assertEquals(new ArrayList<>(), grabbed);
    }

    @Test
    void testAmmo() {
        squares[0].refill(items[4]);
        assertEquals(Stream.of(items[4]).collect(Collectors.toSet()), squares[0].peek());
    }

    @Test
    void testAmmoNot() {
        squares[0].refill(items[4]);
        assertNotEquals(Stream.of(items[5]).collect(Collectors.toSet()), squares[0].peek());
        assertNotEquals(Stream.of(items[3]).collect(Collectors.toSet()), squares[0].peek());
    }

    @Test
    void testSpawn() {
        squares[1].refill(items[0]);
        squares[1].refill(items[1]);
        assertEquals(Stream.of(items[1], items[0]).collect(Collectors.toSet()), squares[1].peek());
    }

    @Test
    void testAmmoException() {
        assertThrows(ClassCastException.class, () -> squares[1].refill(items[4]));
    }

    @Test
    void testSpawnException() {
        assertThrows(ClassCastException.class, () -> squares[0].refill(items[0]));
    }

    @Test
    void testGrabWeapon() {
        squares[1].refill(items[0]);
        squares[1].grab(f, items[0]);
        assertEquals(Stream.of(items[0]).collect(Collectors.toList()), f.getWeapons());
    }

    @Test
    void grab2WtestEapon() {
        squares[1].refill(items[0]);
        squares[1].refill(items[1]);
        squares[1].refill(items[2]);
        squares[1].grab(f, items[1]);
        assertThrows(IllegalStateException.class, () -> squares[1].grab(f, items[1]));
        squares[1].grab(f, items[0]);
        assertEquals(Stream.of(items[1], items[0]).collect(Collectors.toList()), f.getWeapons());
        assertThrows(IllegalStateException.class, () -> squares[1].grab(f, items[2]));
    }

    @Test
    void testGrabNotWeapon() {
        squares[1].refill(items[0]);
        assertThrows(IllegalStateException.class, () -> squares[1].grab(f, items[1]));
        assertThrows(ClassCastException.class, () -> squares[1].grab(f, items[4]));
        assertEquals(Stream.of().collect(Collectors.toList()), f.getWeapons());
    }

    @Test
    void testGrabAmmo() {
        squares[0].refill(items[4]);
        squares[0].grab(f, items[4]);
        assertEquals(Stream.of(pup).collect(Collectors.toList()), f.getPowerUps());
        assertTrue(IntStream.range(0, 3).allMatch(i -> new AmmoCube(1, 2, 3).value(i) == f.getAmmo().value(i)));
        assertEquals(discard, items[4]);
    }

    @Test
    void testGrab2Ammo() {
        squares[0].refill(items[4]);
        squares[0].grab(f, items[4]);
        squares[0].refill(items[5]);
        squares[0].grab(f, items[5]);
        assertEquals(Stream.of(pup).collect(Collectors.toList()), f.getPowerUps());
        assertTrue(IntStream.range(0, 3).allMatch(i -> new AmmoCube(2, 2, 3).value(i) == f.getAmmo().value(i)));
        assertEquals(discard, items[5]);
    }

    @Test
    void testGrabNotAmmo() {
        squares[0].refill(items[4]);
        assertThrows(IllegalStateException.class, () -> squares[0].grab(f, items[5]));
        assertThrows(ClassCastException.class, () -> squares[0].grab(f, items[1]));
        assertEquals(Stream.of().collect(Collectors.toList()), f.getPowerUps());
        assertTrue(IntStream.range(0, 3).allMatch(i -> new AmmoCube().value(i) == f.getAmmo().value(i)));
    }
}
