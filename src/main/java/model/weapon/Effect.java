package model.weapon;

import model.Figure;
import model.Targettable;

import java.util.List;

public interface Effect {
    void run(Figure shooter, List<Targettable> currentTargets, List<Targettable> lastTargets);
}
