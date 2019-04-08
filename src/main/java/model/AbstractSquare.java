package model;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class AbstractSquare implements Targettable{
    private List<AbstractSquare> adjacent;
    private Room room;
    private Set<Figure> occupants;
    public abstract void accept(Game game);
    public abstract void grab(Figure grabber);

    public Room getRoom() {
        return room;
    }

    public boolean sees(Room target){
        return target==room || adjacent.stream().map(AbstractSquare::getRoom).anyMatch(Predicate.isEqual(target)) ;
    }
    public boolean sees(AbstractSquare target){
        return sees(target.getRoom());
    }
    public boolean sees(Figure target){
        return sees(target.getSquare());
    }


}
