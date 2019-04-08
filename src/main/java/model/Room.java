package model;

import java.util.ArrayList;
import java.util.List;

public class Room implements Targettable{
    private List<AbstractSquare> squares;
    public Room(){
        squares=new ArrayList<>();
    }
    public void addSquare(AbstractSquare a){
        squares.add(a);
    }
    public void doDamage(Figure dealer){
        for(AbstractSquare s:squares){
            s.doDamage(dealer);
        }
    }
    public void doMark(Figure dealer){
        for(AbstractSquare s:squares){
            s.doMark(dealer);
        }
    }
}
