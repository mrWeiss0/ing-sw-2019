package model.board;

import model.Game;
import model.Grabbable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <code>AbstractSquares</code> is an abstract base class for any square on the
 * game board.
 * <p>
 * It provides methods to set and return a square's room, coordinates; it also
 * provides methods to keep track of figures moving in and out of this square.
 * <p>
 * Its abstract methods require that different squares handle the refilling of
 * needed contents, and to correctly handle contents when grabbed.
 * <p>
 * It provides methods to determine which <code>Targettable</code>s a square
 * can see and, given a maximum distance, which squares are within reach.
 * <p>
 * It implements the <code>Targettable</code> interface for damage distribution.
 */
public abstract class AbstractSquare implements Targettable {
    private final Room room;
    private final Set<AbstractSquare> adjacent = new HashSet<>();
    private final Set<Figure> occupants = new HashSet<>();
    private final int[] coordinates;

    /**
     * Constructs an empty square that is contained in the specified room,
     * adding this to its square set.
     *
     * @param room        the room this square belongs to
     * @param coordinates this square coordinates
     */
    public AbstractSquare(Room room, int[] coordinates) {
        this.room = room;
        room.addSquare(this);
        this.coordinates = coordinates;
    }

    /**
     * Adds the specified square to this square's adjacency set and ensures
     * that the connection is mutual.
     *
     * @param square the square setAdjacent to this
     */
    public void connect(AbstractSquare square) {
        adjacent.add(square);
        square.adjacent.add(this);
    }

    /**
     * Adds the specified figure to this square's occupants list.
     *
     * @param figure the figure to be moved to this square
     */
    public void addOccupant(Figure figure) {
        occupants.add(figure);
    }

    /**
     * Removes the specified figure to this square's occupants list.
     *
     * @param figure the figure to be moved from this square
     */
    public void removeOccupant(Figure figure) {
        occupants.remove(figure);
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

    /**
     * Returns this square's adjacency set
     * .
     * @return this square's adjacency set
     */
    public Set<AbstractSquare> getAdjacent() {
        return adjacent;
    }

    /**
     * Returns the set of figures that are currently on this square.
     *
     * @return the set of figure on this square
     */
    public Set<Figure> getOccupants() {
        return occupants;
    }

    private Map<AbstractSquare, Integer> distanceMap(int maxDistance) {
        Queue<AbstractSquare> queue = new LinkedList<>();
        Map<AbstractSquare, Integer> distances = new HashMap<>();
        AbstractSquare next;
        List<AbstractSquare> adj;

        queue.add(this);
        distances.put(this, 0);
        while ((next = queue.poll()) != null) {
            Integer dist = distances.get(next);
            if (maxDistance < 0 || dist < maxDistance) {
                adj = next.getAdjacent().stream().filter(s -> !distances.containsKey(s)).collect(Collectors.toList());
                adj.forEach(s -> distances.put(s, dist + 1));
                queue.addAll(adj);
            }
        }
        return distances;
    }

    /**
     * Returns a set of squares whose distance from this is, at most, the one given.
     *
     * @param maxDistance the maximum distance at which a returned square can be.
     *                    The special value "-1" corresponds to an infinite
     *                    maximum distance.
     * @return a set of square whose distance from this is, at most, the one given
     */
    public Set<AbstractSquare> atDistance(int maxDistance) {
        return distanceMap(maxDistance).keySet();
    }

    /**
     * Returns a set of squares whose distance from this is in the given
     * boundaries.
     *
     * @param minDistance the minimum distance at which a returned square can be
     * @param maxDistance the maximum distance at which a returned square can be.
     *                    The special value "-1" corresponds to an infinite
     *                    maximum distance.
     * @return a set of squares whose distance from this is in the given
     * boundaries.
     */
    public Set<AbstractSquare> atDistance(int minDistance, int maxDistance) {
        return distanceMap(maxDistance).entrySet().stream().filter(e -> e.getValue() >= minDistance).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    private Stream<Room> visibleRoomsStream() {
        return Stream.concat(adjacent.stream().map(AbstractSquare::getRoom), Stream.of(room));
    }

    private Stream<AbstractSquare> visibleSquaresStream() {
        return visibleRoomsStream().map(Room::getSquares).flatMap(Set::stream);
    }

    /**
     * Returns a set of rooms visible from this square. A room may be
     * visible either from squares contained within the room itself or
     * adjacent to them.
     *
     * @return a set of rooms visible from this square
     */
    public Set<Targettable> visibleRooms() {
        return visibleRoomsStream().collect(Collectors.toSet());
    }

    /**
     * Returns a set of squares visible from this one. A square may be
     * visible from another when contained in a visible room.
     *
     * @return a set of squares visible from this one
     */
    public Set<Targettable> visibleSquares() {
        return visibleSquaresStream().collect(Collectors.toSet());
    }

    /**
     * Returns a set of figures visible from this square. A figure may be
     * visible from a square when occupying a visible square.
     *
     * @return a set of figures visible from this square
     */
    public Set<Targettable> visibleFigures() {
        return visibleSquaresStream().map(AbstractSquare::getOccupants).flatMap(Collection::stream).collect(Collectors.toSet());
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
        for (Figure s : occupants) s.damageFrom(dealer, n);
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
        for (Figure s : occupants) s.markFrom(dealer, n);
    }

    /**
     * Asks the specified <code>Game</code> for this square to be refilled.
     *
     * @param game the game to ask for a refilling to
     */
    public abstract void accept(Game game);

    /**
     * Refills this square with content from a specified
     * <code>Grabbable</code>.
     *
     * @param o the content with which to refill this square
     */
    public abstract void refill(Grabbable o);

    /**
     * Gives the grabber the specified <code>Grabbable</code> content, if
     * present in this square.
     *
     * @param grabber the figure grabbing the content
     * @param grabbed the content to be grabbed
     */
    public abstract void grab(Figure grabber, Grabbable grabbed);

    /**
     * Returns a set of <code>Grabbable</code>s containing this square's
     * <code>Grabbable</code> content.
     *
     * @return a <code>Grabbable</code>s containing this square's
     * <code>Grabbable</code> content
     */
    public abstract Set<Grabbable> peek();
}
