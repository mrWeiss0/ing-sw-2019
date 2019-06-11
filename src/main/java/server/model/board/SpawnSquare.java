package server.model.board;

import server.model.Game;
import server.model.Grabbable;
import server.model.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The <code>SpawnSquare</code> class extends <code>AbstractSquare</code> and
 * implements its abstract methods for the refilling of weapons and handling of
 * grabbed content.
 */
public class SpawnSquare extends AbstractSquare {
    private final int capacity;
    private final List<Weapon> weapons = new ArrayList<>();

    /**
     * Constructs an empty square that is contained in the specified room,
     * adding this to its square set; also sets this square's max weapon
     * capacity to the given one,
     *
     * @param room        the room this square belongs to
     * @param coordinates this square coordinates
     * @param capacity    this square's max weapon capacity
     */
    public SpawnSquare(Room room, int[] coordinates, int capacity) {
        super(room, coordinates);
        this.capacity = capacity;
    }

    /**
     * Asks the specified <code>Game</code> for this square to be refilled.
     *
     * @param game the game to ask for a refilling to
     */
    @Override
    public void accept(Game game) {
        try {
            while (weapons.size() < capacity)
                game.fillSquare(this);
        } catch (NoSuchElementException ignore) {
            // Weapon dek is empty
        }
    }

    /**
     * Refills this square with ammo from a specified <code>Grabbable</code>,
     * which actual type should be <code>Weapon</code>.
     *
     * @param o the weapon with which to refill this square
     * @throws ClassCastException if the specified <code>Grabbable</code>
     *                            actual type isn't <code>Weapon</code>
     */
    @Override
    public void refill(Grabbable o) {
        Weapon w = (Weapon) o;
        if (weapons.size() >= capacity)
            throw new IllegalStateException("Square is already filled");
        weapons.add(w);
    }

    /**
     * Gives the grabber the specified <code>Weapon</code>, who should
     * always be one of this square's weapons
     *
     * @param grabber the figure grabbing the content
     * @param grabbed the content to be grabbed
     * @throws ClassCastException    if the specified <code>Grabbable</code>
     *                               actual type isn't <code>Weapon</code>
     * @throws IllegalStateException when the specified <code>AmmoTile</code>
     *                               isn' t one of this square's weapons
     */
    @Override
    public void grab(Figure grabber, Grabbable grabbed) {
        Weapon w = (Weapon) grabbed;
        if (!weapons.remove(w))
            throw new IllegalStateException("Square " + this + " does not contain item " + w);
        grabber.grab(w);
    }

    /**
     * Returns a <code>Grabbable</code> set containing this square's
     * <code>Weapon</code>s.
     *
     * @return this square's weapon set
     */
    @Override
    public List<Grabbable> peek() {
        return new ArrayList<>(weapons);
    }
}
