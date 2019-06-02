package server.model.weapon;

import server.model.AmmoCube;
import server.model.board.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LockRifleTest {
    private static FireMode base, secondLock;
    private final Board.Builder boardBuilder = new Board.Builder();
    private Board board;
    private Figure[] figures;

    private static boolean ammoCubeEquals(AmmoCube a, AmmoCube b) {
        try {
            Field f = AmmoCube.class.getDeclaredField("ammo");
            f.setAccessible(true);
            return Objects.deepEquals(f.get(a), f.get(b));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    @BeforeAll
    static void init() {
        base = new FireMode(new FireStep(1, 1,
                TargetGens.visibleFigures(),
                Effects.damageCurr(2).and(Effects.markCurr(1).and(Effects.addCurrToLast()))));

        secondLock = new FireMode(new AmmoCube(0,0,1), new FireStep(1,1,
                TargetGens.visibleFigures().and(TargetGens.differentFigures().less(TargetGens.inLastFigure())),
                Effects.markCurr(1)));


    }

    @BeforeEach
    void each() {
        figures = new Figure[]{

                new Figure(12, 3, 3, 3, 3, 3),
                new Figure(12, 3, 3, 3, 3, 3),
                new Figure(12, 3, 3, 3, 3, 3),
                new Figure(12, 3, 3, 3, 3, 3),
                new Figure(12, 3, 3, 3, 3, 3)
        };
        board = boardBuilder
                .figures(Arrays.asList(figures))
                .figures(new Figure(12, 3, 3, 3, 3, 3))
                .squares(new SquareImage().setCoords(0, 0))
                .build();
        for (Figure f : figures) f.moveTo(board.getSquares().iterator().next());
    }

    @Test
    void testCost() {
        assertTrue(ammoCubeEquals(new AmmoCube(0,0,1), Stream.of(base, secondLock).map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new)));
    }

    @Test
    void testBase(){
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Collections.singletonList(base)));
        assertTrue(fs.hasNext());

        assertEquals(Stream.of(figures[1], figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertFalse(fs.hasNext());
        assertEquals(Stream.of(figures[1]).collect(Collectors.toSet()), board.getDamaged());
        board.applyMarks();
        assertEquals(new HashSet<>(), board.getDamaged());
        assertEquals(Arrays.asList(figures[0],figures[0]), figures[1].getDamages());

        fs = new FireSequence(figures[2], board, FireMode.flatSteps(Collections.singletonList(base)));
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        board.applyMarks();
        assertEquals(new HashSet<>(), board.getDamaged());
        assertEquals(Arrays.asList(figures[0],figures[0], figures[2], figures[2]), figures[1].getDamages());
    }
    @Test
    void testSecondLock(){
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList(base, secondLock)));
        assertTrue(fs.hasNext());
        assertEquals(Stream.of(figures[1], figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[4]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        assertEquals(Stream.of(figures[4]).collect(Collectors.toSet()),board.getDamaged());
        board.applyMarks();
        assertEquals(new HashSet<>(), board.getDamaged());
        assertEquals(Stream.of(figures[1], figures[2], figures[3]).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        board.applyMarks();
        assertEquals(Arrays.asList(figures[0],figures[0]), figures[4].getDamages());
        figures[4].damageFrom(figures[0], 1);
        figures[1].damageFrom(figures[0], 1);
        assertEquals(Arrays.asList(figures[0],figures[0], figures[0], figures[0]), figures[4].getDamages());
        assertEquals(Arrays.asList(figures[0],figures[0]), figures[1].getDamages());
    }


}
