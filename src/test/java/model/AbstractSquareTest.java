package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractSquareTest {

    public static Room room;

    @BeforeAll
    public static void init() {
        room = new Room();
    }

    @Test
    void testGetRoom() {
        AbstractSquare square = new SpawnSquare(AbstractSquareTest.room, new int[]{0,0});
        assertEquals(square.getRoom(), AbstractSquareTest.room);
    }

    @Test
    void TestConnect() {
        AbstractSquare square1 = new SpawnSquare(AbstractSquareTest.room, new int[]{0,0});
        AbstractSquare square2 = new SpawnSquare(AbstractSquareTest.room, new int[]{0,0});
        square1.connect(square2);
        assertTrue(square1.getAdjacent().contains(square2));
        assertTrue(square2.getAdjacent().contains(square1));
        assertFalse(square1.getAdjacent().contains(square1));
        assertFalse(square2.getAdjacent().contains(square2));
        assertEquals(1, square1.getAdjacent().size());
        assertEquals(1, square2.getAdjacent().size());
    }

    @Test
    void TestConnectDup() {
        AbstractSquare square1 = new SpawnSquare(AbstractSquareTest.room, new int[]{0,0});
        AbstractSquare square2 = new SpawnSquare(AbstractSquareTest.room, new int[]{0,0});
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