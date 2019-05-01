package model.weapon;

import model.AmmoCube;

import java.util.ArrayList;
import java.util.List;

public class FireMode {
    private List<FireStep> steps;
    private AmmoCube cost;

    public FireMode() {
        this(new AmmoCube());
    }

    public FireMode(AmmoCube cost) {
        this.cost = cost;
        steps = new ArrayList<>();
    }

    public void addStep(FireStep step) {
        steps.add(step);
    }

    public AmmoCube getCost() {
        return cost;
    }

    public List<FireStep> getSteps() {
        return steps;
    }
}
