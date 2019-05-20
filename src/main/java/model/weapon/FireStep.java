package model.weapon;

import model.board.Board;
import model.board.Figure;
import model.board.Targettable;

import java.util.List;
import java.util.Set;

/**
 * The <code>FireStep</code> class allows to target and apply effects to
 * elements of a <code>Board</code>.
 * <p>
 *
 * <p>
 * It also allows to specify the minimum and maximum number of targets that
 * the step should apply an effect to.
 */
public class FireStep {
    private final TargetGen targetGen;
    private final Effect effect;
    private final int minTargets;
    private final int maxTargets;

    /**
     * Constructs a <code>FireStep</code> with the specified target generation
     * function and effect. A minimum and maximum number of targets to which
     * the effect can be applied to may also be specified.
     *
     * @param minTargets minimum number of targets the effect can be applied to
     * @param maxTargets maximum number of targets the effect can be applied to
     * @param targetGen the function to be used for target generation
     * @param effect the effect to be applied to the targets
     */
    public FireStep(int minTargets, int maxTargets, TargetGen targetGen, Effect effect) {
        this.minTargets = minTargets;
        this.maxTargets = maxTargets;
        this.effect = effect;
        this.targetGen = targetGen;
    }

    /**
     * Returns the maximum number of target that the effect should be applied to
     *
     * @return the maximum number of target that the effect should be applied to
     */
    public int getMaxTargets() {
        return maxTargets;
    }

    /**
     * Returns the maximum number of target that the effect should be applied to
     *
     * @return the maximum number of target that the effect should be applied to
     */
    public int getMinTargets() {
        return minTargets;
    }

    /**
     * Applies this FireStep's target generation function to the board,
     * returning the generated <code>Targettable</code>s.
     *
     * @param shooter the <code>Figure</code> who is currently shooting
     * @param board the <code>Board</code> the function is to be applied to
     * @param lastTargets the previously effected targets
     * @return  the set of generated targets
     */
    public Set<Targettable> getTargets(Figure shooter, Board board, List<Targettable> lastTargets) {
        return targetGen.get(shooter, board, lastTargets);
    }

    /**
     * Applies this <code>FireStep</code>'s effect to the specified targets.
     *
     * @param shooter the <code>Figure</code> who is currently shooting
     * @param currentTargets the targets on which the effect is to be applied
     * @param lastTargets the previously effected targets
     */
    public void run(Figure shooter, Set<Targettable> currentTargets, List<Targettable> lastTargets) {
        effect.run(shooter, currentTargets, lastTargets);
    }
}
