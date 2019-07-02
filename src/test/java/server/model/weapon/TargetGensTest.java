package server.model.weapon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.board.Board;
import server.model.board.BoardBuilderTest;
import server.model.board.Figure;
import server.model.board.Targettable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TargetGensTest {
    private final Board.Builder boardBuilder = new Board.Builder();
    private Board board;
    private Figure[] figures;

    /* TEST BOARD:
     * +-----------+
     * | 0 # 1   2s|
     * |   +-----#-+---+
     * | 3s| 4   5 # 6 |
     * +-#-+-#---#-+   |
     * | 7   8   9 #10s|
     * +-----------+---+
     */
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
                .squares(BoardBuilderTest.squareImages)
                .build();

        figures[0].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 0 && x.getCoordinates()[1] == 0).collect(Collectors.toList()).get(0));
        figures[1].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 0 && x.getCoordinates()[1] == 1).collect(Collectors.toList()).get(0));
        figures[2].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 0 && x.getCoordinates()[1] == 2).collect(Collectors.toList()).get(0));
        figures[3].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 1 && x.getCoordinates()[1] == 3).collect(Collectors.toList()).get(0));
        figures[4].moveTo(board.getSquares().stream().filter(x -> x.getCoordinates()[0] == 1 && x.getCoordinates()[1] == 0).collect(Collectors.toList()).get(0));
    }

    @Test
    void testVisibleFigures() {
        Set<Targettable> targets = TargetGens.visibleFigures().get(figures[1], board, new ArrayList<>());
        assertTrue(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testNotInLast() {
        Set<Targettable> targets = TargetGens.differentFigures().not(TargetGens.inLastFigure()).get(figures[1], board, new ArrayList<>(Collections.singletonList(figures[0])));
        assertFalse(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertTrue(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testMaxDistance() {
        Set<Targettable> targets = TargetGens.atDistanceFigures(2).get(figures[0], board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testInLast() {
        Set<Targettable> targets = TargetGens.inLastFigure().get(figures[1], board, new ArrayList<>(Collections.singletonList(figures[0])));
        assertTrue(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertFalse(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertFalse(targets.contains(figures[4]));
    }

    @Test
    void testAtDistanceFromVisible() {
        Set<Targettable> targets = TargetGens.atDistanceFromVisibleSquareFigures(2).get(figures[0], board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertTrue(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
        targets = TargetGens.atDistanceFromVisibleSquareFigures(0).get(figures[0], board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testNotVisible() {
        Set<Targettable> targets = TargetGens.differentFigures().not(TargetGens.visibleFigures()).get(figures[0], board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertFalse(targets.contains(figures[2]));
        assertTrue(targets.contains(figures[3]));
        assertFalse(targets.contains(figures[4]));
    }

    @Test
    void testOnCardinalFigure() {
        Set<Targettable> targets = TargetGens.onCardinalFigures().get(figures[0], board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testOnCardinalSquare() {
        Set<Targettable> targets = TargetGens.onCardinalSquare().get(figures[0], board, new ArrayList<>());
        assertTrue(targets.contains(figures[0].getLocation()));
        assertTrue(targets.contains(figures[1].getLocation()));
        assertTrue(targets.contains(figures[2].getLocation()));
        assertFalse(targets.contains(figures[3].getLocation()));
        assertTrue(targets.contains(figures[4].getLocation()));
    }

    @Test
    void testVisibleFromLast() {
        Set<Targettable> targets = TargetGens.visibleFromLastFigures().get(figures[0], board, new ArrayList<>(Collections.singletonList(figures[3])));
        assertFalse(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertFalse(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertFalse(targets.contains(figures[4]));

        targets = TargetGens.visibleFromLastFigures().get(figures[0], board, new ArrayList<>(Collections.singletonList(figures[2])));
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertFalse(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertFalse(targets.contains(figures[4]));

        targets = TargetGens.visibleFromLastFigures().get(figures[0], board, new ArrayList<>(Collections.singletonList(figures[1])));
        assertFalse(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testVisibleRoom() {
        Set<Targettable> targets = TargetGens.visibleRoom().get(figures[0], board, new ArrayList<>());
        assertTrue(targets.contains(figures[0].getLocation().getRoom()));
        assertTrue(targets.contains(figures[1].getLocation().getRoom()));
        assertFalse(targets.contains(figures[3].getLocation().getRoom()));
    }

    @Test
    void testDifferentRoom() {
        Set<Targettable> targets = TargetGens.differentRoom().get(figures[0], board, new ArrayList<>());
        assertFalse(targets.contains(figures[0].getLocation().getRoom()));
        assertTrue(targets.contains(figures[1].getLocation().getRoom()));
        assertTrue(targets.contains(figures[3].getLocation().getRoom()));
    }

    @Test
    void testVisibleDifferentRoom() {
        Set<Targettable> targets = TargetGens.differentRoom().and(TargetGens.visibleRoom()).get(figures[0], board, new ArrayList<>());
        assertFalse(targets.contains(figures[0].getLocation().getRoom()));
        assertTrue(targets.contains(figures[1].getLocation().getRoom()));
        assertFalse(targets.contains(figures[3].getLocation().getRoom()));
    }

    @Test
    void testSameDirectionAsLast() {
        Set<Targettable> targets = TargetGens.sameDirectionAsLastSquares().get(figures[1], board, new ArrayList<>(Arrays.asList(figures[0].getLocation(), figures[1].getLocation())));
        assertTrue(targets.contains(figures[1].getLocation()));
        assertTrue(targets.contains(figures[2].getLocation()));
        assertFalse(targets.contains(figures[0].getLocation()));
        assertFalse(targets.contains(figures[3].getLocation()));
        assertFalse(targets.contains(figures[4].getLocation()));

        targets = TargetGens.sameDirectionAsLastSquares().get(figures[4], board, new ArrayList<>(Arrays.asList(figures[0].getLocation(), figures[4].getLocation())));
        assertTrue(targets.contains(figures[4].getLocation()));
        assertFalse(targets.contains(figures[2].getLocation()));
        assertFalse(targets.contains(figures[0].getLocation()));
        assertFalse(targets.contains(figures[3].getLocation()));
        assertFalse(targets.contains(figures[1].getLocation()));
    }

    @Test
    void testMaxDistanceFromLastSquare() {
        Set<Targettable> targets = TargetGens.atDistanceFromLastSquares(1).get(figures[0], board, new ArrayList<>(Collections.singletonList(figures[1].getLocation())));
        assertTrue(targets.contains(figures[1].getLocation()));
        assertTrue(targets.contains(figures[2].getLocation()));
        assertTrue(targets.contains(figures[0].getLocation()));
        assertFalse(targets.contains(figures[3].getLocation()));
        assertFalse(targets.contains(figures[4].getLocation()));
    }

    @Test
    void testOnLast() {
        figures[3].moveTo(figures[1].getLocation());
        Set<Targettable> targets = TargetGens.onLastFigures().get(figures[0], board, new ArrayList<>(Collections.singletonList(figures[1].getLocation())));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[3]));
        assertFalse(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[4]));
    }

    @Test
    void testMaxDistanceSquare() {
        Set<Targettable> targets = TargetGens.atDistanceSquares(1).get(figures[0], board, new ArrayList<>());
        assertTrue(targets.contains(figures[1].getLocation()));
        assertFalse(targets.contains(figures[2].getLocation()));
        assertTrue(targets.contains(figures[0].getLocation()));
        assertFalse(targets.contains(figures[3].getLocation()));
        assertTrue(targets.contains(figures[4].getLocation()));
    }

    @Test
    void testDifferentSquareFigures() {
        figures[1].moveTo(figures[0].getLocation());
        Set<Targettable> targets = TargetGens.differentFigures().not(TargetGens.atDistanceFigures(0)).get(figures[0], board, new ArrayList<>());
        assertFalse(targets.contains(figures[1]));
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[2]));
        assertTrue(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testMaxDistAndOnLast() {
        Set<Targettable> targets = TargetGens.atDistanceFigures(1)
                .not(TargetGens.onLastFigures())
                .not(TargetGens.atDistanceFigures(0))
                .get(figures[0], board, new ArrayList<>(Collections.singletonList(figures[1].getLocation())));
        assertFalse(targets.contains(figures[1]));
    }

}
