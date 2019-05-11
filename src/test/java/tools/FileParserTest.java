package tools;

import model.board.BoardBuilderTest;
import model.board.SquareImage;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileParserTest {
    private static boolean squareReflectEquals(SquareImage a, SquareImage b) {

        return Arrays.stream(SquareImage.class.getDeclaredFields()).peek(f -> f.setAccessible(true)).reduce(true, (u, f) -> {
            boolean c = false;
            try {
                c = Objects.deepEquals(f.get(a), (f.get(b)));
            } catch (IllegalAccessException ignore) {
            }
            return u && c;
        }, (w, v) -> w && v);

    }

    private static boolean squareArrayEquals(SquareImage[] a, SquareImage[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; ++i) if (!squareReflectEquals(a[i], b[i])) return false;
        return true;
    }

    @Test
    void testInvalid() {
        assertTrue(squareArrayEquals(new SquareImage[]{new SquareImage().roomId(0).adjacent(0).spawn()}, FileParser.readSquares(new StringReader("[{\"roomId\":0,\"adjacent\":[0],\"spawn\":true}]"))));
        assertTrue(squareArrayEquals(new SquareImage[]{new SquareImage().coords(1)}, FileParser.readSquares(new StringReader("[{\"coords\":[1]}]"))));
        assertTrue(squareArrayEquals(new SquareImage[]{new SquareImage().coords(1, 2, 3)}, FileParser.readSquares(new StringReader("[{\"coords\":[1,2,3]}]"))));
        assertTrue(squareArrayEquals(new SquareImage[]{new SquareImage().id(1).coords(0, 1), new SquareImage().id(1).coords(0, 2)}, FileParser.readSquares(new StringReader("[{\"id\":1,\"coords\":[0,1]},{\"id\":1,\"coords\":[0,2]}]"))));
    }

    @Test
    void testValid() {
        assertTrue(squareArrayEquals(BoardBuilderTest.squareImages, FileParser.readSquares(new StringReader("[" +
                "{\"id\":8,\"coords\":[2,1],\"roomId\":4,\"adjacent\":[4,7]}," +
                "{\"id\":1,\"coords\":[0,1],\"roomId\":1,\"adjacent\":[0,2]}," +
                "{\"id\":6,\"coords\":[1,3],\"roomId\":5,\"adjacent\":[5,10]}," +
                "{\"id\":9,\"coords\":[2,2],\"roomId\":4,\"adjacent\":[8,5]}," +
                "{\"id\":4,\"coords\":[1,1],\"roomId\":2,\"adjacent\":[5]}," +
                "{\"coords\":[0,0]}," +
                "{\"id\":5,\"coords\":[1,2],\"roomId\":2}," +
                "{\"id\":7,\"coords\":[2,0],\"roomId\":4,\"adjacent\":[3]}," +
                "{\"id\":2,\"coords\":[0,2],\"roomId\":1,\"adjacent\":[5,1],\"spawn\":true}," +
                "{\"id\":3,\"coords\":[1,0],\"roomId\":0,\"adjacent\":[0],\"spawn\":true}," +
                "{\"id\":10,\"coords\":[2,3],\"roomId\":5,\"adjacent\":[9,6],\"spawn\":true}]"))));
    }
}
