package server.model.weapon;

import server.model.board.Figure;
import server.model.board.Targettable;

import java.util.List;
import java.util.Set;

@FunctionalInterface
public interface Effect {
    void run(Figure shooter, Set<Targettable> currentTargets, List<Targettable> lastTargets);
}
