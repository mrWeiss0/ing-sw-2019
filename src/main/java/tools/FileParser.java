package tools;

import client.model.Config;
import com.google.gson.Gson;
import server.model.AmmoTileImage;
import server.model.PowerUpImage;
import server.model.board.SquareImage;

import java.io.Reader;

/**
 * <code>FileParser</code> is a <code>final</code> utility class used to parse
 * files from which to generate a game's squares, AmmoTiles and PowerUps.
 * <p>
 * As all its methods are static, the class doesn't provide a public
 * constructor and no instances of this class may be created.
 */
public final class FileParser {
    private static final Gson gson = new Gson();

    private FileParser() {
    }

    /**
     * Returns an array of Square Images from which the game's square are to be
     * constructed.
     *
     * @param reader the reader that provides a stream from which to generate
     *               the squares
     * @return the Square Images from which the game's square are to be
     * constructed
     */
    public static SquareImage[] readSquares(Reader reader) {
        return gson.fromJson(reader, SquareImage[].class);
    }

    /**
     * Returns an array of AmmoTile Images from which the game's square are
     * to be constructed.
     *
     * @param reader the reader that provides a stream from which to generate
     *               the AmmoTile
     * @return the AmmoTile Images from which the game's square are to be
     * constructed
     */
    public static AmmoTileImage[] readAmmoTiles(Reader reader) {
        return gson.fromJson(reader, AmmoTileImage[].class);
    }

    /**
     * Returns an array of PowerUp Images from which the game's square are
     * to be constructed.
     *
     * @param reader the reader that provides a stream from which to generate
     *               the PowerUp
     * @return the PowerUp Images from which the game's square are to be
     * constructed
     */
    public static PowerUpImage[] readPowerUps(Reader reader) {
        return gson.fromJson(reader, PowerUpImage[].class);
    }

    public static Config readClientConfig(Reader reader) {
        return gson.fromJson(reader, Config.class);
    }

    public static server.Config readServerConfig(Reader reader) {
        return gson.fromJson(reader, server.Config.class);
    }
}
