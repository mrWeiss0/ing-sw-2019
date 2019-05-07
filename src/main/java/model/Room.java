package model;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>Room</code> aggregates all squares belonging to a single room.
 * <p>
 * It implements the <code>Targettable</code> and its chain of responsibility,
 * with its <code>dealDamage</code> and <code>dealMark</code> methods.
 */
public class Room implements Targettable {
    private List<AbstractSquare> squares = new ArrayList<>();

    /**
     * Adds the passed square to this room's squares
     *
     * @param square the square to be added to this room
     */
    public void addSquare(AbstractSquare square) {
        squares.add(square);
    }

    @Override
    public void damageFrom(Figure dealer, int n) {
        for (AbstractSquare s : squares) s.damageFrom(dealer, n);
    }

    @Override
    public void markFrom(Figure dealer, int n) {
        for (AbstractSquare s : squares) s.markFrom(dealer, n);
    }
}
