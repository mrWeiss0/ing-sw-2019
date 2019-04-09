package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AbstractSquareSeesTest {

    public static Room[] rooms;
    public static AbstractSquare[] squares;
    public static Figure figure;

    @BeforeAll
    public static void init() {
        rooms = new Room[]{
                new Room(),
                new Room(),
                new Room()
        };
        squares = new AbstractSquare[]{
                new SquareSpawn(rooms[0]),
                new SquareSpawn(rooms[0]),
                new SquareSpawn(rooms[1]),
                new SquareSpawn(rooms[2])
        };
        squares[0].connect(squares[1]);
        squares[1].connect(squares[2]);
        squares[2].connect(squares[3]);
        figure = new Figure();
        figure.moveTo(squares[2]);
    }

    @Test
    void testSeesSameRoom() {
        assertTrue(squares[0].sees(rooms[0]));
    }

    @Test
    void testSeesSelf() {
        assertTrue(squares[0].sees(squares[0]));
    }

    @Test
    void testSeesSameRoomSquare() {
        assertTrue(squares[0].sees(squares[1]));
    }

    @Test
    void testSeesConnectedRoom() {
        assertTrue(squares[2].sees(rooms[0]));
        assertTrue(squares[2].sees(rooms[1]));
    }

    @Test
    void testSeesConnectedRoomSquares() {
        assertTrue(squares[2].sees(squares[0]));
        assertTrue(squares[2].sees(squares[1]));
        assertTrue(squares[2].sees(squares[3]));
        assertTrue(squares[3].sees(squares[2]));
    }

    @Test
    void testNotSeesNotConnectedRoom() {
        assertFalse(squares[0].sees(rooms[1]));
        assertFalse(squares[0].sees(rooms[2]));
        assertFalse(squares[3].sees(rooms[0]));
    }

    @Test
    void testNotSeesNotConnectedRoomSquares() {
        assertFalse(squares[0].sees(squares[2]));
        assertFalse(squares[1].sees(squares[3]));
        assertFalse(squares[3].sees(squares[1]));
        assertFalse(squares[0].sees(squares[3]));
    }

    @Test
    void testSeesFigure(){
        assertFalse(squares[0].sees(figure));
        assertTrue(squares[1].sees(figure));
        assertTrue(squares[2].sees(figure));
        assertTrue(squares[3].sees(figure));
    }
}
