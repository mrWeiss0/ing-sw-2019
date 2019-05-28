package model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SquareVisibleTest {

    private static Room[] rooms;
    private static AbstractSquare[] squares;
    private static Figure figure;

    @BeforeAll
    static void init() {
        rooms = new Room[]{
                new Room(),
                new Room(),
                new Room()
        };
        squares = new AbstractSquare[]{
                new SpawnSquare(rooms[0], new int[]{}, 1),
                new SpawnSquare(rooms[0], new int[]{}, 1),
                new SpawnSquare(rooms[1], new int[]{}, 1),
                new SpawnSquare(rooms[2], new int[]{}, 1)
        };
        squares[0].connect(squares[1]);
        squares[1].connect(squares[2]);
        squares[2].connect(squares[3]);
        figure = new Figure(10, 12, 3, 3, 3, 3);
        figure.moveTo(squares[2]);
    }

    @Test
    void testSeesSameRoom() {
        assertTrue(squares[0].visibleRooms().contains(rooms[0]));
    }

    @Test
    void testSeesSelf() {
        assertTrue(squares[0].visibleSquares().contains(squares[0]));
    }

    @Test
    void testSeesSameRoomSquare() {
        assertTrue(squares[0].visibleSquares().contains(squares[1]));
    }

    @Test
    void testSeesConnectedRoom() {
        assertTrue(squares[2].visibleRooms().containsAll(Arrays.asList(rooms[0], rooms[1])));
    }

    @Test
    void testSeesConnectedRoomSquares() {
        assertTrue(squares[2].visibleSquares().containsAll(Arrays.asList(squares[0], squares[1], squares[2], squares[3])));
    }

    @Test
    void testNotSeesNotConnectedRoom() {
        assertFalse(squares[0].visibleRooms().contains(rooms[1]));
        assertFalse(squares[0].visibleRooms().contains(rooms[2]));
        assertFalse(squares[3].visibleRooms().contains(rooms[0]));
    }

    @Test
    void testNotSeesNotConnectedRoomSquares() {
        assertFalse(squares[0].visibleSquares().contains(squares[2]));
        assertFalse(squares[1].visibleSquares().contains(squares[3]));
        assertFalse(squares[3].visibleSquares().contains(squares[1]));
        assertFalse(squares[0].visibleSquares().contains(squares[3]));
    }

    @Test
    void testSeesFigure() {
        assertFalse(squares[0].visibleFigures().contains(figure));
        assertTrue(squares[1].visibleFigures().contains(figure));
        assertTrue(squares[2].visibleFigures().contains(figure));
        assertTrue(squares[3].visibleFigures().contains(figure));
    }
}
