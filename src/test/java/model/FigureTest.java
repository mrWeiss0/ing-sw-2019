package model;

import model.mock.MockFigure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {
    public static Room room;
    public static AbstractSquare[] squares;
    private Figure figure;

    @BeforeAll
    static void init() {
        room = new Room();
        squares = new AbstractSquare[]{
                new SpawnSquare(room, new int[]{0,0}),
                new SpawnSquare(room, new int[]{0,0})
        };
    }

    @BeforeEach
    void initCase() {
        figure = new MockFigure();
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


}