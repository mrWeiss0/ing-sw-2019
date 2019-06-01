package server.model.weapon;

import server.model.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.board.Board;
import server.model.weapon.TargetGens;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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
                new Figure(12, 3, 3, 3, 3,3),
                new Figure(12, 3, 3, 3, 3,3),
                new Figure(12, 3, 3, 3, 3,3),
                new Figure(12, 3, 3, 3, 3,3),
                new Figure(12, 3, 3, 3, 3,3)
        };
        board = boardBuilder
                .figures(Arrays.asList(figures))
                .figures(new Figure(12, 3, 3, 3, 3,3))
                .squares(BoardBuilderTest.squareImages)
                .build();

        figures[0].moveTo(board.getSquares().stream().filter(x->x.getCoordinates()[0]==0 && x.getCoordinates()[1]==0).collect(Collectors.toList()).get(0));
        figures[1].moveTo(board.getSquares().stream().filter(x->x.getCoordinates()[0]==0 && x.getCoordinates()[1]==1).collect(Collectors.toList()).get(0));
        figures[2].moveTo(board.getSquares().stream().filter(x->x.getCoordinates()[0]==0 && x.getCoordinates()[1]==2).collect(Collectors.toList()).get(0));
        figures[3].moveTo(board.getSquares().stream().filter(x->x.getCoordinates()[0]==1 && x.getCoordinates()[1]==3).collect(Collectors.toList()).get(0));
        figures[4].moveTo(board.getSquares().stream().filter(x->x.getCoordinates()[0]==1 && x.getCoordinates()[1]==0).collect(Collectors.toList()).get(0));
    }

    @Test
    void testVisibleFigures(){
        Set<Targettable> targets = TargetGens.visibleFigures().get(figures[1],board, new ArrayList<>());
        assertTrue(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testNotInLast(){
        Set<Targettable> targets = TargetGens.notInLastFigures().get(figures[1],board, new ArrayList<>(Collections.singletonList(figures[0])));
        assertFalse(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertTrue(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testMaxDistance(){
        Set<Targettable> targets = TargetGens.maxDistanceFigures(2).get(figures[0],board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testInLast(){
        Set<Targettable> targets = TargetGens.inLastFigure().get(figures[1],board, new ArrayList<>(Collections.singletonList(figures[0])));
        assertTrue(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertFalse(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertFalse(targets.contains(figures[4]));
    }

    @Test
    void testAtDistanceFromVisible(){
        Set<Targettable> targets = TargetGens.atDistanceFromVisibleSquareFigures(2).get(figures[0],board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertTrue(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
        targets = TargetGens.atDistanceFromVisibleSquareFigures(0).get(figures[0],board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }

    @Test
    void testNotVisible(){
        Set<Targettable> targets = TargetGens.notVisibleFigures().get(figures[0],board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertFalse(targets.contains(figures[1]));
        assertFalse(targets.contains(figures[2]));
        assertTrue(targets.contains(figures[3]));
        assertFalse(targets.contains(figures[4]));
    }
    @Test
    void testOnCardinal(){
        Set<Targettable> targets = TargetGens.onCardinalFigure().get(figures[0],board, new ArrayList<>());
        assertFalse(targets.contains(figures[0]));
        assertTrue(targets.contains(figures[1]));
        assertTrue(targets.contains(figures[2]));
        assertFalse(targets.contains(figures[3]));
        assertTrue(targets.contains(figures[4]));
    }
}