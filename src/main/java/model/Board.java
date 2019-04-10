package model;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>Board</code> represents the game board and its square.
 * <p>
 * It provides methods to allow refilling using the visitor pattern.
 */
public class Board {

    private List<AbstractSquare> squares;
    private List<SquareSpawn> spawnpoints; //Probably not needed
    private List<AbstractSquare> toRefill;

    /**
     * Constructs a board filled with specified list of squares.
     *
     * @param squares the list of squares constituting the board
     */
    public Board(List<AbstractSquare> squares) { //TODO should it take the entire list of squares?
        this.squares = squares;
        toRefill = new ArrayList<>();
    }

    /**
     * Passes the <code>Game</code> to the squares that need refill, so they
     * may ask for it using the game's decks. It then cleans the refill list.
     *
     * @param game the game the board belongs to
     */
    public void refill(Game game) {
        for (AbstractSquare current : toRefill) {
            current.accept(game);
        }
        toRefill.clear();
    }

    /**
     * Adds the specified square to the refill list. Should be called once a
     * the content of a square has been grabbed.
     *
     * @param a the square to added to the refill list
     */
    public void addToRefill(AbstractSquare a) {
        toRefill.add(a);
    }

}
