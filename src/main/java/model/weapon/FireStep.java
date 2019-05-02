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
    private List<Effect> effects = new ArrayList<>();
    private int maxTargets;

    public FireStep(int maxTargets) {
        this.maxTargets = maxTargets;
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
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

    public List<Targettable> effect(Figure shooter, List<Targettable> currentTargets, List<Targettable> lastTargets) {
        List<Targettable> nextTargets = new ArrayList<>();
        for (Effect e : effects)
            nextTargets.addAll(e.run(shooter, currentTargets, lastTargets));
        return nextTargets;
    }
}
