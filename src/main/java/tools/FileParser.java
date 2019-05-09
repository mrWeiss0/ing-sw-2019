package tools;

import com.google.gson.Gson;
import model.board.SquareImage;

import java.io.Reader;


public final class FileParser {
    private static final Gson gson = new Gson();

    private FileParser() {
    }

    public static SquareImage[] readSquares(Reader mapReader) {
        return gson.fromJson(mapReader, SquareImage[].class);
    }
}
