package model.weapon;

import model.AmmoCube;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FireMode {
    private final List<FireStep> steps;
    private final AmmoCube cost;

    public FireMode(FireStep... steps) {
        this(new AmmoCube(), steps);
    }

    public FireMode(AmmoCube cost, FireStep... steps) {
        this.cost = cost;
        this.steps = Arrays.asList(steps);
    }

    public static List<FireStep> flatSteps(List<FireMode> fm) {
        return fm.stream().flatMap(FireMode::getStepsStream).collect(Collectors.toList());
    }

    public AmmoCube getCost() {
        return cost;
    }

    private Stream<FireStep> getStepsStream() {
        return steps.stream();
    }
}
