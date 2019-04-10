package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefillTest {
    private Game g;
    private AbstractSquare[] squares;
    private Weapon[] weapons;
    private AmmoTile[] ammoTiles;

    @BeforeEach
    void init() {
        weapons = new Weapon[]{new Weapon(), new Weapon(), new Weapon(), new Weapon()};
        ammoTiles = new AmmoTile[]{new AmmoTile(), new AmmoTile()};
        g = new Game(Arrays.asList(weapons), Arrays.asList(ammoTiles));
        squares = new AbstractSquare[]{new AmmoSquare(new Room()), new SpawnSquare(new Room()), new AmmoSquare(new Room()), new SpawnSquare(new Room())};
    }

    @Test
    void testRefill() {
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
}
