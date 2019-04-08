package model;

import java.util.List;

public abstract class AbstractSquare implements Targettable{
    private List<AbstractSquare> adjacent;
    private Room room;
    public abstract void accept(Game game);


}
