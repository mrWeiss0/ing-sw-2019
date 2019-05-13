package model.weapon;

import model.board.Board;
import model.board.Figure;
import model.board.Targettable;

import java.util.List;
import java.util.Set;

@FunctionalInterface
public interface TargetGen {
    Set<Targettable> get(Figure shooter, Board board, List<Targettable> lastTargets);
}
