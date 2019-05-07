package tools;

import com.google.gson.Gson;
import model.board.Board;

import java.io.Reader;


public class FileParser {
    private static Gson gson = new Gson();

    private FileParser() {
    }

    public static Board buildBoard(Reader mapFile) throws MalformedDataException {
        return new BoardBuilder(gson.fromJson(mapFile, JsonSquare[].class)).build();
    }
}
