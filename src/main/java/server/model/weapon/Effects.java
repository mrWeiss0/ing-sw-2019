package server.model.weapon;

import server.model.board.AbstractSquare;
import server.model.board.Figure;

public class Effects {

    public static Effect damageCurr(int n){
        return (shooter, currentTargets, lastTargets) -> currentTargets.forEach(x->x.damageFrom(shooter,n));
    }

    public static Effect markCurr(int n){
        return (shooter, currentTargets, lastTargets) -> currentTargets.forEach(x->x.markFrom(shooter,n));
    }

    public static Effect damageSameSquare(int n){
        return (shooter, currentTargets, lastTargets) -> shooter.getLocation().damageFrom(shooter, n);
    }

    public static Effect addCurrToLast(){
        return (shooter, currentTargets, lastTargets) -> lastTargets.addAll(currentTargets);
    }

    public static Effect moveCurrToLast(){
        return (shooter, currentTargets, lastTargets) -> currentTargets.forEach(x->((Figure)x).moveTo((AbstractSquare) lastTargets.get(0)));
    }
}
