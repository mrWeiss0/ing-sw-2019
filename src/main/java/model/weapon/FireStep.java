package model.weapon;

import model.board.Board;
import model.board.Figure;
import model.board.Targettable;

import java.util.List;
import java.util.Set;

public class FireStep {
    private TargetGen targetGen;
    private Effect effect;
    private int maxTargets;

    public FireStep(int maxTargets, TargetGen targetGen, Effect effect) {
        this.maxTargets = maxTargets;
        this.effect = effect;
        this.targetGen = targetGen;
    }

    public int getMaxTargets() {
        return maxTargets;
    }

    public Set<Targettable> getTargets(Figure shooter, Board board, List<Targettable> lastTargets) {
        return targetGen.get(shooter, board, lastTargets);
    }

    public List<Targettable> run(Figure shooter, List<Targettable> currentTargets, List<Targettable> lastTargets) {
        return effect.run(shooter, currentTargets, lastTargets);
    }
}
