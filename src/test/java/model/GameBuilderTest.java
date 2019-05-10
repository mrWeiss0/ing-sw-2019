package model;

import model.board.Figure;
import model.board.SpawnSquare;
import model.board.SquareImage;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GameBuilderTest {
    private Game.Builder gameBuilder = new Game.Builder();

    @Test
    void testParam() {
        Game game = gameBuilder
                .nKills(10)
                .build();
        try {
            Field f = Game.class.getDeclaredField("remainingKills");
            f.setAccessible(true);
            assertEquals(10, f.getInt(game));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e);
        }
    }

    @Test
    void testFigure() {
        Player p = new Player();
        Game game = gameBuilder
                .player(p)
                .maxDamages(8)
                .maxMarks(4)
                .maxAmmo(5)
                .defaultAmmo(new AmmoCube(1, 2, 3))
                .build();
        Figure f = p.getFigure();
        assertSame(f, game.getBoard().getFigures().iterator().next());
        try {
            Field field;
            field = Figure.class.getDeclaredField("maxDamages");
            field.setAccessible(true);
            assertEquals(8, field.getInt(f));
            field = Figure.class.getDeclaredField("maxMarks");
            field.setAccessible(true);
            assertEquals(4, field.getInt(f));
            field = Figure.class.getDeclaredField("maxAmmo");
            field.setAccessible(true);
            assertEquals(5, field.getInt(f));
            field = Figure.class.getDeclaredField("ammo");
            field.setAccessible(true);
            Field ammoField = AmmoCube.class.getDeclaredField("ammo");
            ammoField.setAccessible(true);
            assertTrue(Objects.deepEquals(new int[]{1, 2, 3}, ammoField.get(f.getAmmo())));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e);
        }
    }

    @Test
    void testBoard() {
        Game game = gameBuilder
                .spawnCapacity(4)
                .squares(new SquareImage().coords(0, 0).spawn())
                .build();
        try {
            Field f = SpawnSquare.class.getDeclaredField("capacity");
            f.setAccessible(true);
            assertEquals(4, f.getInt(game.getBoard().getSquares().iterator().next()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e);
        }
    }
}
