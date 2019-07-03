package server.model.weapon;

import server.model.board.Board;
import server.model.board.Figure;
import server.model.board.Targettable;

import java.util.*;

/**
 * The <code>FireSequence</code> class allows handling of a shooting sequence:
 * given a list of <code>FireStep</code>s, it provides the ability to easily
 * iterate over them, generate target list, check the viability of given
 * targets and run effects on them.
 */
public class FireSequence {
    private final Figure shooter;
    private final Board board;
    private final Iterator<FireStep> steps;
    private final List<Targettable> lastTargets = new ArrayList<>();
    private FireStep currentStep;
    private List<Targettable> validTargets = new ArrayList<>();
    private boolean hasNext = true;

    /**
     * Constructs a <code>FireSequence</code> with the specified <code>Figure</code>
     * as the shooter, the <code>Board</code> on which to generate targets and
     * apply effects, and the list of steps to execute.
     *
     * @param shooter  the <code>Figure</code> that is shooting
     * @param board    the <code>Board</code> on which to apply the target
     *                 generation and effects
     * @param stepList the list of steps to execute
     */
    public FireSequence(Figure shooter, Board board, List<FireStep> stepList) {
        this.shooter = shooter;
        this.board = board;
        steps = stepList.iterator();
        next();
    }

    /**
     * Returns the all previous targets on which effects have been applied.
     *
     * @return all previous targets on which effects have been applied
     */
    public List<Targettable> getTargets() {
        return validTargets;
    }

    /**
     * Applies the <code>FireSequence</code> current step's effect on the
     * <code>Set</code> of specified targets. The set must abide restrictions
     * set by step itself, and are checked by <code>validateTargets</code>
     * method
     *
     * @param currentTargets the targets on which to apply the step' effect
     * @throws NoSuchElementException   if all the steps have been run
     * @throws IllegalArgumentException if invalid targets are given
     */
    public void run(Set<Targettable> currentTargets) {
        if (!hasNext)
            throw new NoSuchElementException("No more steps to execute");
        if (!validateTargets(currentTargets))
            throw new IllegalArgumentException("Invalid targets selected");
        currentStep.run(shooter, currentTargets, lastTargets);
        next();
    }

    /**
     * Returns true if the given set of targets is a subset of the one
     * generated by the current step's target gen and its cardinality respects
     * the boundaries set by the current step.
     *
     * @param currentTargets the set of targets to be validated
     * @return true if the given set of targets is a subset of the one
     * generated by the current step's target gen and its cardinality respects
     * the boundaries set by the current step
     */
    public boolean validateTargets(Set<Targettable> currentTargets) {
        if (currentTargets.size() < currentStep.getMinTargets() ||
                currentTargets.size() > currentStep.getMaxTargets())
            return false;
        return validTargets.containsAll(currentTargets);
    }

    public int getMinTargets(){
        return currentStep.getMinTargets();
    }

    public int getMaxTargets(){
        return currentStep.getMaxTargets();
    }

    /**
     * Returns true if there's still a step which effect has to be applied.
     *
     * @return true if there's still a step which effect has to be applied
     */
    public boolean hasNext() {
        return hasNext;
    }

    private void next() {
        if (steps.hasNext()) {
            currentStep = steps.next();
            validTargets = new ArrayList<>(currentStep.getTargets(shooter, board, lastTargets));
        } else
            hasNext = false;
    }

    /**
     * Returns the shooter of the fire sequence.
     *
     * @return the shooter of this sequence
     */
    public Figure getShooter() {
        return shooter;
    }
}
