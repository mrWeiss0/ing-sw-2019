package server.model.weapon;

import server.model.board.AbstractSquare;

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

    public static TargetGen notVisibleFigures(){
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x->x!=shooter && !shooter.getLocation().visibleFigures().contains(x)).collect(Collectors.toSet());
    }

    public static TargetGen notInLastFigures(){
        return (shooter,board,last)->  board.getFigures()
                .stream().filter(t -> t != shooter && !last.contains(t)).collect(Collectors.toSet());
    }

    public static TargetGen maxDistanceFigures(int n){
        return (shooter,board,last)-> board.getFigures()
                .stream().filter(t->t!=shooter && shooter.getLocation().atDistance(n).contains(t.getLocation())).collect(Collectors.toSet());
    }

    public static TargetGen inLastFigure(){
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(t->t!=shooter && lastTargets.contains(t)).collect(Collectors.toSet());
    }

    public static TargetGen atDistanceFromVisibleSquareFigures(int n){
        return (shooter, board, lastTargets) -> board.getFigures().stream()
                .filter(x->x!=shooter && shooter.getLocation().visibleSquares()
                        .stream().anyMatch(y->((AbstractSquare)y).atDistance(n).contains(x.getLocation()))).collect(Collectors.toSet());
    }

    public static TargetGen onCardinalFigure(){
        return (shooter, board, lastTargets) -> board.getFigures()
                .stream().filter(x->x!= shooter && x.getLocation()!=null && (x.getLocation().getCoordinates()[0]==shooter.getLocation().getCoordinates()[0] ||
                        x.getLocation().getCoordinates()[1]==shooter.getLocation().getCoordinates()[1])).collect(Collectors.toSet());
    }

    public static TargetGen onCardinalSquare(){
        return (shooter, board, lastTargets) -> board.getSquares()
                .stream().filter(x->x.getCoordinates()[0]==shooter.getLocation().getCoordinates()[0] ||
                x.getCoordinates()[1]==shooter.getLocation().getCoordinates()[1]).collect(Collectors.toSet());
    }

}
