package model;

import model.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GrabTest {
    private Figure f;
    private AbstractSquare[] squares;
    private Set<Grabbable> grabbed;
    private Grabbable[] items;

    @BeforeEach
    void init() {
        f = new Figure(12, 3);
        squares = new AbstractSquare[]{new AmmoSquare(new Room(), new int[]{0,0}), new SpawnSquare(new Room(), new int[]{0,0})};
        items = new Grabbable[]{new Weapon(), new Weapon(), new Weapon(), new Weapon(), new AmmoTile(), new AmmoTile()};
    }

    @Test
    void emptyTest() {
        grabbed = squares[0].peek();
        assertEquals(new HashSet<>(), grabbed);
    }

    @Test
    void ammoTest() {
        squares[0].refill(items[4]);
        assertEquals(Stream.of(items[4]).collect(Collectors.toSet()), squares[0].peek());
    }

    @Test
    void ammoNotTest() {
        squares[0].refill(items[4]);
        assertNotEquals(Stream.of(new AmmoTile()).collect(Collectors.toSet()), squares[0].peek());
    }

    @Test
    void spawnTest() {
        squares[1].refill(items[0]);
        squares[1].refill(items[1]);
        assertEquals(Stream.of(items[1], items[0]).collect(Collectors.toSet()), squares[1].peek());
    }

    @Test
    void ammoExceptionTest() {
        assertThrows(ClassCastException.class, () -> squares[1].refill(items[4]));
    }

    @Test
    void spawnExceptionTest() {
        assertThrows(ClassCastException.class, () -> squares[0].refill(items[0]));
    }

    @Test
    void grabWeaponTest() {
        squares[1].refill(items[0]);
        squares[1].grab(f, items[0]);
        assertEquals(Stream.of(items[0]).collect(Collectors.toSet()), f.getWeapons());
    }

    @Test
    void grab2WeaponTest() {
        squares[1].refill(items[0]);
        squares[1].refill(items[1]);
        squares[1].grab(f, items[1]);
        squares[1].grab(f, items[0]);
        assertEquals(Stream.of(items[0], items[1]).collect(Collectors.toSet()), f.getWeapons());
    }

    @Test
    void grabNotWeaponTest() {
        squares[1].refill(items[0]);
        squares[1].grab(f, items[1]);
        assertEquals(Stream.of().collect(Collectors.toSet()), f.getWeapons());
    }
}