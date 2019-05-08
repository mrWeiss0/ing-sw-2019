package model.weapon;

import model.AmmoCube;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FireMode {
    private List<FireStep> steps = new ArrayList<>();
    private AmmoCube cost;

    public FireMode() {
        this(new AmmoCube());
    }

    public FireMode(AmmoCube cost) {
        this.cost = cost;
    }

    public void addStep(FireStep step) {
        steps.add(step);
    }

    public AmmoCube getCost() {
        return cost;
    }

    public Stream<FireStep> getStepsStream() {
        return steps.stream();
    }
}
