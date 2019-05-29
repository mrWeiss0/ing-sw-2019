package server.model.board;

import server.model.Game;
import server.model.Grabbable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final Room room;
    private final Set<AbstractSquare> adjacent = new HashSet<>();
    private final Set<Figure> occupants = new HashSet<>();
    private final int[] coordinates;

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
     * the server.connection is mutual.
     *
     * @param square the square setAdjacent to this
     */
    public void connect(AbstractSquare square) {
        adjacent.add(square);
        square.adjacent.add(this);
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

    public Set<AbstractSquare> atDistance(int maxDistance) {
        return distanceMap(maxDistance).keySet();
    }

    public Set<AbstractSquare> atDistance(int minDistance, int maxDistance) {
        return distanceMap(maxDistance).entrySet().stream().filter(e -> e.getValue() >= minDistance).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    private Stream<Room> visibleRoomsStream() {
        return Stream.concat(adjacent.stream().map(AbstractSquare::getRoom), Stream.of(room));
    }

    private Stream<AbstractSquare> visibleSquaresStream() {
        return visibleRoomsStream().map(Room::getSquares).flatMap(Set::stream);
    }

    public Set<Targettable> visibleRooms() {
        return visibleRoomsStream().collect(Collectors.toSet());
    }

    public Set<Targettable> visibleSquares() {
        return visibleSquaresStream().collect(Collectors.toSet());
    }

    public Set<Targettable> visibleFigures() {
        return visibleSquaresStream().map(AbstractSquare::getOccupants).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @Override
    public void damageFrom(Figure dealer, int n) {
        for (Figure s : occupants) s.damageFrom(dealer, n);
    }

    @Override
    public void markFrom(Figure dealer, int n) {
        for (Figure s : occupants) s.markFrom(dealer, n);
    }

    public abstract void accept(Game game);

    public abstract void refill(Grabbable o);

    public abstract void grab(Figure grabber, Grabbable grabbed);

    public abstract Set<Grabbable> peek();
}
