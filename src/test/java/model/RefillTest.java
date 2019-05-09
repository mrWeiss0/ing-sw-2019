package model;

import model.board.*;
import model.weapon.OptionalWeaponMock;
import model.weapon.Weapon;
import model.weapon.WeaponMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SuspiciousMethodCalls")
class RefillTest {
    private Game g;
    private AbstractSquare[] squares;
    private Weapon[] weapons;
    private AmmoTile[] ammoTiles;

    @BeforeEach
    void init() {
        weapons = new Weapon[]{new WeaponMock(), new OptionalWeaponMock(), new WeaponMock(), new WeaponMock()};
        ammoTiles = new AmmoTile[]{new AmmoTile(), new AmmoTile()};
        g = new GameMock().setWeapons(Arrays.asList(weapons)).setAmmoTiles(Arrays.asList(ammoTiles));
    }

    @Test
    void testRefill() {
        squares = new AbstractSquare[]{new AmmoSquareMock(new Room()), new SpawnSquareMock(new Room()), new AmmoSquareMock(new Room()), new SpawnSquareMock(new Room())};
        Arrays.stream(squares).forEach(s -> s.accept(g));
        Arrays.stream(squares).forEach(s -> s.accept(g));
        Arrays.stream(squares).forEach(s -> {
            Grabbable grabbed = s.peek().iterator().next();
            if (s instanceof AmmoSquare)
                assertTrue(Arrays.asList(ammoTiles).contains(grabbed));
            else if (s instanceof SpawnSquare)
                assertTrue(Arrays.asList(weapons).contains(grabbed));
        });
    }

    @Test
    void testRefillEmpty() {
        squares = new AbstractSquare[]{
                new SpawnSquareMock(new Room(), 3),
                new SpawnSquareMock(new Room(), 3),
                new SpawnSquareMock(new Room(), 3)
        };
        Arrays.stream(squares).forEach(s -> s.accept(g));
        assertEquals(3, squares[0].peek().size());
        assertEquals(1, squares[1].peek().size());
        assertEquals(0, squares[2].peek().size());
    }

    @Test
    void testRefillMany() {
        AbstractSquare square = new SpawnSquareMock(new Room(), 3);
        square.refill(weapons[2]);
        square.refill(weapons[1]);
        square.refill(weapons[3]);
        assertThrows(IllegalStateException.class, () -> square.refill(weapons[0]));
        assertEquals(Stream.of(weapons[2], weapons[1], weapons[3]).collect(Collectors.toSet()), square.peek());
    }

    @Test
    void testWrongAmmo() {
        AbstractSquare square = new AmmoSquareMock(new Room());
        square.refill(ammoTiles[1]);
        assertThrows(IllegalStateException.class, () -> square.refill(ammoTiles[0]));
        assertThrows(ClassCastException.class, () -> square.refill(weapons[0]));
        assertEquals(ammoTiles[1], square.peek().iterator().next());
    }

    @Test
    void testWrongWeapon() {
        AbstractSquare square = new SpawnSquareMock(new Room(), 3);
        square.refill(weapons[1]);
        assertThrows(ClassCastException.class, () -> square.refill(ammoTiles[0]));
        assertEquals(weapons[1], square.peek().iterator().next());
    }
}
