package model;

import java.util.*;
import java.util.function.Predicate;


public abstract class AbstractSquare implements Targettable {
    private Room room;
    private Set<AbstractSquare> adjacent;
    private Set<Figure> occupants;
    // TODO Add [ X, Y ] coordinates for both display and check cardinal?

    public AbstractSquare(Room room) {
        this.room = room;
        room.addSquare(this);
        adjacent = new HashSet<>();
        occupants = new HashSet<>();
    }

    public void connect(AbstractSquare square) {
        adjacent.add(square);
        if (!square.getAdjacent().contains(this))
            square.connect(this);
    }

    public abstract void accept(Game game); // TODO Rename accept () to fill()?

    public abstract void grab(Figure grabber);

    public Room getRoom() {
        return room;
    }

    public boolean sees(Room target) {
        return target == room || adjacent.stream().map(AbstractSquare::getRoom).anyMatch(Predicate.isEqual(target));
    }

    public boolean sees(AbstractSquare target) {
        return sees(target.getRoom());
    }

    public boolean sees(Figure target) {
        return sees(target.getSquare());
    }

    public void doDamage(Figure dealer) {
        for (Figure s : occupants) {
            s.doDamage(dealer);
        }
    }

    public void doMark(Figure dealer) {
        for (Figure s : occupants) {
            s.doMark(dealer);
        }
    }

    public Set<AbstractSquare> getAdjacent() {
        return adjacent;
    }


    public int distance(AbstractSquare target){
        ArrayList<AbstractSquare> visited=new ArrayList<>();
        ArrayDeque<AbstractSquare> toVisit= new ArrayDeque<>();
        HashMap<AbstractSquare,Integer> distances= new HashMap<>();
        toVisit.push(this);
        distances.put(this,0);

        while(!toVisit.isEmpty()){
            AbstractSquare current=toVisit.pop();
            if(current==target) return distances.get(current);
            for(AbstractSquare square:current.getAdjacent())
                if(!visited.contains(square)){
                    distances.put(square,distances.get(current)+1);
                    toVisit.add(square);
                }

            visited.add(current);
        }
        return -1;
    }
}
