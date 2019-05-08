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

    public Set<AbstractSquare> atDistanceMax(int distance) {
        Queue<AbstractSquare> queue = new LinkedList<>();
        Set<AbstractSquare> visited = new HashSet<>();
        AbstractSquare next;
        List<AbstractSquare> adj;

        queue.add(this);
        visited.add(this);
        while (distance-- > 0 && (next = queue.poll()) != null) {
            adj = next.getAdjacent().stream().filter(s -> !visited.contains(s)).collect(Collectors.toList());
            queue.addAll(adj);
            visited.addAll(adj);
        }
        return visited;
    }

    private Stream<Room> visibleRoomsStream() {
        return Stream.concat(adjacent.stream().map(AbstractSquare::getRoom), Stream.of(room));
    }

    private Stream<AbstractSquare> visibleSquaresStream() {
        return visibleRoomsStream().map(Room::getSquares).flatMap(Set::stream);
    }

    public Set<Room> visibleRooms() {
        return visibleRoomsStream().collect(Collectors.toSet());
    }

    public Set<AbstractSquare> visibleSquares() {
        return visibleSquaresStream().collect(Collectors.toSet());
    }

    public Set<Figure> visibleFigures() {
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

    public abstract boolean accept(Game game);

    public abstract boolean refill(Grabbable o);

    public abstract void grab(Figure grabber, Grabbable grabbed);

    public abstract Set<Grabbable> peek();
}
