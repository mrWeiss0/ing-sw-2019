package model.weapon;

import model.board.Figure;
import model.board.Targettable;

import java.util.List;

public interface Effect {
    List<Targettable> run(Figure shooter, List<Targettable> currentTargets, List<Targettable> lastTargets);
}
