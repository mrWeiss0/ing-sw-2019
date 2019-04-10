package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GrabTest {
    private Figure f;
    private AbstractSquare[] squares;
    private Set<Grabbable> grabbed;
    private Grabbable[] items;

    @BeforeEach
    public void init() {
        f = new Figure();
        squares = new AbstractSquare[]{new AmmoSquare(new Room()), new SpawnSquare(new Room())};
        items = new Grabbable[]{new Weapon(), new Weapon(), new Weapon(), new Weapon(), new AmmoTile(), new AmmoTile()};
    }

    @Test
    public void emptyTest() {
        grabbed = squares[0].peek();
        assertEquals(new HashSet<>(), grabbed);
    }

    @Test
    public void ammoTest() {
        squares[0].refill(items[4]);
        assertEquals(Stream.of(items[4]).collect(Collectors.toSet()), squares[0].peek());
    }

    @Test
    public void spawnTest() {
        squares[1].refill(items[0]);
        squares[1].refill(items[1]);
        assertEquals(Stream.of(items[1], items[0]).collect(Collectors.toSet()), squares[1].peek());
    }

    @Test
    public void ammoExceptionTest() {
        assertThrows(ClassCastException.class, () -> {
            squares[1].refill(items[4]);
        });
    }

    @Test
    public void spawnExceptionTest() {
        assertThrows(ClassCastException.class, () -> {
            squares[0].refill(items[0]);
        });
    }
}