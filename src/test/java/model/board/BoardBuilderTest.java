package model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;

public class BoardBuilderTest {
    /* TEST BOARD:
     * +-----------+
     * | 0 # 1   2s|
     * |   +-----#-+---+
     * | 3s| 4   5 # 6 |
     * +-#-+-#---#-+   |
     * | 7   8   9 #10s|
     * +-----------+---+
     */
    public static SquareImage[] squareImages = new SquareImage[]{
            new SquareImage().id(8).coords(new int[]{2, 1}).roomId(4).adjacent(new int[]{4, 7}),
            new SquareImage().id(1).coords(new int[]{0, 1}).roomId(1).adjacent(new int[]{0, 2}),
            new SquareImage().id(6).coords(new int[]{1, 3}).roomId(5).adjacent(new int[]{5, 10}),
            new SquareImage().id(9).coords(new int[]{2, 2}).roomId(4).adjacent(new int[]{8, 5}),
            new SquareImage().id(4).coords(new int[]{1, 1}).roomId(2).adjacent(new int[]{5}),
            new SquareImage().id(0).coords(new int[]{0, 0}).roomId(0),
            new SquareImage().id(5).coords(new int[]{1, 2}).roomId(2),
            new SquareImage().id(7).coords(new int[]{2, 0}).roomId(4).adjacent(new int[]{3}),
            new SquareImage().id(2).coords(new int[]{0, 2}).roomId(1).adjacent(new int[]{5, 1}).spawn(true),
            new SquareImage().id(3).coords(new int[]{1, 0}).roomId(0).adjacent(new int[]{0}).spawn(true),
            new SquareImage().id(10).coords(new int[]{2, 3}).roomId(5).adjacent(new int[]{9, 6}).spawn(true)
    };
    private static Set<Room> rooms;
    private static List<AbstractSquare> squares;

    @BeforeAll
    static void init() {
        Board board = new Board.Builder().squares(squareImages).build();
        rooms = board.getRooms();
        squares = new ArrayList<>(board.getSquares());
        squares.sort((a, b) -> {
            int[] ca = a.getCoordinates();
            int[] cb = b.getCoordinates();
            for (int i = 0; i < min(ca.length, cb.length); ++i) {
                int d = Integer.compare(ca[i], cb[i]);
                if (d != 0) return d;
            }
            return Integer.compare(ca.length, cb.length);
        });
    }

    @Test
    void testExceptions() {
        assertThrows(IllegalArgumentException.class, () ->
                new Board.Builder().squares(new SquareImage().roomId(0).adjacent(new int[]{0}).spawn(true)).build());
        assertThrows(IllegalArgumentException.class, () ->
                new Board.Builder().squares(new SquareImage().coords(new int[]{1})).build());
        assertThrows(IllegalArgumentException.class, () ->
                new Board.Builder().squares(new SquareImage().coords(new int[]{1, 2, 3})).build());
        assertThrows(IllegalArgumentException.class, () ->
                new Board.Builder().squares(new SquareImage().id(1).coords(new int[]{0, 1}), new SquareImage().id(1).coords(new int[]{0, 2})).build());
    }

    @Test
    void testSize() {
        assertEquals(11, squares.size());
        assertEquals(5, rooms.size());
    }

    @Test
    void testRooms() {
        assertSame(squares.get(0).getRoom(), squares.get(3).getRoom());
        assertSame(squares.get(1).getRoom(), squares.get(2).getRoom());
        assertSame(squares.get(4).getRoom(), squares.get(5).getRoom());
        assertSame(squares.get(6).getRoom(), squares.get(10).getRoom());
        assertSame(squares.get(7).getRoom(), squares.get(8).getRoom());
        assertSame(squares.get(7).getRoom(), squares.get(9).getRoom());
        assertNotSame(squares.get(0).getRoom(), squares.get(1).getRoom());
        assertNotSame(squares.get(0).getRoom(), squares.get(4).getRoom());
        assertNotSame(squares.get(0).getRoom(), squares.get(6).getRoom());
        assertNotSame(squares.get(0).getRoom(), squares.get(7).getRoom());
        assertNotSame(squares.get(1).getRoom(), squares.get(4).getRoom());
        assertNotSame(squares.get(1).getRoom(), squares.get(6).getRoom());
        assertNotSame(squares.get(1).getRoom(), squares.get(7).getRoom());
        assertNotSame(squares.get(4).getRoom(), squares.get(6).getRoom());
        assertNotSame(squares.get(4).getRoom(), squares.get(7).getRoom());
        assertNotSame(squares.get(6).getRoom(), squares.get(7).getRoom());
    }

    @Test
    void testConnections() {
        assertEquals(Stream.of(squares.get(1), squares.get(3)).collect(Collectors.toSet()), squares.get(0).getAdjacent());
        assertEquals(Stream.of(squares.get(0), squares.get(2)).collect(Collectors.toSet()), squares.get(1).getAdjacent());
        assertEquals(Stream.of(squares.get(1), squares.get(5)).collect(Collectors.toSet()), squares.get(2).getAdjacent());
        assertEquals(Stream.of(squares.get(0), squares.get(7)).collect(Collectors.toSet()), squares.get(3).getAdjacent());
        assertEquals(Stream.of(squares.get(5), squares.get(8)).collect(Collectors.toSet()), squares.get(4).getAdjacent());
        assertEquals(Stream.of(squares.get(2), squares.get(6), squares.get(9), squares.get(4)).collect(Collectors.toSet()), squares.get(5).getAdjacent());
        assertEquals(Stream.of(squares.get(5), squares.get(10)).collect(Collectors.toSet()), squares.get(6).getAdjacent());
        assertEquals(Stream.of(squares.get(3), squares.get(8)).collect(Collectors.toSet()), squares.get(7).getAdjacent());
        assertEquals(Stream.of(squares.get(7), squares.get(9), squares.get(4)).collect(Collectors.toSet()), squares.get(8).getAdjacent());
        assertEquals(Stream.of(squares.get(5), squares.get(8), squares.get(10)).collect(Collectors.toSet()), squares.get(9).getAdjacent());
        assertEquals(Stream.of(squares.get(6), squares.get(9)).collect(Collectors.toSet()), squares.get(10).getAdjacent());
    }

    @Test
    void testSpawnpoint() {
        assertEquals(SpawnSquare.class, squares.get(2).getClass());
        assertEquals(SpawnSquare.class, squares.get(3).getClass());
        assertEquals(SpawnSquare.class, squares.get(10).getClass());
        assertEquals(AmmoSquare.class, squares.get(0).getClass());
        assertEquals(AmmoSquare.class, squares.get(1).getClass());
        assertEquals(AmmoSquare.class, squares.get(4).getClass());
        assertEquals(AmmoSquare.class, squares.get(5).getClass());
        assertEquals(AmmoSquare.class, squares.get(6).getClass());
        assertEquals(AmmoSquare.class, squares.get(7).getClass());
        assertEquals(AmmoSquare.class, squares.get(8).getClass());
        assertEquals(AmmoSquare.class, squares.get(9).getClass());
    }
}