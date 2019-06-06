package model.weapon;

import model.board.AbstractSquare;
import model.board.Board;
import model.board.BoardBuilderTest;
import model.board.Figure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WeaponsTest {

    private final Board.Builder boardBuilder = new Board.Builder();
    private Board board;
    private Figure[] figures;

    @BeforeEach
    void init(){
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
    void testPowerGloveBase(){
        Weapon pglove = Weapons.POWER_GLOVE.build();
        assertEquals(2, pglove.getFireModes().size());
        FireMode base = pglove.getFireModes().get(0);
        FireSequence fs = new FireSequence(figures[0],board,FireMode.flatSteps(Collections.singletonList(base)));
        assertEquals(Stream.of(figures[1],figures[4]).collect(Collectors.toSet()), fs.getTargets());
        assertNotEquals(figures[1].getLocation(),figures[0].getLocation());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertEquals(1, figures[1].getDamages().size());
        assertEquals(figures[1].getLocation(),figures[0].getLocation());
        board.applyMarks();
        figures[1].damageFrom(figures[0],1);
        assertEquals(4, figures[1].getDamages().size());
    }

    @Test
    void testPowerGloveRocket(){
        Weapon pglove = Weapons.POWER_GLOVE.build();
        FireSequence fs = new FireSequence(figures[0], board,FireMode
                .flatSteps(Collections.singletonList(pglove.getFireModes().get(1))));
        assertEquals(Stream.of(figures[1].getLocation(),figures[4].getLocation()).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[1].getLocation()).collect(Collectors.toSet()));
        assertEquals(figures[1].getLocation(),figures[0].getLocation());
        assertEquals(Stream.of(figures[1]).collect(Collectors.toSet()),fs.getTargets());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertEquals(2, figures[1].getDamages().size());
        assertEquals(Stream.of(figures[2].getLocation()).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[2].getLocation()).collect(Collectors.toSet()));
        assertEquals(Stream.of(figures[2]).collect(Collectors.toSet()), fs.getTargets());
        fs.run(Stream.of(figures[2]).collect(Collectors.toSet()));
        assertEquals(2, figures[2].getDamages().size());
    }

    @Test
    void testRocketLauncherBase(){
        Weapon rlaunch = Weapons.ROCKET_LAUNCHER.build();
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Arrays.asList
                (rlaunch.getFireModes().get(0), rlaunch.getFireModes().get(1), rlaunch.getFireModes().get(2))));
        assertEquals(Stream.of(figures[1],figures[2],figures[4]).collect(Collectors.toSet()),fs.getTargets());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertEquals(Stream.of(figures[0].getLocation(),figures[2].getLocation(), figures[1].getLocation()).collect(Collectors.toSet()),fs.getTargets());
        assertEquals(2, figures[1].getDamages().size());
        AbstractSquare finalDest = figures[2].getLocation();

        //MOVE 2 TO 0,1
        figures[2].moveTo(figures[1].getLocation());

        //MOVE 1 to 0,0
        fs.run(Stream.of(figures[0].getLocation()).collect(Collectors.toSet()));
        assertEquals(figures[0].getLocation(),figures[1].getLocation());

        //MOVE 0 to 0,2
        fs.run(Stream.of(figures[2].getLocation()).collect(Collectors.toSet()));

        //USE FRAG GRENADE
        fs.run(new HashSet<>());
        assertEquals(3, figures[1].getDamages().size());
        assertEquals(0, figures[0].getDamages().size());
        assertEquals(1, figures[2].getDamages().size());
    }

    @Test
    void testRailgunBase(){
        Weapon railgun = Weapons.RAILGUN.build();
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Collections.singletonList(
                railgun.getFireModes().get(0))));
        assertEquals(Stream.of(figures[1],figures[2],figures[3],figures[4]).collect(Collectors.toSet()),fs.getTargets());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertEquals(3, figures[1].getDamages().size());
    }

    @Test
    void testRailgunPiercing(){
        Weapon railgun = Weapons.RAILGUN.build();
        FireSequence fs = new FireSequence(figures[0], board, FireMode.flatSteps(Collections.singletonList(
                railgun.getFireModes().get(1))));
        assertEquals(Stream.of(figures[1],figures[2],figures[3],figures[4]).collect(Collectors.toSet()),fs.getTargets());
        fs.run(Stream.of(figures[1]).collect(Collectors.toSet()));
        assertEquals(2, figures[1].getDamages().size());
        assertEquals(Stream.of(figures[2]).collect(Collectors.toSet()),fs.getTargets());
        fs.run(Stream.of(figures[2]).collect(Collectors.toSet()));
        assertEquals(2,figures[2].getDamages().size());
    }

}