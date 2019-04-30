package tools;

import model.AbstractSquare;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {
    private static List<AbstractSquare> mappa;
    private static FileParser fp;

    @BeforeAll
    static void init() {
        fp = new FileParser();
        mappa = fp.initBoard("map1.json");
    }

    @Test
    void testDimension1() {
        assertEquals(11, mappa.size());
    }

    @Test
    void testDistances1() {
        assertEquals(1, mappa.get(0).distance(mappa.get(1)));
        assertEquals(5, mappa.get(0).distance(mappa.get(10)));
    }

    @Test
    void testRoom() {
        assertSame(mappa.get(0).getRoom(), mappa.get(3).getRoom());
        assertSame(mappa.get(1).getRoom(), mappa.get(2).getRoom());
        assertNotSame(mappa.get(0).getRoom(), mappa.get(1).getRoom());
    }

    @Test
    void testNotFound() {
        List<AbstractSquare> board;
        board = fp.initBoard("inexistentmap.json");
        assertTrue(board.isEmpty());
    }

    @Test
    void testIoEx() throws IOException {
        List<AbstractSquare> board;
        File file = File.createTempFile("wrongmap", ".json", new File("src/main/resources/maps/"));
        FileWriter fw = new FileWriter(file);
        fw.write("[\n" +
                "  {\n" +
                "    \"Unknown\": false,\n" +
                "    \"coordinates\": [\n" +
                "      0,\n" +
                "      0\n" +
                "    ],\n" +
                "    \"room\": 0,\n" +
                "    \"adjacent\": [\n" +
                "    ]\n" +
                "  }]");
        fw.close();
        board = fp.initBoard(file.getName());
        assertTrue(board.isEmpty());
        file.deleteOnExit();
    }
}