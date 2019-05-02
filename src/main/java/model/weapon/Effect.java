package model.weapon;

import model.Figure;
import model.Targettable;

import java.util.List;

public interface Effect {
    List<Targettable> run(Figure shooter, List<Targettable> currentTargets, List<Targettable> lastTargets);
}
