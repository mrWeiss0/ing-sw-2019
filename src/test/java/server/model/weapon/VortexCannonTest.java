package server.model.weapon;

import server.model.AmmoCube;
import server.model.board.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.weapon.TargetGens;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class VortexCannonTest {
    private static FireMode base, blackhole;
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
        base = new FireMode(
                new FireStep(1, 1,
                TargetGens.visibleSquares(),
                (shooter, curr, last) ->
                    last.addAll(curr)),
                new FireStep(1,1,
                (shooter, board, last)-> ((AbstractSquare)last.get(0)).atDistance(1).stream().
                        collect(HashSet::new,(x,y)->x.addAll(y.getOccupants().stream().filter(z->z!=shooter).collect(Collectors.toSet())),Set::addAll),
                (shooter, curr, last)-> {
                    ((Figure)curr.toArray()[0]).moveTo((AbstractSquare)last.get(0));
                    curr.forEach(x->x.damageFrom(shooter,2));
                    last.addAll(curr);
                }
        ));
        blackhole = new FireMode(new AmmoCube(0,0,1), new FireStep(1,2,
                (shooter, board, last) -> ((AbstractSquare)last.get(0)).atDistance(1).stream().
                        collect(HashSet::new,(x,y)->x.addAll(y.getOccupants().stream().filter(z->z!=shooter && !last.contains(z)).collect(Collectors.toSet())),Set::addAll),
                (shooter, curr, last)-> {
                    curr.forEach(f->((Figure)f).moveTo((AbstractSquare) last.get(0)));
                    curr.forEach(f->f.damageFrom(shooter,1));
                }));
    }

    @BeforeEach
    void each() {
        figures = new Figure[]{
                new Figure(12, 3, 3, 3, 3,3),
                new Figure(12, 3, 3, 3, 3,3),
                new Figure(12, 3, 3, 3, 3,3),
                new Figure(12, 3, 3, 3, 3,3),
                new Figure(12, 3, 3, 3, 3,3)
        };
        board = boardBuilder
                .figures(Arrays.asList(figures))
                .figures(new Figure(12, 3, 3, 3, 3,3))
                .squares(new SquareImage().setCoords(0, 0).setId(0), new SquareImage().setCoords(0,1).setId(1).setAdjacent(0))
                .build();
        for (Figure f : figures) f.moveTo(board.getSquares().iterator().next());
    }

    @Test
    void testCost() {
        assertTrue(ammoCubeEquals(new AmmoCube(0,0,1), Stream.of(base, blackhole).map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new)));
    }

    @Test
    void testBase(){
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Collections.singletonList(base)));
        assertTrue(fs.hasNext());

        assertNotEquals(Stream.of(figures[1], figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertEquals(board.getSquares().stream().filter(x->x!=figures[0].getLocation()).collect(Collectors.toSet()), fs.getTargets());
        Targettable target = (Targettable) fs.getTargets().toArray()[0];
        fs.run(Stream.of(target).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        assertEquals(Stream.of(figures[1], figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertEquals(Stream.of(figures[1]).collect(Collectors.toSet()), board.getDamaged());
        board.applyMarks();
        assertEquals(new HashSet<>(), board.getDamaged());
        assertEquals(Arrays.asList(figures[0],figures[0]), figures[1].getDamages());
        assertEquals(target, figures[1].getLocation());
    }
    @Test
    void testBlackHole(){
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList(base,blackhole)));
        assertTrue(fs.hasNext());

        assertNotEquals(Stream.of(figures[1], figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertEquals(board.getSquares().stream().filter(x->x!=figures[0].getLocation()).collect(Collectors.toSet()), fs.getTargets());
        Targettable target = (Targettable) fs.getTargets().toArray()[0];
        fs.run(Stream.of(target).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        assertEquals(Stream.of(figures[1], figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertEquals(Stream.of(figures[1]).collect(Collectors.toSet()), board.getDamaged());
        board.applyMarks();
        assertEquals(new HashSet<>(), board.getDamaged());
        assertEquals(Arrays.asList(figures[0],figures[0]), figures[1].getDamages());
        assertEquals(target, figures[1].getLocation());

        assertTrue(fs.hasNext());
        assertEquals(Stream.of(figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());

        fs.run(Stream.of(figures[2],figures[3]).collect(Collectors.toSet()));
        assertEquals(Stream.of(figures[2],figures[3]).collect(Collectors.toSet()), board.getDamaged());
        board.applyMarks();
        assertEquals(new HashSet<>(), board.getDamaged());
        assertEquals(Arrays.asList(figures[0]), figures[2].getDamages());
        assertEquals(Arrays.asList(figures[0]), figures[3].getDamages());
        assertEquals(target, figures[2].getLocation());
        assertEquals(target, figures[3].getLocation());
        assertFalse(fs.hasNext());

    }


}
