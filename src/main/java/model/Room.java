package model;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>Room</code> aggregates all <code>AbstractSquare</code> belonging
 * to a single room.
 * It implements the <code>Targettable</code> and its chain of responsibility,
 * with its <code>dealDamage</code> and <code>dealMark</code> methods.
 */
public class Room implements Targettable {
    private List<AbstractSquare> squares;

    /**
     * sole constructor. Readies the room to accept its squares.
     */
    public Room() {
        squares = new ArrayList<>();
    }

    /**
     * Adds the passed <code>AbstractSquare</code> to the squares of this room
     *
     * @param square    the square to be added to this room
     */
    public void addSquare(AbstractSquare square) {
        squares.add(square);
    }

    public void doDamage(Figure dealer) {
        for (AbstractSquare s : squares) s.doDamage(dealer);

    }

    public void doMark(Figure dealer) {
        for (AbstractSquare s : squares) s.doMark(dealer);

    }
}
