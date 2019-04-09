package model;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractSquare implements Targettable {
    private List<AbstractSquare> adjacent;
    private Room room;
    private Set<Figure> occupants;

    public abstract void accept(Game game);

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

    @Override
    public void doDamage(Figure dealer) {

    }

    @Override
    public void doMark(Figure dealer) {

    }

    public List<AbstractSquare> getAdjacent() {
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
