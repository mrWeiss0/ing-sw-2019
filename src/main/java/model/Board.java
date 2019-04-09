package model;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>Board</code> represents the game board and its <code>AbstractSquares</code>.
 * It contains a list of squares and methods to refill the board using the
 * visitor pattern.
 */
public class Board {

    private List<AbstractSquare> squares;
    private List<SquareSpawn> spawnpoints; //Probably not needed
    private List<AbstractSquare> toRefill;

    /**
     * sole constructor
     *
     * @param squares the list of squares constituting the board
     */
    public Board(List<AbstractSquare> squares) { //TODO json library needed and how to create the board using JSON
        this.squares = squares;
        toRefill = new ArrayList<>();
    }

    /**
     * Passes the <code>Game</code> to the squares that need refill, so they
     * may ask for a refill. It then cleans the refill list.
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
     * Adds the square passed to the refill list. The refill list
     * is the list of squares that need refilling, usually after
     * a <code>grab</code>.
     *
     * @param a the square to add. Should have
     *          been previously grabbed.
     */
    public void addToRefill(AbstractSquare a) {
        toRefill.add(a);
    }

}
