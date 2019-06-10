package server.model.weapon;

import server.model.board.AbstractSquare;
import server.model.board.Figure;
import server.model.board.Targettable;

import java.util.NoSuchElementException;

public class Effects {

    public static Effect damageCurr(int n) {
        return (shooter, currentTargets, lastTargets) -> currentTargets.forEach(x -> x.damageFrom(shooter, n));
    }

    public static Effect markCurr(int n) {
        return (shooter, currentTargets, lastTargets) -> currentTargets.forEach(x -> x.markFrom(shooter, n));
    }

    public static Effect damageSameSquare(int n) {
        return (shooter, currentTargets, lastTargets) -> shooter.getLocation().damageFrom(shooter, n);
    }

    public static Effect addCurrToLast() {
        return (shooter, currentTargets, lastTargets) -> lastTargets.addAll(currentTargets);
    }

    public static Effect moveCurrToLast() {
        return (shooter, currentTargets, lastTargets) -> currentTargets.forEach(x -> ((Figure) x).moveTo((AbstractSquare) lastTargets.get(0)));
    }

    public static Effect moveLastToCurr() {
        return (shooter, currentTargets, lastTargets) -> {
            try{
                ((Figure)lastTargets.get(lastTargets.size()-1))
                        .moveTo(((AbstractSquare) currentTargets.iterator().next()));
            }catch (NoSuchElementException  ignore){}
        };
    }

    public static Effect moveFirstLastToCurr(){
        return (shooter, currentTargets, lastTargets) -> {
            try {
                ((Figure) lastTargets.get(0))
                        .moveTo(((AbstractSquare) currentTargets.iterator().next()));
            }catch (NoSuchElementException ignore){
            }
        };
    }

    public static Effect addShooterSquareToLast() {
        return (shooter, currentTargets, lastTargets) -> lastTargets.add(shooter.getLocation());
    }

    public static Effect moveToCurr() {
        return (shooter, currentTargets, lastTargets) -> {
            try {
                shooter.moveTo((AbstractSquare) currentTargets.iterator().next());
            }catch(NoSuchElementException ignore){
            }
            };
    }

    public static Effect moveToLast(){
        return (shooter, currentTargets, lastTargets) -> {
            try {
                shooter.moveTo((AbstractSquare) lastTargets.get(0));
            }catch(IndexOutOfBoundsException ignore){}
        };
    }

    public static Effect damageLast(int n) {
        return (shooter, currentTargets, lastTargets) -> lastTargets.forEach(x -> x.damageFrom(shooter, n));
    }

    public static Effect markLast(int n) {
        return (shooter, currentTargets, lastTargets) -> lastTargets.forEach(x -> x.markFrom(shooter, n));
    }

    public static Effect addCurrFigureSquareToLast() {
        return (shooter, currentTargets, lastTargets) -> lastTargets
                .add(((Figure) currentTargets.iterator().next()).getLocation());
    }

    public static Effect clearLast() {
        return (shooter, currentTargets, lastTargets) -> lastTargets.clear();
    }

    public static Effect fillLastToSize(int n){
        return (shooter, currentTargets, lastTargets) -> {
            while(lastTargets.size()<n) lastTargets.add(null);
        };
    }

    public static Effect damageOther(int n){
        return (shooter, curr, last) -> {
            try {
                Targettable current = curr.iterator().next();
                current.damageFrom(shooter, 1);
                last.set(last.indexOf(current), null);
                last.add(current);
            } catch (NoSuchElementException ignore) {
            }
        };
    }

    public static Effect damageNeighbours(int n){
        return (shooter, currentTargets, lastTargets) -> shooter.getLocation().atDistance(1,1).forEach(x->x.damageFrom(shooter,n));
    }


}

