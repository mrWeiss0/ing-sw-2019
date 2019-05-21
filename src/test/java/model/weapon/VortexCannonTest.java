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
                (shooter, board, last) -> shooter.getSquare().visibleSquares().stream().filter(t -> t != shooter.getSquare()).collect(Collectors.toSet()),
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
//TODO FIX
        blackhole = new FireMode(new AmmoCube(0,0,1), new FireStep(1,1,
                (shooter, board, last) -> shooter.getSquare().visibleFigures().stream().filter(t-> t!=shooter && !last.contains(t)).collect(Collectors.toSet()),
                (shooter, curr, last)-> {
                    curr.forEach(f->f.markFrom(shooter,1));
                }));


    }

    @BeforeEach
    void each() {
        figures = new Figure[]{
                new Figure(12, 3, 3, 3, 3),
                new Figure(12, 3, 3, 3, 3),
                new Figure(12, 3, 3, 3, 3),
                new Figure(12, 3, 3, 3, 3),
                new Figure(12, 3, 3, 3, 3)
        };
        board = boardBuilder
                .figures(Arrays.asList(figures))
                .figures(new Figure(12, 3, 3, 3, 3))
                .squares(new SquareImage().setCoords(0, 0))
                .build();
        for (Figure f : figures) f.moveTo(board.getSquares().iterator().next());
    }

    @Test
    void testCost() {
        assertTrue(ammoCubeEquals(new AmmoCube(0,0,1), Stream.of(base, blackhole).map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new)));
    }




}
