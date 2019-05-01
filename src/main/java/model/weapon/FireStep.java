package model.weapon;

import model.Figure;
import model.Game;
import model.Targettable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FireStep {
    private List<TargetGen> targetGens;
    private List<Effect> effects;
    private int max;

    public FireStep(int max) {
        this.max = max;
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    public void addTargetGen(TargetGen targetGen) {
        targetGens.add(targetGen);
    }

    public int getMax() {
        return max;
    }

    public Set<Targettable> getTargets(Figure shooter, Game game, List<Targettable> lastTargets) {
        return targetGens.stream().map(t -> t.get(shooter, game, lastTargets)).reduce((a, b) -> {
            a.retainAll(b);
            return a;
        }).orElseGet(HashSet::new);
    }

    public void effect(Figure shooter, List<Targettable> currentTargets, List<Targettable> lastTargets) {
        for (Effect e : effects)
            e.run(shooter, currentTargets, lastTargets);
    }
}
