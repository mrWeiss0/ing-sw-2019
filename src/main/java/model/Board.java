package model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<AbstractSquare> squares;
    private List<SquareSpawn> spawnpoints; //Probably not needed
    private List<AbstractSquare> toRefill;

    public Board(List<AbstractSquare> squares) { //TODO json library needed and how to create the board using JSON
        this.squares = squares;
        toRefill = new ArrayList<>();
    }

    public void refill(Game game) {
        for (AbstractSquare current : toRefill) {
            current.accept(game);
        }
        toRefill.clear();
    }

    public void addToRefill(AbstractSquare a) {
        toRefill.add(a);
    }

}
