package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SquareDistanceTest {

    public static Room room;

    @BeforeAll
    public static void init() {
        room = new Room();
    }

    @Test
    void TestDistance() {
        AbstractSquare sq1 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq2 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq3 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
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
        AbstractSquare sq1 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq2 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq3 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq4 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq5 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq6 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
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
        AbstractSquare sq1 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq2 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq3 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq4 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq5 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq6 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
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
    void TestDistanceNotConnected() {
        AbstractSquare sq1 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        AbstractSquare sq2 = new SpawnSquare(SquareDistanceTest.room, new int[]{0,0});
        assertEquals(-1, sq1.distance(sq2));
    }
}