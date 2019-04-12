package model;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FigureDamageTest {
    public static Room room;
    public static AbstractSquare[] squares;
    private Figure[] figures;

    @BeforeAll
    static void init() {
        room = new Room();
        squares = new AbstractSquare[]{
                new SpawnSquare(room),
                new SpawnSquare(room)
        };
    }

    @BeforeEach
    void initCase() {
        figures = new Figure[]{
                new Figure(),
                new Figure(),
                new Figure(),
                new Figure(),
                new Figure()
        };
        figures[0].moveTo(squares[0]);
        figures[1].moveTo(squares[0]);
        figures[2].moveTo(squares[0]);
        figures[3].moveTo(squares[1]);
        figures[4].moveTo(squares[1]);
    }

    @Test
    void testSimpleDamage() {
        figures[0].damageFrom(figures[1], 5);
        assertEquals(5, figures[0].getDamages().size());
    }

    @Test
    void testSquareDamage() {
        squares[0].damageFrom(figures[0], 6);
        assertEquals(0, figures[0].getDamages().size());
        assertEquals(6, figures[1].getDamages().size());
        assertEquals(6, figures[2].getDamages().size());
        assertEquals(0, figures[3].getDamages().size());
        assertEquals(0, figures[4].getDamages().size());
    }

    @Test
    void testRoomDamage() {
        room.damageFrom(figures[0], 6);
        assertEquals(0, figures[0].getDamages().size());
        assertEquals(6, figures[1].getDamages().size());
        assertEquals(6, figures[2].getDamages().size());
        assertEquals(6, figures[3].getDamages().size());
        assertEquals(6, figures[4].getDamages().size());
    }

    @Test
    void testMoreThan12() {
        room.damageFrom(figures[0], 13);
        assertEquals(0, figures[0].getDamages().size());
        assertEquals(12, figures[1].getDamages().size());
        assertEquals(12, figures[2].getDamages().size());
        assertEquals(12, figures[3].getDamages().size());
        assertEquals(12, figures[4].getDamages().size());
    }

    @Test
    void test2FigureDamage() {
        figures[0].damageFrom(figures[1], 10);
        figures[0].damageFrom(figures[2], 5);
        assertEquals(12, figures[0].getDamages().size());
        assertEquals(10, figures[0].getDamages().stream().filter(Predicate.isEqual(figures[1])).count());
        assertEquals(2, figures[0].getDamages().stream().filter(Predicate.isEqual(figures[2])).count());
    }

    @Test
    void testSimpleMark() {
        figures[0].markFrom(figures[1], 1);
        figures[0].damageFrom(figures[1], 1);
        assertEquals(2, figures[0].getDamages().size());
    }

    @Test
    void testNotPopMark() {
        figures[0].markFrom(figures[2], 1);
        figures[0].damageFrom(figures[1], 1);
        assertEquals(1, figures[0].getDamages().size());
    }

    @Test
    void testMaxMarks() {
        figures[0].markFrom(figures[1], 2);
        figures[0].markFrom(figures[2], 4);
        figures[0].markFrom(figures[3], 1);
        figures[0].damageFrom(figures[2], 1);
        assertEquals(4, figures[0].getDamages().size());
        figures[0].damageFrom(figures[2], 1);
        assertEquals(5, figures[0].getDamages().size());
        figures[0].damageFrom(figures[3], 1);
        assertEquals(2, figures[0].getDamages().stream().filter(Predicate.isEqual(figures[3])).count());
    }

    @Test
    void testLimitDamageWithMarks() {
        figures[0].markFrom(figures[1], 3);
        figures[0].damageFrom(figures[1], 10);
        assertEquals(12, figures[0].getDamages().size());
    }

    @Test
    void testRoomMark() {
        room.markFrom(figures[0], 3);
        room.damageFrom(figures[0], 1);
        assertEquals(0, figures[0].getDamages().size());
        assertEquals(4, figures[1].getDamages().size());
        assertEquals(4, figures[2].getDamages().size());
        assertEquals(4, figures[3].getDamages().size());
        assertEquals(4, figures[4].getDamages().size());
    }

}
