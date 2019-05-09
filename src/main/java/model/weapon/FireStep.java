package model.weapon;

import model.board.Board;
import model.board.Figure;
import model.board.Targettable;

import java.util.List;
import java.util.Set;

public class FireStep {
    private final TargetGen targetGen;
    private final Effect effect;
    private final int minTargets;
    private final int maxTargets;

    public FireStep(int minTargets, int maxTargets, TargetGen targetGen, Effect effect) {
        this.minTargets = minTargets;
        this.maxTargets = maxTargets;
        this.effect = effect;
        this.targetGen = targetGen;
    }

    public int getMaxTargets() {
        return maxTargets;
    }

    public int getMinTargets() {
        return minTargets;
    }

    public Set<Targettable> getTargets(Figure shooter, Board board, List<Targettable> lastTargets) {
        return targetGen.get(shooter, board, lastTargets);
    }

    public void run(Figure shooter, Set<Targettable> currentTargets, List<Targettable> lastTargets) {
        effect.run(shooter, currentTargets, lastTargets);
    }
}
