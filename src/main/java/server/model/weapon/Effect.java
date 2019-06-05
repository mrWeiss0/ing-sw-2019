package server.model.weapon;

import server.model.board.Figure;
import server.model.board.Targettable;

import java.util.List;
import java.util.Set;

/**
 * <code>Effect</code> is a functional interface for applying an effect to a list
 * of targets, which include dealing damage and marks or moving the target to a
 * previously targeted location.
 */
@FunctionalInterface
public interface Effect {

    /**
     * Applies on the specified targets an effect as dealt by the shooter.
     * The list of targettable previously affected may also be updated for
     * future usage.
     *
     * @param shooter        the figure who is shooting
     * @param currentTargets the targets on which to apply the effect to
     * @param lastTargets    the list previously affected targets to be updated
     */
    void run(Figure shooter, Set<Targettable> currentTargets, List<Targettable> lastTargets);

    default Effect and(Effect other) {
        return (shooter, currentTargets, lastTargets) -> {
            this.run(shooter, currentTargets, lastTargets);
            other.run(shooter, currentTargets, lastTargets);
        };
    }
}
