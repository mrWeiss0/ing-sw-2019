package server.model.weapon;

import server.model.board.Board;
import server.model.board.Figure;
import server.model.board.Targettable;

import java.util.List;
import java.util.Set;

@FunctionalInterface
public interface TargetGen {
    Set<Targettable> get(Figure shooter, Board board, List<Targettable> lastTargets);
}
