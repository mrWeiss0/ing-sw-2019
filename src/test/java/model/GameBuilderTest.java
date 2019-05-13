package model;

import model.board.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.IntStream;

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
                .maxWeapons(6)
                .maxPowerUps(7)
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
            field = Figure.class.getDeclaredField("maxWeapons");
            field.setAccessible(true);
            assertEquals(6, field.getInt(f));
            field = Figure.class.getDeclaredField("maxPowerUps");
            field.setAccessible(true);
            assertEquals(7, field.getInt(f));
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

    @Test
    void testInitTiles() {
        Game g = new Game.Builder()
                .powerUps(new PowerUpImage(1))
                .ammoTiles(new AmmoTileImage(new AmmoCube(0,1), false))
                .build();
        AbstractSquare s = new AmmoSquare(new Room(), new int[]{});
        s.accept(g);
        Grabbable a = s.peek().iterator().next();
        Figure f = new Figure(1, 1, 1, 1, 1);
        s.grab(f, a);
        assertEquals(0, f.getPowerUps().size());
        assertTrue(IntStream.range(0, f.getAmmo().size()).allMatch(i -> new AmmoCube(0,1).value(i) == f.getAmmo().value(i)));
    }

    @Test
    void testTilePUp() {
        Game g = new Game.Builder()
                .powerUps(new PowerUpImage(2))
                .ammoTiles(new AmmoTileImage(new AmmoCube(1), true))
                .build();
        AbstractSquare s = new AmmoSquare(new Room(), new int[]{});
        s.accept(g);
        Grabbable a = s.peek().iterator().next();
        Figure f = new Figure(1, 1, 1, 1, 1);
        s.grab(f, a);
        AmmoCube powerUpAmmo = f.getPowerUps().iterator().next().getAmmo();
        assertTrue(IntStream.range(0, powerUpAmmo.size()).allMatch(i -> new AmmoCube(0,0, 1).value(i) == powerUpAmmo.value(i)));
    }
}
