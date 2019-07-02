package server.model.board;

import server.model.AmmoTile;
import server.model.Game;
import server.model.Grabbable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The <code>AmmoSquare</code> class extends <code>AbstractSquare</code> and
 * implements its abstract methods for the refilling of ammo and handling of
 * grabbed content.
 */
public class AmmoSquare extends AbstractSquare {
    private AmmoTile ammoTile = null;

    /**
     * Constructs an empty square that is contained in the specified room,
     * adding this to its square set.
     *
     * @param room        the room this square belongs to
     * @param coordinates this square coordinates
     */
    public AmmoSquare(Room room, int[] coordinates) {
        super(room, coordinates);
    }


    /**
     * Asks the specified <code>Game</code> for this square to be refilled.
     *
     * @param game the game to ask for a refilling to
     */
    @Override
    public void accept(Game game) {
        if (ammoTile == null)
            game.fillSquare(this);
    }

    /**
     * Refills this square with ammo from a specified <code>Grabbable</code>,
     * which actual type should be <code>AmmoTile</code>.
     *
     * @param o the ammo with which to refill this square
     * @throws ClassCastException if the specified <code>Grabbable</code>
     *                            actual type isn't <code>AmmoTile</code>
     */
    @Override
    public void refill(Grabbable o) {
        AmmoTile t = (AmmoTile) o;
        if (ammoTile != null)
            throw new IllegalStateException("Square is already filled");
        ammoTile = t;

    }

    /**
     * Gives the grabber the specified <code>AmmoTile</code>, who should
     * always be this square's ammo.
     *
     * @param grabber the figure grabbing the content
     * @param grabbed the content to be grabbed
     * @throws ClassCastException    if the specified <code>Grabbable</code>
     *                               actual type isn't <code>AmmoTile</code>
     * @throws IllegalStateException when the specified <code>AmmoTile</code>
     *                               isn' t this square's ammo
     */
    @Override
    public void grab(Figure grabber, Grabbable grabbed) {
        AmmoTile t = (AmmoTile) grabbed;
        if (ammoTile != t)
            throw new IllegalStateException("Square " + this + " does not contain item " + t);
        grabber.grab(t);
        ammoTile = null;
    }

    /**
     * Returns a <code>Grabbable</code> set containing this square's <code>AmmoTile</code>.
     *
     * @return this square's <code>AmmoTile</code>
     */
    @Override
    public List<Grabbable> peek() {
        return Optional.ofNullable(ammoTile).map(Stream::of).orElseGet(Stream::empty).collect(Collectors.toList());
    }
}
