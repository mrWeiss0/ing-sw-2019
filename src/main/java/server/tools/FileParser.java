package server.tools;

import com.google.gson.Gson;
import server.model.AmmoTileImage;
import server.model.PowerUpImage;
import server.model.board.SquareImage;

import java.io.Reader;

public final class FileParser {
    private static final Gson gson = new Gson();

    private FileParser() {
    }

    public static SquareImage[] readSquares(Reader reader) {
        return gson.fromJson(reader, SquareImage[].class);
    }

    public static AmmoTileImage[] readAmmoTiles(Reader reader) {
        return gson.fromJson(reader, AmmoTileImage[].class);
    }

    public static PowerUpImage[] readPowerUps(Reader reader) {
        return gson.fromJson(reader, PowerUpImage[].class);
    }
}