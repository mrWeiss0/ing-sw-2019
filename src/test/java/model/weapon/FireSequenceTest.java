package model.weapon;

import model.AmmoCube;
import model.board.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FireSequenceTest {
    private static FireMode base, focus, tripod;
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
        base = new FireMode(new FireStep(1, 2,
                (shooter, board, last) -> shooter.getSquare().visibleFigures().stream().filter(t -> t != shooter).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    curr.forEach(f -> f.damageFrom(shooter, 1));
                    last.addAll(curr);
                    if (last.size() < 2) last.add(null);
                }));

        TargetGen otherTG = (shooter, board, last) -> last.stream().limit(2).filter(Objects::nonNull).collect(Collectors.toSet());

        Effect otherEff = (shooter, curr, last) -> {
            try {
                Targettable current = curr.iterator().next();
                current.damageFrom(shooter, 1);
                last.set(last.indexOf(current), null);
                last.add(current);
            } catch (NoSuchElementException ignore) {
            }
        };

        focus = new FireMode(new AmmoCube(0, 1), new FireStep(1, 1, otherTG, otherEff));

        tripod = new FireMode(new AmmoCube(1), new FireStep(0, 1, otherTG, otherEff), new FireStep(0, 1,
                (shooter, board, last) -> shooter.getSquare().visibleFigures().stream().filter(t -> !last.contains(t) && t != shooter).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    try {
                        curr.iterator().next().damageFrom(shooter, 1);
                    } catch (NoSuchElementException ignore) {
                    }
                }));
    }

    @BeforeEach
    void each() {
        figures = new Figure[]{
                new FigureMock(),
                new FigureMock(),
                new FigureMock(),
                new FigureMock(),
                new FigureMock()
        };
        board = boardBuilder
                .figures(Arrays.asList(figures))
                .figure(new FigureMock())
                .squares(new SquareImage().coords(0, 0))
                .build();
        for (Figure f : figures) f.moveTo(board.getSquares().iterator().next());
    }

    @Test
    void testCost() {
        assertTrue(ammoCubeEquals(new AmmoCube(1, 1), Stream.of(base, focus, tripod).map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new)));
    }

    @Test
    void testBase() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Collections.singletonList(base)));
        // Base mode
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1], figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[0]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1], figures[3], figures[2]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Collections.emptySet()));
        // Run
        fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()));
        assertFalse(fs.hasNext());
        assertThrows(NoSuchElementException.class, () -> fs.run(Stream.of(figures[0]).collect(Collectors.toSet())));
        // Check
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), board.getDamaged());
        board.clearDamaged();
        assertEquals(new HashSet<>(), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[1].getDamages());
        assertEquals(Collections.singletonList(figures[0]), figures[2].getDamages());
    }

    @Test
    void testFocus() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList(base, focus)));
        // Base mode
        fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Collections.emptySet()));
        // Run
        fs.run(Stream.of(figures[2]).collect(Collectors.toSet()));
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[1].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[2].getDamages());
    }

    @Test
    void testTripod() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList(base, tripod)));
        // Base mode
        fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3]).collect(Collectors.toSet())));
        // Run
        fs.run(Stream.of(figures[2]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3], figures[4]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1]).collect(Collectors.toSet())));
        // Run
        fs.run(Stream.of(figures[4]).collect(Collectors.toSet()));
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2], figures[4]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[1].getDamages());
        assertEquals(Collections.singletonList(figures[0]), figures[4].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[2].getDamages());
    }

    @Test
    void testTripodOnlyOther() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList(base, tripod)));
        // Base mode
        fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3]).collect(Collectors.toSet())));
        // Run
        fs.run(Collections.emptySet());
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3], figures[4]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1]).collect(Collectors.toSet())));
        // Run
        fs.run(Stream.of(figures[4]).collect(Collectors.toSet()));
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2], figures[4]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[1].getDamages());
        assertEquals(Collections.singletonList(figures[0]), figures[2].getDamages());
        assertEquals(Collections.singletonList(figures[0]), figures[4].getDamages());
    }

    @Test
    void testTripodOnlyLast() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList(base, tripod)));
        // Base mode
        fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3]).collect(Collectors.toSet())));
        // Run
        fs.run(Stream.of(figures[2]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3], figures[4]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1]).collect(Collectors.toSet())));
        // Run
        fs.run(Collections.emptySet());
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[1].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[2].getDamages());
    }

    @Test
    void testFocusTripod() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList(base, focus, tripod)));
        // Base mode
        fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()));
        // Focus mode
        fs.run(Stream.of(figures[2]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[2]).collect(Collectors.toSet())));
        // Run
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3], figures[4]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1]).collect(Collectors.toSet())));
        // Run
        fs.run(Stream.of(figures[4]).collect(Collectors.toSet()));
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2], figures[4]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[4].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[1].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[2].getDamages());
    }

    @Test
    void testTripodFocus() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList(base, tripod, focus)));
        // Base mode
        fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()));
        // Tripod mode
        fs.run(Stream.of(figures[2]).collect(Collectors.toSet()));
        fs.run(Stream.of(figures[4]).collect(Collectors.toSet()));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1]).collect(Collectors.toSet()), fs.getTargets());
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[2]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[3]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[4]).collect(Collectors.toSet())));
        assertThrows(IllegalArgumentException.class, () -> fs.run(Stream.of(figures[1], figures[2]).collect(Collectors.toSet())));
        // Run
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2], figures[4]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[4].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[1].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[2].getDamages());
    }
}
