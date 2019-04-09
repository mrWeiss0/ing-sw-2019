package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.Transient;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {
    public static Room room;
    public static AbstractSquare[] squares;
    private Figure figure;

    @BeforeAll
    static void init() {
        room = new Room();
        squares = new AbstractSquare[]{
                new SquareSpawn(room),
                new SquareSpawn(room)
        };
    }
    @BeforeEach
    void initCase(){
        figure= new Figure();
    }
    @Test
    void testMove(){
        figure.moveTo(squares[0]);
        assertEquals(squares[0],figure.getSquare());
        figure.moveTo(squares[1]);
        assertEquals(squares[1],figure.getSquare());
        assertNotEquals(squares[0],figure.getSquare());
        figure.moveTo(null);
        assertNull(figure.getSquare());
    }
    @Test
    void testContains(){
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