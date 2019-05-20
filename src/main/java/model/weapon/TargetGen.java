package model.weapon;

import model.board.Board;
import model.board.Figure;
import model.board.Targettable;

import java.util.List;
import java.util.Set;

/**
 * Target generation is the ability to determine on a <code>Board</code> which
 * elements implementing the <code>Targettable</code> interface can be
 * targeted by the shooter.
 * <p>
 * <code>TargetGen</code> is a functional interface for target generation
 * purposes.
 */
@FunctionalInterface
public interface TargetGen {
    /**
     * Returns the set of elements which can be by the specified shooter
     * on the given board.
     *
     * @param shooter the figure who is shooting
     * @param board the board on which to look for targets
     * @param lastTargets the list of previous affected targets
     * @return the set of elements which can be targeted
     */
    Set<Targettable> get(Figure shooter, Board board, List<Targettable> lastTargets);
}
