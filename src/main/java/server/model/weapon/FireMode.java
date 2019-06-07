package server.model.weapon;

import server.model.AmmoCube;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * The <code>FireMode</code> class represents a weapon's fire mode, with its
 * associated cost and <code>FireStep</code>s, each of which represents a
 * single atomic effect of its fire mode. Both the cost and the <code>FireSteps</code>
 * are <code>final</code> as to reflect a fire mode's static nature in the
 * game.
 * <p>
 * The class provides static methods to calculate a list of <code>FireMode</code>s'
 * cost or its associated <code>FireSteps</code>.
 */
public class FireMode {
    private final List<FireStep> steps;
    private final AmmoCube cost;

    /**
     * Constructs a FireMode with the specified steps and a cost of zero.
     *
     * @param steps the steps of the FireMode
     */
    public FireMode(FireStep... steps) {
        this(new AmmoCube(), steps);
    }

    /**
     * Constructs a FireMode with the specified steps and cost.
     *
     * @param cost  the cost of the FireMode
     * @param steps the steps of the FireMode
     */
    public FireMode(AmmoCube cost, FireStep... steps) {
        this.cost = cost;
        this.steps = Arrays.asList(steps);
    }

    /**
     * Returns the list of <code>FireStep</code>s for the given list of
     * FireModes; the returned list is ordered so that a FireMode's steps are
     * concatenated with those of the next FireMode.
     *
     * @param fm the list of FireModes of which the <code>FireStep</code>s are
     *           to be returned
     * @return the list of <code>FireStep</code> of the specified FireModes
     */
    public static List<FireStep> flatSteps(List<FireMode> fm) {
        return fm.stream().flatMap(FireMode::getStepsStream).collect(Collectors.toList());
    }

    /**
     * Returns the total cost for the specified FireModes.
     *
     * @param fm the list of FireModes of which the total cost is to be
     *           calculated
     * @return the total cost of the specified FireModes
     */
    public static AmmoCube flatCost(List<FireMode> fm) {
        return fm.stream().map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new);
    }

    /**
     * Returns the cost of this FireMode.
     *
     * @return the cost of this FireMode
     */
    public AmmoCube getCost() {
        return cost;
    }

    /**
     * Returns this FireMode's FireSteps.
     *
     * @return this FireMode's FireStep
     */
    private Stream<FireStep> getStepsStream() {
        return steps.stream();
    }
}
