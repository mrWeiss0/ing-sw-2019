package server.model.weapon;

import server.model.board.AbstractSquare;
import server.model.board.Figure;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public final class TargetGens {
    private TargetGens() {
    }

    /**
     * Returns the <code>TargetGen</code> that generates all visible squares by the shooter
     *
     * @return the TargetGen generating all visible squares by shooter
     */
    public static TargetGen visibleSquares() {
        return (shooter, board, last) -> shooter.getLocation().visibleSquares()
                .stream().filter(t -> t != shooter.getLocation()).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all visible figures by the shooter
     *
     * @return the TargetGen generating all visible figures by shooter
     */
    public static TargetGen visibleFigures() {
        return (shooter, board, last) -> shooter.getLocation().visibleFigures()
                .stream().filter(t -> t != shooter).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all visible rooms by the shooter
     *
     * @return the TargetGen generating all visible rooms by shooter
     */
    public static TargetGen visibleRoom() {
        return (shooter, board, lastTargets) -> shooter.getLocation().visibleRooms();
    }

    /**
     * Returns the <code>TargetGen</code> that generates all figures different from shooter,
     * generally useful in combination with the not() composition
     *
     * @return the TargetGen generating all figures different from shooter
     */
    public static TargetGen differentFigures() {
        return (shooter, board, lastTargets) -> board.getFigures().stream().filter(x -> x != shooter).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all figures present in lastTarget
     *
     * @return the TargetGen generating all figures present in lastTarget
     */
    public static TargetGen inLastFigure() {
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(t -> t != shooter && lastTargets.contains(t)).collect(Collectors.toSet());
    }

    public static TargetGen inLast() {
        return (shooter, board, lastTargets) -> new HashSet<>(lastTargets);
    }

    /**
     * Returns the <code>TargetGen</code> that generates all figures that are on a square
     * present in lastTarget
     *
     * @return the TargetGen generating all figures on a square in lastTargets
     */
    public static TargetGen onLastFigures() {
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x -> lastTargets.contains(x.getLocation())).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all figures that are at
     * a maximum distance of n from shooter
     *
     * @param n the max distance from shooter
     * @return the TargetGen generating all figures at max distance n
     */
    public static TargetGen maxDistanceFigures(int n) {
        return (shooter, board, last) -> board.getFigures()
                .stream().filter(t -> t != shooter && shooter.getLocation().atDistance(n).contains(t.getLocation())).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the squares that
     * are at a maximum distance of n from shooter
     *
     * @param n the max distance from shooter
     * @return the TargetGen generating all squares at max distance n
     */
    public static TargetGen maxDistanceSquares(int n) {
        return (shooter, board, lastTargets) -> new HashSet<>(shooter.getLocation().atDistance(n));
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the figures that are at
     * a maximum distance of n from a Square visible by shooter
     *
     * @param n the max distance from a visible square
     * @return the TargetGen generating all figures at max distance n from a
     * square visible by shooter
     */
    public static TargetGen atDistanceFromVisibleSquareFigures(int n) {
        return (shooter, board, lastTargets) -> board.getFigures().stream()
                .filter(x -> x != shooter && shooter.getLocation().visibleSquares()
                        .stream().anyMatch(y -> ((AbstractSquare) y).atDistance(n).contains(x.getLocation()))).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the figures that
     * are on squares on a cardinal direction from the shooter's square
     *
     * @return the TargetGen generating all figures on a cardinal direction
     */
    public static TargetGen onCardinalFigures() {
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x -> x != shooter && x.getLocation() != null && (x.getLocation().getCoordinates()[0] == shooter.getLocation().getCoordinates()[0] ||
                        x.getLocation().getCoordinates()[1] == shooter.getLocation().getCoordinates()[1])).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the squares
     * on a cardinal direction from the shooter's square
     *
     * @return the TargetGen generating all squares on a cardinal direction
     */
    public static TargetGen onCardinalSquare() {
        return (shooter, board, lastTargets) -> board.getSquares()
                .stream().filter(x -> x.getCoordinates()[0] == shooter.getLocation().getCoordinates()[0] ||
                        x.getCoordinates()[1] == shooter.getLocation().getCoordinates()[1]).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the rooms
     * different from the shooter's one
     *
     * @return the TargetGen generating all rooms different from the shooter's
     * one
     */
    public static TargetGen differentRoom() {
        return (shooter, board, lastTargets) -> board.getRooms()
                .stream().filter(x -> !x.getSquares().contains(shooter.getLocation())).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the rooms
     * different from the shooter's one
     *
     * @return the TargetGen generating all rooms different from the shooter's
     * one
     */
    public static TargetGen differentSquares() {
        return (shooter, board, lastTargets) -> board.getSquares().stream().filter(x -> x != shooter.getLocation()).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the figures
     * that are at a maximum distance of n from the first element of
     * lastTargets
     *
     * @param n the maximum distance of a figure from the last square
     * @return the TargetGen generating all the figures
     * that are at a maximum distance of n from the first element of
     * lastTargets
     */
    public static TargetGen maxDistanceFromLastFigures(int n) {
        return (shooter, board, lastTargets) -> ((AbstractSquare) lastTargets.get(0)).atDistance(n).stream().
                collect(HashSet::new, (x, y) -> x.addAll(y.getOccupants().stream().filter(z -> z != shooter).collect(Collectors.toSet())), Set::addAll);
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the squares
     * that are at a maximum distance of n from the first element of
     * lastTargets
     *
     * @param n the maximum distance of a square from the last square
     * @return the TargetGen generating all the squares
     * that are at a maximum distance of n from the first element of
     * lastTargets
     */
    public static TargetGen maxDistanceFromLastSquares(int n) {
        return (shooter, board, lastTargets) -> new HashSet<>(((AbstractSquare) lastTargets.get(0)).atDistance(n));
    }

    public static TargetGen maxDistanceFromLastFigureSquare(int n) {
        return (shooter, board, lastTargets) -> new HashSet<>(((Figure) lastTargets.get(0)).getLocation().atDistance(n));
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the figures
     * that are visible from the last figure in lastTargets
     *
     * @return the TargetGen generating all the figures
     * that are visible from the last figure in lastTargets
     */
    public static TargetGen visibleFromLastFigures() {
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x -> x != shooter && !lastTargets.contains(x) && ((Figure) lastTargets.get(lastTargets.size() - 1)).getLocation()
                        .visibleSquares().contains(x.getLocation())).collect(Collectors.toSet());
    }

    /**
     * Returns the <code>TargetGen</code> that generates all the squares
     * that are on the same direction of the movement from the first element
     * of lastTargets to the shooter's current square
     *
     * @return the TargetGen generating all the squares
     * that are on the same direction of the movement from the first element
     * of lastTargets to the shooter's current square
     */
    public static TargetGen sameDirectionAsLastSquares() {
        return (shooter, board, lastTargets) -> {
            int[] origin = ((AbstractSquare) lastTargets.get(0)).getCoordinates();
            int[] last = ((AbstractSquare) lastTargets.get(1)).getCoordinates();
            if (origin[0] == last[0] && origin[1] > last[1]) return board.getSquares()
                    .stream().filter(x -> x.getCoordinates()[0] == origin[0] && x.getCoordinates()[1] < origin[1]).collect(Collectors.toSet());
            else if (origin[0] == last[0] && origin[1] < last[1]) return board.getSquares()
                    .stream().filter(x -> x.getCoordinates()[0] == origin[0] && x.getCoordinates()[1] > origin[1]).collect(Collectors.toSet());
            else if (origin[1] == last[1] && origin[0] < last[0]) return board.getSquares()
                    .stream().filter(x -> x.getCoordinates()[1] == origin[1] && x.getCoordinates()[0] > origin[0]).collect(Collectors.toSet());
            else return board.getSquares()
                        .stream().filter(x -> x.getCoordinates()[1] == origin[1] && x.getCoordinates()[0] < origin[0]).collect(Collectors.toSet());
        };
    }

    public static TargetGen sameDirectionAsLastFigures() {
        return (shooter, board, lastTargets) -> {
            int[] origin = ((AbstractSquare) lastTargets.get(0)).getCoordinates();
            int[] last = ((AbstractSquare) lastTargets.get(1)).getCoordinates();
            Predicate<AbstractSquare> p;
            if (origin[0] == last[0] && origin[1] > last[1])
                p = x -> x.getCoordinates()[0] == origin[0] && x.getCoordinates()[1] < origin[1];
            else if (origin[0] == last[0] && origin[1] < last[1])
                p = x -> x.getCoordinates()[0] == origin[0] && x.getCoordinates()[1] > origin[1];
            else if (origin[1] == last[1] && origin[0] < last[0])
                p = x -> x.getCoordinates()[1] == origin[1] && x.getCoordinates()[0] > origin[0];
            else
                p = x -> x.getCoordinates()[1] == origin[1] && x.getCoordinates()[0] < origin[0];
            return board.getSquares().stream()
                    .filter(p)
                    .map(AbstractSquare::getOccupants)
                    .collect(HashSet::new, HashSet::addAll, HashSet::addAll);
        };
    }

    /**
     * Returns the <code>TargetGen</code> that generates the non-null elements
     * present in the last 2 positions of lastTargets (used in machine gun)
     *
     * @return the TargetGen generating the non-null elements
     * present in the last 2 positions of lastTargets
     */
    public static TargetGen otherTarget() {
        return (shooter, board, last) -> last.stream().limit(2).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static TargetGen ifNotMoved() {
        return (shooter, board, lastTargets) -> lastTargets.stream().anyMatch(x -> board.getSquares().contains(x)) ? Collections.emptySet() : new HashSet<>(board.getSquares());
    }


}
