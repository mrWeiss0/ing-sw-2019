package model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    private static Room room;

    @BeforeAll
    static void init() {
        room = new Room();
    }

    @Test
    void testGetRoom() {
        AbstractSquare square = new SpawnSquare(SquareTest.room, new int[]{}, 1);
        assertEquals(square.getRoom(), SquareTest.room);
    }

    @Test
    void testConnect() {
        AbstractSquare square1 = new SpawnSquare(SquareTest.room, new int[]{}, 1);
        AbstractSquare square2 = new SpawnSquare(SquareTest.room, new int[]{}, 1);
        square1.connect(square2);
        assertTrue(square1.getAdjacent().contains(square2));
        assertTrue(square2.getAdjacent().contains(square1));
        assertFalse(square1.getAdjacent().contains(square1));
        assertFalse(square2.getAdjacent().contains(square2));
        assertEquals(1, square1.getAdjacent().size());
        assertEquals(1, square2.getAdjacent().size());
    }

    @Test
    void testConnectDup() {
        AbstractSquare square1 = new SpawnSquare(SquareTest.room, new int[]{}, 1);
        AbstractSquare square2 = new SpawnSquare(SquareTest.room, new int[]{}, 1);
        square1.connect(square2);
        square2.connect(square1);
        assertTrue(square1.getAdjacent().contains(square2));
        assertTrue(square2.getAdjacent().contains(square1));
        assertFalse(square1.getAdjacent().contains(square1));
        assertFalse(square2.getAdjacent().contains(square2));
        assertEquals(1, square1.getAdjacent().size());
        assertEquals(1, square2.getAdjacent().size());
    }

}
