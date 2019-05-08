package model.weapon;

import model.AmmoCube;
import model.board.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class machineGunTest {
    private static FireMode base, focus, tripod;
    private Board board = new Board();
    private Figure[] figures;

    @BeforeAll
    static void init() {
        base = new FireMode();
        base.addStep(new FireStep(2,
                (shooter, board, last) -> shooter.getSquare().visibleFigures().stream().filter(t -> t != shooter).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    curr.forEach(f -> f.damageFrom(shooter, 1));
                    last.addAll(curr);
                    if (last.size() < 2) last.add(null);
                    return last;
                }));

        focus = new FireMode(new AmmoCube(0, 1));
        focus.addStep(new FireStep(1,
                (shooter, board, last) -> last.stream().limit(2).filter(Objects::nonNull).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    Targettable current = curr.get(0);
                    current.damageFrom(shooter, 1);
                    last.set(last.indexOf(current), null);
                    last.add(current);
                    return last;
                }));

        tripod = new FireMode(new AmmoCube(1));
        tripod.addStep(new FireStep(1,
                (shooter, board, last) -> last.stream().limit(2).filter(Objects::nonNull).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    Targettable current = curr.get(0);
                    current.damageFrom(shooter, 1);
                    last.set(last.indexOf(current), null);
                    last.add(current);
                    return last;
                }));
        tripod.addStep(new FireStep(1,
                (shooter, board, last) -> shooter.getSquare().visibleFigures().stream().filter(t -> !last.contains(t) && t != shooter).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    curr.get(0).damageFrom(shooter, 1);
                    return last;
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
        board.getFigures().addAll(Arrays.asList(figures));
        board.getFigures().add(new FigureMock());
        AbstractSquare square = new AmmoSquareMock(new Room());
        for (Figure f : figures) f.moveTo(square);
        board.addSquare(square);
    }

    @Test
    void testCost() {
        assertEquals(new AmmoCube(1, 1), Stream.of(base, focus, tripod).map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new));
    }

    @Test
    void testBase() {
        FireSequence fs = new FireSequence(figures[0], board, Stream.of(base).flatMap(FireMode::getStepsStream).collect(Collectors.toList()));
        // Base mode
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1], figures[2], figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertFalse(fs.run(Collections.singletonList(figures[0])));
        assertFalse(fs.run(Arrays.asList(figures[1], figures[3], figures[2])));
        assertFalse(fs.run(Arrays.asList(figures[1], figures[1])));
        // Run
        assertTrue(fs.run(Arrays.asList(figures[1], figures[2])));
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[1].getDamages());
        assertEquals(Collections.singletonList(figures[0]), figures[2].getDamages());
    }

    @Test
    void testFocus() {
        FireSequence fs = new FireSequence(figures[0], board, Stream.of(base, focus).flatMap(FireMode::getStepsStream).collect(Collectors.toList()));
        // Base mode
        assertTrue(fs.run(Arrays.asList(figures[1], figures[2])));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), fs.getTargets());
        assertFalse(fs.run(Arrays.asList(figures[1], figures[2])));
        assertFalse(fs.run(Collections.singletonList(figures[3])));
        // Run
        assertTrue(fs.run(Collections.singletonList(figures[2])));
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[1].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[2].getDamages());
    }

    @Test
    void testTripod() {
        FireSequence fs = new FireSequence(figures[0], board, Stream.of(base, tripod).flatMap(FireMode::getStepsStream).collect(Collectors.toList()));
        // Base mode
        assertTrue(fs.run(Arrays.asList(figures[1], figures[2])));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[1], figures[2]).collect(Collectors.toSet()), fs.getTargets());
        assertFalse(fs.run(Arrays.asList(figures[1], figures[2])));
        assertFalse(fs.run(Collections.singletonList(figures[3])));
        // Run
        assertTrue(fs.run(Collections.singletonList(figures[2])));
        assertTrue(fs.hasNext());
        // Target gen
        assertEquals(Stream.of(figures[3], figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertFalse(fs.run(Arrays.asList(figures[3], figures[4])));
        assertFalse(fs.run(Collections.singletonList(figures[1])));
        // Run
        assertTrue(fs.run(Collections.singletonList(figures[4])));
        assertFalse(fs.hasNext());
        // Check
        assertEquals(Stream.of(figures[1], figures[2], figures[4]).collect(Collectors.toSet()), board.getDamaged());
        assertEquals(Collections.singletonList(figures[0]), figures[1].getDamages());
        assertEquals(Collections.singletonList(figures[0]), figures[4].getDamages());
        assertEquals(Arrays.asList(figures[0], figures[0]), figures[2].getDamages());
    }
}
