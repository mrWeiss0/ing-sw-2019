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
        AbstractSquare square = new SquareSpawn(AbstractSquareTest.room);
        assertEquals(square.getRoom(), AbstractSquareTest.room);
    }

    @Test
    void TestConnect() {
        AbstractSquare square1 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare square2 = new SquareSpawn(AbstractSquareTest.room);
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
        AbstractSquare square1 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare square2 = new SquareSpawn(AbstractSquareTest.room);
        square1.connect(square2);
        square2.connect(square1);
        assertTrue(square1.getAdjacent().contains(square2));
        assertTrue(square2.getAdjacent().contains(square1));
        assertFalse(square1.getAdjacent().contains(square1));
        assertFalse(square2.getAdjacent().contains(square2));
        assertEquals(1, square1.getAdjacent().size());
        assertEquals(1, square2.getAdjacent().size());
    }

    @Test
    void TestDistance() {
        AbstractSquare sq1 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq2 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq3 = new SquareSpawn(AbstractSquareTest.room);
        sq1.connect(sq2);
        sq2.connect(sq1);
        sq2.connect(sq3);
        sq3.connect(sq2);
        assertEquals(1, sq1.distance(sq2));
        assertEquals(0, sq1.distance(sq1));
        assertEquals(2, sq1.distance(sq3));
    }

    @Test
    void TestDistanceRamified() {
        AbstractSquare sq1 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq2 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq3 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq4 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq5 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq6 = new SquareSpawn(AbstractSquareTest.room);
        sq1.connect(sq2);
        sq2.connect(sq3);
        sq3.connect(sq4);
        sq4.connect(sq6);
        sq3.connect(sq5);
        sq5.connect(sq6);
        assertEquals(1, sq1.distance(sq2));
        assertEquals(0, sq1.distance(sq1));
        assertEquals(2, sq1.distance(sq3));
        assertEquals(4, sq1.distance(sq6));
    }

    @Test
    void TestDistanceMinPath() {
        AbstractSquare sq1 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq2 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq3 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq4 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq5 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq6 = new SquareSpawn(AbstractSquareTest.room);
        sq1.connect(sq2);
        sq2.connect(sq3);
        sq2.connect(sq4);
        sq4.connect(sq5);
        sq3.connect(sq6);
        sq5.connect(sq6);
        assertEquals(1, sq1.distance(sq2));
        assertEquals(0, sq1.distance(sq1));
        assertEquals(2, sq1.distance(sq3));
        assertEquals(3, sq1.distance(sq6));
        assertNotEquals(4, sq1.distance(sq6));
    }

    @Test
    void TestDistanceNotConnected(){
        AbstractSquare sq1 = new SquareSpawn(AbstractSquareTest.room);
        AbstractSquare sq2 = new SquareSpawn(AbstractSquareTest.room);
        assertEquals(-1,sq1.distance(sq2));
    }

}