package model.board;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<Figure> getDamaged(){
        return figures.stream().filter(Figure::isDamaged).collect(Collectors.toSet());
    }

    public void clearDamaged(){
        figures.stream().forEach(Figure::clearDamaged);
    }

    public void applyMarks(){
        figures.stream().forEach(Figure::applyMarks);
    }

}
