package model.weapon;

import model.board.Figure;
import model.board.Targettable;

import java.util.List;
import java.util.Set;

@FunctionalInterface
public interface Effect {
    void run(Figure shooter, Set<Targettable> currentTargets, List<Targettable> lastTargets);
}
