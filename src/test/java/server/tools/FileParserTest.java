package server.tools;

import server.model.AmmoTileImage;
import server.model.PowerUpImage;
import server.model.board.BoardBuilderTest;
import server.model.board.SquareImage;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileParserTest {
    private static boolean reflectEquals(Object a, Object b) {
        return Arrays.stream(a.getClass().getFields()).allMatch((f) -> {
            try {
                return Objects.deepEquals(f.get(a), (f.get(b)));
            } catch (IllegalAccessException ignore) {
                return false;
            }
        });
    }

    private static boolean reflectArrayEquals(Object[] a, Object[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; ++i) if (!reflectEquals(a[i], b[i])) return false;
        return true;
    }

    @Test
    void testInvalidSquares() {
        assertTrue(reflectArrayEquals(new SquareImage[]{new SquareImage().setRoomId(0).setAdjacent(0).setSpawn()}, FileParser.readSquares(new StringReader("[{\"roomId\":0,\"adjacent\":[0],\"spawn\":true}]"))));
        assertTrue(reflectArrayEquals(new SquareImage[]{new SquareImage().setCoords(1)}, FileParser.readSquares(new StringReader("[{\"coords\":[1]}]"))));
        assertTrue(reflectArrayEquals(new SquareImage[]{new SquareImage().setCoords(1, 2, 3)}, FileParser.readSquares(new StringReader("[{\"coords\":[1,2,3]}]"))));
        assertTrue(reflectArrayEquals(new SquareImage[]{new SquareImage().setId(1).setCoords(0, 1), new SquareImage().setId(1).setCoords(0, 2)}, FileParser.readSquares(new StringReader("[{\"id\":1,\"coords\":[0,1]},{\"id\":1,\"coords\":[0,2]}]"))));
    }

    @Test
    void testValidSquares() {
        assertTrue(reflectArrayEquals(BoardBuilderTest.squareImages, FileParser.readSquares(new StringReader("[" +
                "{\"id\":8,\"coords\":[2,1],\"roomId\":4,\"adjacent\":[4,7]}," +
                "{\"id\":1,\"coords\":[0,1],\"roomId\":1,\"adjacent\":[0,2]}," +
                "{\"id\":6,\"coords\":[1,3],\"roomId\":5,\"adjacent\":[5,10]}," +
                "{\"id\":9,\"coords\":[2,2],\"roomId\":4,\"adjacent\":[8,5]}," +
                "{\"id\":4,\"coords\":[1,1],\"roomId\":2,\"adjacent\":[5]}," +
                "{\"coords\":[0,0]}," +
                "{\"id\":5,\"coords\":[1,2],\"roomId\":2}," +
                "{\"id\":7,\"coords\":[2,0],\"roomId\":4,\"adjacent\":[3]}," +
                "{\"id\":2,\"coords\":[0,2],\"roomId\":1,\"adjacent\":[5,1],\"spawn\":true,\"color\":1}," +
                "{\"id\":3,\"coords\":[1,0],\"roomId\":0,\"adjacent\":[0],\"spawn\":true,\"color\":2}," +
                "{\"id\":10,\"coords\":[2,3],\"roomId\":5,\"adjacent\":[9,6],\"spawn\":true}]"))));
    }

    @Test
    void testAmmoTiles() {
        assertTrue(reflectArrayEquals(new AmmoTileImage[]{
                        new AmmoTileImage(true, 1),
                        new AmmoTileImage(true, 0, 1),
                        new AmmoTileImage(false, 1, 2)
                },
                FileParser.readAmmoTiles(new StringReader("[" +
                        "{\"ammo\":[1],\"powerUp\":true}," +
                        "{\"ammo\":[0,1],\"powerUp\":true}," +
                        "{\"ammo\":[1,2],\"powerUp\":false}]"))
        ));
    }

    @Test
    void testPowerUps() {
        assertTrue(reflectArrayEquals(new PowerUpImage[]{
                        new PowerUpImage(1),
                        new PowerUpImage(2),
                        new PowerUpImage(3)
                },
                FileParser.readPowerUps(new StringReader("[" +
                        "{\"color\":1}," +
                        "{\"color\":2}," +
                        "{\"color\":3}]"
                ))
        ));
    }
}
