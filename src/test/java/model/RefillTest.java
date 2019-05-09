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
        Arrays.stream(squares).forEach(s -> {
            Grabbable grabbed = (Grabbable) s.peek().toArray()[0];
            if (s instanceof AmmoSquare)
                assertTrue(Arrays.asList(ammoTiles).contains(grabbed));
            else if (s instanceof SpawnSquare)
                assertTrue(Arrays.asList(weapons).contains(grabbed));
        });
    }

    @Test
    void testRefillEmpty() {
        squares = new AbstractSquare[]{new SpawnSquareMock(new Room()), new SpawnSquareMock(new Room()),
                new SpawnSquareMock(new Room()), new SpawnSquareMock(new Room()),
                new SpawnSquareMock(new Room()), new SpawnSquareMock(new Room())};
        Arrays.stream(squares).forEach(s -> s.accept(g));
        assertTrue(Arrays.asList(weapons).contains(squares[0].peek().toArray()[0]));
        assertTrue(Arrays.asList(weapons).contains(squares[1].peek().toArray()[0]));
        assertTrue(Arrays.asList(weapons).contains(squares[2].peek().toArray()[0]));
        assertTrue(Arrays.asList(weapons).contains(squares[3].peek().toArray()[0]));
        assertEquals(Stream.of().collect(Collectors.toSet()), squares[4].peek());
        assertEquals(Stream.of().collect(Collectors.toSet()), squares[5].peek());
    }

    @Test
    void testRefillMany() {
        AbstractSquare square = new SpawnSquareMock(new Room());
        assertTrue(square.refill(weapons[2]));
        assertTrue(square.refill(weapons[1]));
        assertTrue(square.refill(weapons[3]));
        assertFalse(square.refill(weapons[0]));
        assertEquals(Stream.of(weapons[2], weapons[1], weapons[3]).collect(Collectors.toSet()), square.peek());
    }

    @Test
    void testWrong() {
        AbstractSquare square = new AmmoSquareMock(new Room());
        assertTrue(square.refill(ammoTiles[1]));
        assertFalse(square.refill(ammoTiles[0]));
        assertEquals(ammoTiles[1], square.peek().toArray()[0]);
    }
}
