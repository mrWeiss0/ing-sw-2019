package model.weapon;

import model.board.Board;
import model.board.Figure;
import model.board.Targettable;

import java.util.*;

public class FireSequence {
    private final Figure shooter;
    private final Board board;
    private final Iterator<FireStep> steps;
    private final List<Targettable> lastTargets = new ArrayList<>();
    private FireStep currentStep;
    private Set<Targettable> validTargets = new HashSet<>();
    private boolean hasNext = true;

    public FireSequence(Figure shooter, Board board, List<FireStep> stepList) {
        this.shooter = shooter;
        this.board = board;
        steps = stepList.iterator();
        next();
    }

    public Set<Targettable> getTargets() {
        return validTargets;
    }

    public boolean run(Set<Targettable> currentTargets) {
        if (!(hasNext && validateTargets(currentTargets))) return false;
        currentStep.run(shooter, currentTargets, lastTargets);
        next();
        return true;
    }

    public boolean hasNext() {
        return hasNext;
    }

    private void next() {
        if (steps.hasNext()) {
            currentStep = steps.next();
            validTargets = currentStep.getTargets(shooter, board, lastTargets);
        } else
            hasNext = false;
    }

    private boolean validateTargets(Set<Targettable> currentTargets) {
        if (currentTargets.size() < currentStep.getMinTargets() ||
                currentTargets.size() > currentStep.getMaxTargets())
            return false;
        return validTargets.containsAll(currentTargets);
    }
}
