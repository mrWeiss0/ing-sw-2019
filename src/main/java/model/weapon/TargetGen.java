package model.weapon;

import model.Figure;
import model.Game;
import model.Targettable;

import java.util.List;
import java.util.Set;

public interface TargetGen {
    Set<Targettable> get(Figure shooter, Game game, List<Targettable> lastTargets);
}
