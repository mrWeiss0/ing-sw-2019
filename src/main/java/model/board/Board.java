package model.board;

import java.util.HashSet;
import java.util.Set;

/**
 * <code>Board</code> represents the game board and its square.
 * <p>
 * It provides methods to allow refilling using the visitor pattern.
 */
public class Board {
    private Set<Room> rooms = new HashSet<>();
    private Set<AbstractSquare> squares = new HashSet<>();
    private Set<Figure> figures = new HashSet<>();

    public void addSquare(AbstractSquare square) {
        rooms.add(square.getRoom());
        squares.add(square);
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public Set<AbstractSquare> getSquares() {
        return squares;
    }

    public Set<Figure> getFigures() {
        return figures;
    }

}
