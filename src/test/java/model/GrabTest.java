package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrabTest {
    private Figure f;
    private AbstractSquare[] squares;
    Set<Grabbable> grabbed;

    @BeforeEach
    public void init() {
        f = new Figure();
        squares = new AbstractSquare[]{new AmmoSquare(new Room()), new SpawnSquare(new Room())};

    }

    @Test
    public void AmmoTest() {
        grabbed = squares[0].peek();
        assertEquals(Stream.of().collect(Collectors.toSet()), grabbed);
    }

    @Test
    public void SpawnTest() {
        grabbed = squares[1].peek();
        assertEquals(Stream.of().collect(Collectors.toSet()), grabbed);
    }
}