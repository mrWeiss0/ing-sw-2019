package server.model.weapon;

import server.model.board.*;
import server.model.weapon.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FlameThrowerTest {
    private static FireMode base, barbecue;
    private final Board.Builder boardBuilder = new Board.Builder();
    private Board board;
    private Figure[] figures;

    @BeforeAll
    static void init() {
        base = new FireMode(
                new FireStep(1, 1
                        , TargetGens.onCardinalSquare().and(TargetGens.maxDistanceSquares(1).not(TargetGens.maxDistanceSquares(0)))
                        , Effects.addCurrToLast())
                , new FireStep(0, 1
                , TargetGens.onLastFigures()
                , Effects.damageCurr(1).and(Effects.clearLast()
                .and(Effects.addShooterSquareToLast().and(Effects.addCurrFigureSquareToLast()))))
                , new FireStep(0, 1
                , TargetGens.sameDirectionAsLastSquares().and(TargetGens.maxDistanceFromLastSquares(2).not(TargetGens.inLast()))
                , Effects.clearLast().and(Effects.addCurrToLast()))
                , new FireStep(0, 1
                , TargetGens.onLastFigures()
                , Effects.damageCurr(1))
        );

        barbecue = new FireMode(
                new FireStep(1, 1,
                        TargetGens.onCardinalSquare().and(TargetGens.maxDistanceSquares(1).not(TargetGens.maxDistanceSquares(0))),
                        Effects.damageCurr(2).and(Effects.addShooterSquareToLast().and(Effects.addCurrToLast()))),
                new FireStep(0, 1,
                        TargetGens.sameDirectionAsLastSquares().and(TargetGens.maxDistanceFromLastSquares(2).not(TargetGens.inLast())),
                        Effects.damageCurr(2))
        );

    }


    @BeforeEach
    void each() {
        figures = new Figure[]{
                new Figure(11, 12, 3, 3, 3, 3),
                new Figure(11, 12, 3, 3, 3, 3),
                new Figure(11, 12, 3, 3, 3, 3),
                new Figure(11, 12, 3, 3, 3, 3),
                new Figure(11, 12, 3, 3, 3, 3)
        };
        board = boardBuilder
                .figures(Arrays.asList(figures))
                .figures(new Figure(12, 3, 3, 3, 3, 3))
                .squares(BoardBuilderTest.squareImages)
                .build();
        figures[0].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 0 && x.getCoordinates()[1] == 0).collect(Collectors.toList()).get(0));
        figures[1].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 0 && x.getCoordinates()[1] == 1).collect(Collectors.toList()).get(0));
        figures[2].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 0 && x.getCoordinates()[1] == 2).collect(Collectors.toList()).get(0));
        figures[3].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 2 && x.getCoordinates()[1] == 0).collect(Collectors.toList()).get(0));
        figures[4].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 1 && x.getCoordinates()[1] == 0).collect(Collectors.toList()).get(0));

    }

    @Test
    void testBase() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Collections.singletonList(base)));
        assertTrue(fs.hasNext());
        Set<AbstractSquare> expected = Stream.of(figures[1].getLocation(), figures[4].getLocation())
                .collect(Collectors.toSet());
        assertEquals(expected, fs.getTargets());

        fs.run(Stream.of(figures[1].getLocation()).collect(Collectors.toSet()));

        assertEquals(Stream.of(figures[1]).collect(Collectors.toSet()), fs.getTargets());

        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));

        assertEquals(1, figures[1].getDamages().size());
        assertEquals(Stream.of(figures[2].getLocation()).collect(Collectors.toSet()), fs.getTargets());

        fs.run(Stream.of(figures[2].getLocation()).collect(Collectors.toSet()));

        assertEquals(Stream.of(figures[2]).collect(Collectors.toSet()), fs.getTargets());

        fs.run(Stream.of(figures[2]).collect(Collectors.toSet()));

        assertEquals(1, figures[2].getDamages().size());
        assertFalse(fs.hasNext());
    }

    @Test
    void testBaseSingleCell() {
        FireSequence fs = new FireSequence(figures[1], board, FireMode.flatSteps(Collections.singletonList(base)));
        fs.run(Stream.of(figures[0].getLocation()).collect(Collectors.toSet()));
        fs.run(Stream.of(figures[0]).collect(Collectors.toSet()));
        assertEquals(0, fs.getTargets().size());
        fs.run(Collections.emptySet());
        assertEquals(0, fs.getTargets().size());
        fs.run(Collections.emptySet());
        assertFalse(fs.hasNext());
    }

    @Test
    void testBarbecue() {
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Collections.singletonList(barbecue)));
        assertEquals(Stream.of(figures[1].getLocation(), figures[4].getLocation()).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[1].getLocation()).collect(Collectors.toSet()));
        assertEquals(Stream.of(figures[2].getLocation()).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[2].getLocation()).collect(Collectors.toSet()));
        assertEquals(2, figures[2].getDamages().size());
        assertEquals(2, figures[1].getDamages().size());
        assertFalse(fs.hasNext());
    }

    @Test
    void testBarbecueSingleCell() {
        FireSequence fs = new FireSequence(figures[1], board, FireMode.flatSteps(Collections.singletonList(barbecue)));
        fs.run(Stream.of(figures[0].getLocation()).collect(Collectors.toSet()));
        fs.run(Collections.emptySet());
        assertEquals(2, figures[0].getDamages().size());
        assertFalse(fs.hasNext());
    }

}
