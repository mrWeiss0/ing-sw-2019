package model;

import java.util.*;
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
public abstract class AbstractSquare implements Targettable {
    private Room room;
    private Set<AbstractSquare> adjacent;
    private Set<Figure> occupants;
    // TODO Add [ X, Y ] coordinates for both display and check cardinal?

    /**
     * sole constructor
     *
     * @param room      the <code>Room</code> this square belongs to
     */
    public AbstractSquare(Room room) {
        this.room = room;
        room.addSquare(this);
        adjacent = new HashSet<>();
        occupants = new HashSet<>();
    }

    /**
     * Adds the passed square to the set of adjacency and ensures that
     * the connection is mutual.
     *
     * @param square    the square that is added and that this square
     *                  will be added to.
     */
    public void connect(AbstractSquare square) {
        adjacent.add(square);
        if (!square.getAdjacent().contains(this))
            square.connect(this);
    }

    public abstract void accept(Game game); // TODO Rename accept () to fill()?

    public abstract void grab(Figure grabber);

    /**
     * Gets the <code>Room</code> this square is part of.
     * Every square should belong to a single room.
     *
     * @return the room the square is in
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
    public boolean sees(Room target) {
        return target == room || adjacent.stream().map(AbstractSquare::getRoom).anyMatch(Predicate.isEqual(target));
    }

    /**
     * Checks if the passed <code>AbstractSquare</code> is seen by this square,
     * whether it's in the same room or in a room seen by it.
     *
     * @param target    the square checked for visibility
     * @return          <code>true</code> if the target is seen by this square;
     *                  <code>false</code> otherwise.
     */
    public boolean sees(AbstractSquare target) {
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
    public boolean sees(Figure target) {
        return sees(target.getSquare());
    }

    public void doDamage(Figure dealer) {
        for (Figure s : occupants) {
            s.doDamage(dealer);
        }
    }

    public void doMark(Figure dealer) {
        for (Figure s : occupants) {
            s.doMark(dealer);
        }
    }

    /**
     * Gets the set of adjacency.
     *
     * @return  the set of adjacency
     */
    public Set<AbstractSquare> getAdjacent() {
        return adjacent;
    }

    /**
     * Calculates number of steps to get from this square to the one given.
     *
     * @param target    the square whose distance from this has to be
     *                  calculated.
     * @return          the distance calculated
     */
    public int distance(AbstractSquare target) {
        ArrayList<AbstractSquare> visited = new ArrayList<>();
        ArrayDeque<AbstractSquare> toVisit = new ArrayDeque<>();
        HashMap<AbstractSquare, Integer> distances = new HashMap<>();
        toVisit.addFirst(this);
        distances.put(this, 0);
        while (!toVisit.isEmpty()) {
            AbstractSquare current = toVisit.removeLast();
            if (current == target) return distances.get(current);
            for (AbstractSquare square : current.getAdjacent())
                if (!visited.contains(square) && !toVisit.contains(square)) {
                    distances.put(square, distances.get(current) + 1);
                    toVisit.addFirst(square);
                }
            visited.add(current);

        }
        return -1;
    }


}
