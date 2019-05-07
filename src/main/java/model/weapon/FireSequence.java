package model.weapon;

import model.board.Board;
import model.board.Figure;
import model.board.Targettable;

import java.util.*;

public class FireSequence {
    private Figure shooter;
    private Board board;
    private Iterator<FireStep> steps;
    private FireStep currentStep;
    private Set<Targettable> validTargets = new HashSet<>();
    private List<Targettable> lastTargets = new ArrayList<>();
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

    public boolean run(List<Targettable> currentTargets) {
        if (!validateTargets(currentTargets)) return false;
        lastTargets = currentStep.run(shooter, currentTargets, lastTargets);
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

    private boolean validateTargets(List<Targettable> currentTargets) {
        Set<Targettable> set = new HashSet<>();
        if (currentTargets.size() >= currentStep.getMaxTargets()) return false;
        for (Targettable t : currentTargets)
            if (!validTargets.contains(t) || !set.add(t)) return false;
        return true;
    }
}
