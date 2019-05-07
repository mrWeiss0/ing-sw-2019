package tools;

import model.board.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {
    private static Set<Room> rooms;
    private static List<AbstractSquare> squares;

    @BeforeAll
    static void init() {
        Board board;
        try {
            /* TEST BOARD:
             * +-----------+
             * | 0 # 1   2s|
             * |   +-----#-+---+
             * | 3s| 4   5 # 6 |
             * +-#-+-#---#-+   |
             * | 7   8   9 #10s|
             * +-----------+---+
             */
            board = FileParser.buildBoard(new StringReader("[" +
                    "{\"id\":8,\"coords\":[2,1],\"roomId\":4,\"adjacent\":[4,7]}," +
                    "{\"id\":1,\"coords\":[0,1],\"roomId\":1,\"adjacent\":[0,2]}," +
                    "{\"id\":6,\"coords\":[1,3],\"roomId\":5,\"adjacent\":[5,10]}," +
                    "{\"id\":9,\"coords\":[2,2],\"roomId\":4,\"adjacent\":[8,5]}," +
                    "{\"id\":4,\"coords\":[1,1],\"roomId\":2,\"adjacent\":[5]}," +
                    "{\"id\":0,\"coords\":[0,0],\"roomId\":0}," +
                    "{\"id\":5,\"coords\":[1,2],\"roomId\":2}," +
                    "{\"id\":7,\"coords\":[2,0],\"roomId\":4,\"adjacent\":[3]}," +
                    "{\"id\":2,\"coords\":[0,2],\"roomId\":1,\"adjacent\":[5,1],\"spawn\":true}," +
                    "{\"id\":3,\"coords\":[1,0],\"roomId\":0,\"adjacent\":[0],\"spawn\":true}," +
                    "{\"id\":10,\"coords\":[2,3],\"roomId\":5,\"adjacent\":[9,6],\"spawn\":true}]"));
        } catch (MalformedDataException e) {
            fail(e);
            return;
        }
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
        assertThrows(MalformedDataException.class, () -> FileParser.buildBoard(new StringReader("[{\"roomId\":0,\"adjacent\":[0],\"spawn\":true}]")));
        assertThrows(MalformedDataException.class, () -> FileParser.buildBoard(new StringReader("[{\"coords\":[1]}]")));
        assertThrows(MalformedDataException.class, () -> FileParser.buildBoard(new StringReader("[{\"coords\":[1,2,3]}]")));
        assertThrows(MalformedDataException.class, () -> FileParser.buildBoard(new StringReader("[{\"id\":1,\"coords\":[0,1]},{\"id\":1,\"coords\":[0,1]}]")));
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