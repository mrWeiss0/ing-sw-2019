package model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SquareDistanceTest {
    private static AbstractSquare[] sq;

    @BeforeAll
    static void init() {
        Room room = new Room();
        sq = new AbstractSquare[]{
                new SpawnSquare(room, new int[]{}, 1), new SpawnSquare(room, new int[]{}, 1), new SpawnSquare(room, new int[]{}, 1), new SpawnSquare(room, new int[]{}, 1),
                new SpawnSquare(room, new int[]{}, 1), new SpawnSquare(room, new int[]{}, 1), new SpawnSquare(room, new int[]{}, 1), new SpawnSquare(room, new int[]{}, 1)
        };
        sq[0].connect(sq[1]);
        sq[1].connect(sq[2]);
        sq[2].connect(sq[5]);
        sq[1].connect(sq[5]);
        sq[5].connect(sq[4]);
        sq[4].connect(sq[3]);
        sq[3].connect(sq[1]);
        sq[5].connect(sq[6]);
        sq[5].connect(sq[7]);
    }

    @Test
    void testDistanceZero() {
        for (AbstractSquare s : sq)
            assertEquals(Stream.of(s).collect(Collectors.toSet()), s.atDistance(0));
    }

    @Test
    void testDistanceInf() {
        for (AbstractSquare s : sq)
            assertEquals(Arrays.stream(sq).collect(Collectors.toSet()), s.atDistance(-1));
    }

    @Test
    void testDistanceInfOne() {
        for (AbstractSquare s : sq)
            assertEquals(Arrays.stream(sq).filter(t -> t != s).collect(Collectors.toSet()), s.atDistance(1, -1));
    }

    @Test
    void testDistanceOne() {
        for (AbstractSquare s : sq)
            assertEquals(Stream.concat(s.getAdjacent().stream(), Stream.of(s)).collect(Collectors.toSet()), s.atDistance(1));
    }

    @Test
    void testDistanceOneExact() {
        for (AbstractSquare s : sq)
            assertEquals(new HashSet<>(s.getAdjacent()), s.atDistance(1, 1));
    }

    @Test
    void testDistanceTwo() {
        for (AbstractSquare s : sq)
            assertEquals(Stream.concat(
                    Stream.concat(s.getAdjacent().stream().map(AbstractSquare::getAdjacent).flatMap(Set::stream), s.getAdjacent().stream()),
                    Stream.of(s)).collect(Collectors.toSet()), s.atDistance(2));
    }

    @Test
    void testDistanceOneTwo() {
        for (AbstractSquare s : sq)
            assertEquals(
                    Stream.concat(s.getAdjacent().stream().map(AbstractSquare::getAdjacent).flatMap(Set::stream), s.getAdjacent().stream())
                            .filter(t -> t != s).collect(Collectors.toSet()), s.atDistance(1, 2));
    }

    @Test
    void testDistanceTwoExact() {

        for (AbstractSquare s : sq) {
            assertEquals(s.getAdjacent().stream().map(AbstractSquare::getAdjacent).flatMap(Set::stream)
                    .filter(t -> t != s && !s.getAdjacent().contains(t)).collect(Collectors.toSet()), s.atDistance(2, 2));

        }
    }
}