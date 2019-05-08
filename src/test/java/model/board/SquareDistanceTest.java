package model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SquareDistanceTest {
    private static Room room;

    @BeforeAll
    static void init() {
        room = new Room();
    }

    @Test
    void TestDistance() {
        AbstractSquare sq1 = new SpawnSquareMock(room);
        AbstractSquare sq2 = new SpawnSquareMock(room);
        AbstractSquare sq3 = new SpawnSquareMock(room);
        sq1.connect(sq2);
        sq3.connect(sq2);
        assertEquals(Stream.of(sq1, sq2).collect(Collectors.toSet()), sq1.atDistanceMax(1));
        assertEquals(Stream.of(sq1).collect(Collectors.toSet()), sq1.atDistanceMax(0));
        assertEquals(Stream.of(sq1, sq2, sq3).collect(Collectors.toSet()), sq1.atDistanceMax(2));
    }

    @Test
    void TestDistanceRamified() {
        AbstractSquare sq1 = new SpawnSquareMock(room);
        AbstractSquare sq2 = new SpawnSquareMock(room);
        AbstractSquare sq3 = new SpawnSquareMock(room);
        AbstractSquare sq4 = new SpawnSquareMock(room);
        AbstractSquare sq5 = new SpawnSquareMock(room);
        AbstractSquare sq6 = new SpawnSquareMock(room);
        sq1.connect(sq2);
        sq2.connect(sq3);
        sq3.connect(sq4);
        sq4.connect(sq6);
        sq3.connect(sq5);
        sq5.connect(sq6);
        assertEquals(Stream.of(sq1, sq2).collect(Collectors.toSet()), sq1.atDistanceMax(1));
        assertEquals(Stream.of(sq1, sq2, sq3).collect(Collectors.toSet()), sq2.atDistanceMax(1));
        assertEquals(Stream.of(sq1, sq2, sq3).collect(Collectors.toSet()), sq1.atDistanceMax(2));
        assertEquals(Stream.of(sq5, sq6, sq4, sq2, sq3).collect(Collectors.toSet()), sq5.atDistanceMax(2));
        assertEquals(Stream.of(sq5, sq4, sq2, sq3).collect(Collectors.toSet()), sq3.atDistanceMax(1));
    }

    @Test
    void TestDistanceMinPath() {
        AbstractSquare sq1 = new SpawnSquareMock(room);
        AbstractSquare sq2 = new SpawnSquareMock(room);
        AbstractSquare sq3 = new SpawnSquareMock(room);
        AbstractSquare sq4 = new SpawnSquareMock(room);
        AbstractSquare sq5 = new SpawnSquareMock(room);
        AbstractSquare sq6 = new SpawnSquareMock(room);
        sq1.connect(sq2);
        sq2.connect(sq3);
        sq2.connect(sq4);
        sq4.connect(sq5);
        sq3.connect(sq6);
        sq5.connect(sq6);
        assertEquals(Stream.of(sq1, sq2, sq3, sq4).collect(Collectors.toSet()), sq1.atDistanceMax(2));
        assertEquals(Stream.of(sq3, sq2, sq6).collect(Collectors.toSet()), sq3.atDistanceMax(1));
        assertEquals(Stream.of(sq1, sq2, sq3, sq4, sq5, sq6).collect(Collectors.toSet()), sq3.atDistanceMax(5));
    }
}