package model;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * <code>AbstractSquares</code> is an abstract base class for any square on the
 * gameboard.
 * Its abstract methods allow different squares to be refilled with their
 * needed contents, or to return the correct contents when grabbed.
 * It contains methods and attributes to determine which <code>Targettable</code>s
 * can be seen and which <code>AbstractSquare</code>s are adjacent.
 * It implements the <code>Targettable</code> and its chain of responsibility,
 * with its <code>dealDamage</code> and <code>dealMark</code> methods.
 */
public abstract class AbstractSquare implements Targettable{

    private List<AbstractSquare> adjacent;
    private Room room;
    private Set<Figure> occupants;
    public abstract void accept(Game game);
    public abstract void grab(Figure grabber);

    /**
     * Gets the <code>Room</code> this square is part of.
     * Every square should belong to a single room.
     *
     * @return  the room the square is in
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Checks if the passed <code>Room</code> is seen by this square,
     * whether it's the one containing it or it's seen by it.
     *
     * @param target    the room checked for visibility
     * @return          <code>true</code> if the target is seen by this square;
     *                  <code>false</code> otherwise.
     */
    public boolean sees(Room target){
        return target==room || adjacent.stream().map(AbstractSquare::getRoom).anyMatch(Predicate.isEqual(target)) ;
    }

    /**
     * Checks if the passed <code>AbstractSquare</code> is seen by this square,
     * whether it's in the same room or in a room seen by it.
     *
     * @param target    the square checked for visibility
     * @return          <code>true</code> if the target is seen by this square;
     *                  <code>false</code> otherwise.
     */
    public boolean sees(AbstractSquare target){
        return sees(target.getRoom());
    }

    /**
     * Checks if the passed <code>Figure</code> is seen by this square,
     * whether it's in the square itself or in a square seen by it.
     *
     * @param target    the square checked for visibility
     * @return          <code>true</code> if the target is seen by this square;
     *                  <code>false</code> otherwise.
     */
    public boolean sees(Figure target){
        return sees(target.getSquare());
    }


}
