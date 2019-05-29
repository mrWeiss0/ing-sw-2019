package server.model.weapon;

import server.model.board.Board;
import server.model.board.Figure;
import server.model.board.Targettable;

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

    public void run(Set<Targettable> currentTargets) {
        if (!hasNext)
            throw new NoSuchElementException("No more steps to execute");
        if (!validateTargets(currentTargets))
            throw new IllegalArgumentException("Invalid targets selected");
        currentStep.run(shooter, currentTargets, lastTargets);
        next();
    }

    public boolean validateTargets(Set<Targettable> currentTargets) {
        if (currentTargets.size() < currentStep.getMinTargets() ||
                currentTargets.size() > currentStep.getMaxTargets())
            return false;
        return validTargets.containsAll(currentTargets);
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
}
