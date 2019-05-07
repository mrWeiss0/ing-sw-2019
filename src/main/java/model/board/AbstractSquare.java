package model.board;

import model.Game;
import model.Grabbable;

import java.util.*;
import java.util.function.Predicate;

/**
 * <code>AbstractSquares</code> is an abstract base class for any square on the
 * game board.
 * <p>
 * Each <code>AbstractSquare</code> provides attributes and methods to set and
 * return its room, coordinates, adjacency set and its occupants.
 * <p>
 * Its abstract methods require that different squares handle their refilling
 * needed contents, or to return the correct contents when grabbed.
 * <p>
 * It provides methods to determine which <code>Targettable</code>s it can see.
 * <p>
 * It implements the <code>Targettable</code> and its chain of responsibility,
 * with its <code>damageFrom</code> and <code>markFrom</code> methods.
 */
public abstract class AbstractSquare implements Targettable {
    private Room room;
    private Set<AbstractSquare> adjacent = new HashSet<>();
    private Set<Figure> occupants = new HashSet<>();
    private int[] coordinates;

    /**
     * Constructs an empty square belonging to the specified room, automatically
     * adding this to its square set.
     *
     * @param room        the room this square belongs to
     * @param coordinates the square coordinates
     */
    public AbstractSquare(Room room, int[] coordinates) {
        this.room = room;
        room.addSquare(this);
        this.coordinates = coordinates;
    }

    /**
     * Adds the specified square to the set of adjacency and ensures that
     * the connection is mutual.
     *
     * @param square the square adjacent to this
     */
    public void connect(AbstractSquare square) {
        adjacent.add(square);
        if (!square.getAdjacent().contains(this))
            square.connect(this);
    }

    /**
     * Calculates number of steps to get from this square to the one specified.
     *
     * @param target the square whose distance from this is to be calculated
     * @return the distance calculated
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
            for (AbstractSquare square : current.adjacent)
                if (!visited.contains(square) && !toVisit.contains(square)) {
                    distances.put(square, distances.get(current) + 1);
                    toVisit.addFirst(square);
                }
            visited.add(current);

        }
        return -1;
    }

    /**
     * Returns the room this square is part of.
     *
     * @return the room this square is part of
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Returns the coordinates of this square.
     *
     * @return the coordinates of this square
     */
    public int[] getCoordinates() {
        return coordinates;
    }

    public Set<AbstractSquare> getAdjacent() {
        return adjacent;
    }

    /**
     * Returns the set of figures that are currently on this square.
     *
     * @return the set of figure on the square
     */
    public Set<Figure> getOccupants() {
        return occupants;
    }

    /**
     * Adds the specified figure to the list of occupants. Should be called
     * only when a figure is moved to this square.
     *
     * @param figure the figure to be moved to this square
     */
    public void addOccupant(Figure figure) {
        occupants.add(figure);
    }

    /**
     * Removes the specified figure to the list of occupants. Should be called
     * only when a figure is moved from this square.
     *
     * @param figure the figure to be moved from this square
     */
    public void removeOccupant(Figure figure) {
        occupants.remove(figure);
    }

    /**
     * Checks if the specified room is seen by this square, whether it's the
     * one containing it or it's seen by it.
     *
     * @param target the room to be checked for visibility
     * @return <code>true</code> if the target is seen by this square;
     * <code>false</code> otherwise
     */
    public boolean sees(Room target) {
        return target == room || adjacent.stream().map(AbstractSquare::getRoom).anyMatch(Predicate.isEqual(target));
    }

    /**
     * Checks if the specified square is seen by this square, whether
     * it's in the same room or in a room seen by it.
     *
     * @param target the square to be checked for visibility
     * @return <code>true</code> if the target is seen by this square;
     * <code>false</code> otherwise.
     */
    public boolean sees(AbstractSquare target) {
        return sees(target.getRoom());
    }

    /**
     * Checks if the specified <code>Figure</code> is seen by this square,
     * whether it's in the square itself or in a square seen by it.
     *
     * @param target the square to checked for visibility
     * @return <code>true</code> if the target is seen by this square;
     * <code>false</code> otherwise.
     */
    public boolean sees(Figure target) {
        return sees(target.getSquare());
    }

    @Override
    public void damageFrom(Figure dealer, int n) {
        for (Figure s : occupants) s.damageFrom(dealer, n);
    }

    @Override
    public void markFrom(Figure dealer, int n) {
        for (Figure s : occupants) s.markFrom(dealer, n);
    }

    public abstract boolean accept(Game game);

    public abstract boolean refill(Grabbable o);

    public abstract void grab(Figure grabber, Grabbable grabbed);

    public abstract Set<Grabbable> peek();
}
