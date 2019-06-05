package server.model.weapon;

import server.model.board.AbstractSquare;
import server.model.board.Figure;

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
        return (shooter, currentTargets, lastTargets) -> lastTargets.forEach(x -> ((Figure) x).moveTo((AbstractSquare) lastTargets.get(0)));
    }

    public static Effect addShooterSquareToLast() {
        return (shooter, currentTargets, lastTargets) -> lastTargets.add(shooter.getLocation());
    }

    public static Effect moveToCurr() {
        return (shooter, currentTargets, lastTargets) -> shooter.moveTo((AbstractSquare) currentTargets.iterator().next());
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
}

