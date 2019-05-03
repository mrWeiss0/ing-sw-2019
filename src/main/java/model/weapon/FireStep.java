package model.weapon;

import model.Figure;
import model.Game;
import model.Targettable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FireStep {
    private List<TargetGen> targetGens = new ArrayList<>();
    private Effect effect;
    private int maxTargets;

    public FireStep(int maxTargets, Effect effect) {
        this.maxTargets = maxTargets;
        this.effect = effect;
    }

    public void addTargetGen(TargetGen targetGen) {
        targetGens.add(targetGen);
    }

    public int getMaxTargets() {
        return maxTargets;
    }

    public Set<Targettable> getTargets(Figure shooter, Game game, List<Targettable> lastTargets) {
        return targetGens.stream().map(t -> t.get(shooter, game, lastTargets)).reduce((a, b) -> {
            a.retainAll(b);
            return a;
        }).orElseGet(HashSet::new);
    }

    public List<Targettable> run(Figure shooter, List<Targettable> currentTargets, List<Targettable> lastTargets) {
        return effect.run(shooter, currentTargets, lastTargets);
    }
}
