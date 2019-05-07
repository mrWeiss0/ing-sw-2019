package model.weapon;

import model.Board;
import model.Figure;
import model.Targettable;

import java.util.List;
import java.util.Set;

public interface TargetGen {
    Set<Targettable> get(Figure shooter, Board board, List<Targettable> lastTargets);
}
