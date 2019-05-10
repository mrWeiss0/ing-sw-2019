package model;

import model.board.*;
import model.weapon.OptionalWeapon;
import model.weapon.Weapon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SuspiciousMethodCalls")
class RefillTest {
    private static Weapon[] weapons;
    private static AmmoTile[] ammoTiles;

    @BeforeAll
    static void init() {
        Weapon.Builder weapon = new Weapon.Builder();
        weapons = new Weapon[]{weapon.build(), new OptionalWeapon.Builder().build(), weapon.build(), weapon.build()};
        ammoTiles = new AmmoTile[]{new AmmoTile(), new AmmoTile(), new AmmoTile()};
    }

    @Test
    void testRefill() {
        Game g = new Game.Builder()
                .weapons(Arrays.asList(weapons))
                .ammoTiles(Arrays.asList(ammoTiles))
                .squares(
                        new SquareImage().coords(0, 0),
                        new SquareImage().coords(0, 0).spawn(),
                        new SquareImage().coords(0, 0),
                        new SquareImage().coords(0, 0).spawn()
                )
                .build();
        Set<AbstractSquare> squares = g.getBoard().getSquares();
        g.fillBoard();
        g.fillBoard();
        squares.forEach(s -> {
            Grabbable grabbed = s.peek().iterator().next();
            if (s instanceof AmmoSquare)
                assertTrue(Arrays.asList(ammoTiles).contains(grabbed));
            else if (s instanceof SpawnSquare)
                assertTrue(Arrays.asList(weapons).contains(grabbed));
        });
    }

    @Test
    void testRefillEmpty() {
        Game g = new Game.Builder()
                .weapons(Arrays.asList(weapons))
                .squares(
                        new SquareImage().coords(0, 0).spawn(),
                        new SquareImage().coords(1, 0).spawn(),
                        new SquareImage().coords(2, 0).spawn()
                )
                .spawnCapacity(3)
                .build();
        List<AbstractSquare> squares = new ArrayList<>(g.getBoard().getSquares());
        squares.forEach(s -> s.accept(g));
        assertEquals(3, squares.get(0).peek().size());
        assertEquals(1, squares.get(1).peek().size());
        assertEquals(0, squares.get(2).peek().size());
    }

    @Test
    void testRefillMany() {
        AbstractSquare square = new SpawnSquare(new Room(), new int[]{}, 3);
        square.refill(weapons[2]);
        square.refill(weapons[1]);
        square.refill(weapons[3]);
        assertThrows(IllegalStateException.class, () -> square.refill(weapons[0]));
        assertEquals(Stream.of(weapons[2], weapons[1], weapons[3]).collect(Collectors.toSet()), square.peek());
    }

    @Test
    void testWrongAmmo() {
        AbstractSquare square = new AmmoSquare(new Room(), new int[]{});
        square.refill(ammoTiles[1]);
        assertThrows(IllegalStateException.class, () -> square.refill(ammoTiles[0]));
        assertThrows(ClassCastException.class, () -> square.refill(weapons[0]));
        assertEquals(ammoTiles[1], square.peek().iterator().next());
    }

    @Test
    void testWrongWeapon() {
        AbstractSquare square = new SpawnSquare(new Room(), new int[]{}, 3);
        square.refill(weapons[1]);
        assertThrows(ClassCastException.class, () -> square.refill(ammoTiles[0]));
        assertEquals(weapons[1], square.peek().iterator().next());
    }
}
