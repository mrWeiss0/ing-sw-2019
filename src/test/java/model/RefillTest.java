package model;

import model.mock.MockAmmoSquare;
import model.mock.MockOptionalWeapon;
import model.mock.MockSpawnSquare;
import model.mock.MockWeapon;
import model.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefillTest {
    private Game g;
    private AbstractSquare[] squares;
    private Weapon[] weapons;
    private AmmoTile[] ammoTiles;

    @BeforeEach
    void init() {
        weapons = new Weapon[]{new MockWeapon(), new MockOptionalWeapon(), new MockWeapon(), new MockWeapon()};
        ammoTiles = new AmmoTile[]{new AmmoTile(), new AmmoTile()};
        g = new Game(Arrays.asList(weapons), Arrays.asList(ammoTiles));
    }

    @Test
    void testRefill() {
        squares = new AbstractSquare[]{new MockAmmoSquare(new Room()), new MockSpawnSquare(new Room()), new MockAmmoSquare(new Room()), new MockSpawnSquare(new Room())};
        Arrays.stream(squares).forEach(s -> s.accept(g));
        Arrays.stream(squares).forEach(s -> {
            Grabbable grabbed = (Grabbable) s.peek().toArray()[0];
            if (s instanceof AmmoSquare) {
                assertTrue(Arrays.asList(ammoTiles).contains(grabbed));
            } else if (s instanceof SpawnSquare) {
                assertTrue(Arrays.asList(weapons).contains(grabbed));
            }
        });
    }

    @Test
    void testRefillEmpty() {
        squares = new AbstractSquare[]{new MockSpawnSquare(new Room()), new MockSpawnSquare(new Room()),
                new MockSpawnSquare(new Room()), new MockSpawnSquare(new Room()),
                new MockSpawnSquare(new Room()), new MockSpawnSquare(new Room())};
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
        AbstractSquare square = new MockSpawnSquare(new Room());
        square.accept(g);
        square.accept(g);
        square.accept(g);
        square.accept(g);
        assertEquals(Arrays.stream(weapons).collect(Collectors.toSet()), square.peek());
    }
}
