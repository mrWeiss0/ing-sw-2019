package server.model.weapon;

import server.model.board.AbstractSquare;
import server.model.board.Figure;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class TargetGens{

    public static TargetGen visibleSquares() {
        return (shooter, board, last)-> shooter.getLocation().visibleSquares()
                .stream().filter(t -> t != shooter.getLocation()).collect(Collectors.toSet());
    }

    public static TargetGen visibleFigures() {
        return  (shooter, board, last)->shooter.getLocation().visibleFigures()
                .stream().filter(t -> t != shooter).collect(Collectors.toSet());
    }

    public static TargetGen visibleRoom(){
        return (shooter, board, lastTargets) -> shooter.getLocation().visibleRooms();
    }

    public static TargetGen notVisibleFigures(){
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x->x!=shooter && !shooter.getLocation().visibleFigures().contains(x)).collect(Collectors.toSet());

    }

    public static TargetGen inLastFigure(){
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(t->t!=shooter && lastTargets.contains(t)).collect(Collectors.toSet());

    }

    public static TargetGen notInLastFigures(){
        return (shooter,board,last)->  board.getFigures()
                .stream().filter(t -> t != shooter && !last.contains(t)).collect(Collectors.toSet());
    }

    public static TargetGen onLastFigures(){
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x->lastTargets.contains(x.getLocation())).collect(Collectors.toSet());
    }

    public static TargetGen maxDistanceFigures(int n){
        return (shooter,board,last)-> board.getFigures()
                .stream().filter(t->t!=shooter && shooter.getLocation().atDistance(n).contains(t.getLocation())).collect(Collectors.toSet());
    }

    public static TargetGen maxDistanceSquares(int n){
        return (shooter, board, lastTargets) -> new HashSet<>(shooter.getLocation().atDistance(n));
    }

    public static TargetGen atDistanceFromVisibleSquareFigures(int n){
        return (shooter, board, lastTargets) -> board.getFigures().stream()
                .filter(x->x!=shooter && shooter.getLocation().visibleSquares()
                        .stream().anyMatch(y->((AbstractSquare)y).atDistance(n).contains(x.getLocation()))).collect(Collectors.toSet());
    }

    public static TargetGen onCardinalFigures(){
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x->x!= shooter && x.getLocation()!=null && (x.getLocation().getCoordinates()[0]==shooter.getLocation().getCoordinates()[0] ||
                        x.getLocation().getCoordinates()[1]==shooter.getLocation().getCoordinates()[1])).collect(Collectors.toSet());
    }

    public static TargetGen onCardinalSquare(){
        return (shooter, board, lastTargets) -> board.getSquares()
                .stream().filter(x->x.getCoordinates()[0]==shooter.getLocation().getCoordinates()[0] ||
                x.getCoordinates()[1]==shooter.getLocation().getCoordinates()[1]).collect(Collectors.toSet());
    }

    public static TargetGen differentRoom(){
        return (shooter, board, lastTargets) -> board.getRooms()
                .stream().filter(x->!x.getSquares().contains(shooter.getLocation())).collect(Collectors.toSet());
    }

    public static TargetGen differentSquares(){
        return (shooter, board, lastTargets) -> board.getSquares().stream().filter(x->x!=shooter.getLocation()).collect(Collectors.toSet());
    }

    public static TargetGen differentSquareFigures(){
        return (shooter, board, lastTargets) -> board.getFigures().stream().filter(x->x.getLocation()!=shooter.getLocation()).collect(Collectors.toSet());
    }

    public static TargetGen maxDistanceFromLastFigures(int n){
        return (shooter, board, lastTargets)->((AbstractSquare)lastTargets.get(0)).atDistance(n).stream().
                collect(HashSet::new,(x, y)->x.addAll(y.getOccupants().stream().filter(z->z!=shooter).collect(Collectors.toSet())), Set::addAll);
    }

    public static TargetGen maxDistanceFromLastSquares(int n){
        return (shooter, board, lastTargets)-> new HashSet<>(((AbstractSquare)lastTargets.get(0)).atDistance(n));
    }

    public static TargetGen visibleFromLastFigures(){
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x->x!=shooter && !lastTargets.contains(x) && ((Figure)lastTargets.get(lastTargets.size()-1)).getLocation()
                        .visibleSquares().contains(x.getLocation())).collect(Collectors.toSet());
    }

    public static TargetGen sameDirectionAsLastSquares(){
        return (shooter, board, lastTargets) -> {
            int[] origin = ((AbstractSquare)lastTargets.get(0)).getCoordinates();
            int[] last = shooter.getLocation().getCoordinates();
            if(origin[0]==last[0] && origin[1]>last[1]) return board.getSquares()
                    .stream().filter(x->x.getCoordinates()[0]==origin[0] && x.getCoordinates()[1]<origin[1]).collect(Collectors.toSet());
            else if(origin[0]==last[0] && origin[1]<last[1]) return board.getSquares()
                    .stream().filter(x->x.getCoordinates()[0]==origin[0] && x.getCoordinates()[1]>origin[1]).collect(Collectors.toSet());
            else if(origin[1]==last[1] && origin[0]<last[0]) return board.getSquares()
                    .stream().filter(x->x.getCoordinates()[1]==origin[1] && x.getCoordinates()[0]>origin[0]).collect(Collectors.toSet());
            else return board.getSquares()
                    .stream().filter(x->x.getCoordinates()[1]==origin[1] && x.getCoordinates()[0]<origin[0]).collect(Collectors.toSet());
        };
    }

    public static TargetGen otherTarget(){
        return (shooter, board, last) -> last.stream().limit(2).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
