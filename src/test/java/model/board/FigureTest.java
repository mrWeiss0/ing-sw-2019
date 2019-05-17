package model.board;

import model.AmmoCube;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {
    private static AbstractSquare[] squares;
    private Figure figure;

    @BeforeAll
    static void init() {
        Room room = new Room();
        squares = new AbstractSquare[]{
                new SpawnSquare(room, new int[]{}, 1),
                new SpawnSquare(room, new int[]{}, 1)
        };
    }

    @BeforeEach
    void initCase() {
        figure = new Figure(12, 3, 3, 3, 3);
    }

    @Test
    void testMove() {
        figure.moveTo(squares[0]);
        assertEquals(squares[0], figure.getSquare());
        figure.moveTo(squares[1]);
        assertEquals(squares[1], figure.getSquare());
        assertNotEquals(squares[0], figure.getSquare());
        figure.moveTo(null);
        assertNull(figure.getSquare());
    }

    @Test
    void testContains() {
        assertFalse(squares[0].getOccupants().contains(figure));
        figure.moveTo(squares[0]);
        assertTrue(squares[0].getOccupants().contains(figure));
        assertFalse(squares[1].getOccupants().contains(figure));
        figure.moveTo(squares[1]);
        assertTrue(squares[1].getOccupants().contains(figure));
        assertFalse(squares[0].getOccupants().contains(figure));
        figure.moveTo(null);
        assertFalse(squares[0].getOccupants().contains(figure));
        assertFalse(squares[1].getOccupants().contains(figure));
    }

    @Test
    void testAddAmmo() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(1, 1, 1);
        AmmoCube c = a.add(b).cap(3);
        figure.addAmmo(a);
        figure.addAmmo(b);
        assertTrue(IntStream.range(0, 3).allMatch(i -> c.value(i) == figure.getAmmo().value(i)));
    }

    @Test
    void testSubAmmo() {
        AmmoCube a = new AmmoCube(1, 2, 3);
        AmmoCube b = new AmmoCube(1, 1, 1);
        AmmoCube c = new AmmoCube(1, 3, 3);
        AmmoCube d = a.sub(b);
        figure.addAmmo(a);
        assertThrows(IllegalStateException.class, () -> figure.subAmmo(c));
        figure.subAmmo(b);
        assertTrue(IntStream.range(0, 3).allMatch(i -> d.value(i) == figure.getAmmo().value(i)));
    }
}
