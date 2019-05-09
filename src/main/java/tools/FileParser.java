package tools;

import com.google.gson.Gson;
import model.board.Board;

import java.io.Reader;


public final class FileParser {
    private static final Gson gson = new Gson();

    private FileParser() {
    }

    public static Board buildBoard(Reader mapFile) throws MalformedDataException {
        return new BoardBuilder(gson.fromJson(mapFile, JsonSquare[].class)).build();
    }
}
