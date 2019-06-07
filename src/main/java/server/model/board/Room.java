package server.model.board;

import java.util.HashSet;
import java.util.Set;

/**
 * <code>Room</code> aggregates all squares belonging to a single room.
 * <p>
 * It provides methods to returns its squares.
 * <p>
 * It implements the <code>Targettable</code> interface for damage distribution.
 */
public class Room implements Targettable {
    private final Set<AbstractSquare> squares = new HashSet<>();

    /**
     * Adds the passed square to this room's squares
     *
     * @param square the square to be added to this room
     */
    public void addSquare(AbstractSquare square) {
        squares.add(square);
    }

    /**
     * Returns this room's squares.
     *
     * @return this room's squares
     */
    public Set<AbstractSquare> getSquares() {
        return squares;
    }

    /**
     * Delegates damage dealing from this target to all smaller targets
     * contained within this.
     *
     * @param dealer the figure that has dealt the damage
     * @param n      the amount of damage given
     */
    @Override
    public void damageFrom(Figure dealer, int n) {
        for (AbstractSquare s : squares)
            s.damageFrom(dealer, n);
    }

    /**
     * Delegates marks assigning from this target to all smaller targets
     * contained within this.
     *
     * @param dealer the figure that has dealt the damage
     * @param n      the amount of damage given
     */
    @Override
    public void markFrom(Figure dealer, int n) {
        for (AbstractSquare s : squares)
            s.markFrom(dealer, n);
    }
}
