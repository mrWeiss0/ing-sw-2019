package server.model.weapon;

import server.model.board.Board;
import server.model.board.Figure;
import server.model.board.Targettable;

import java.util.HashSet;
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
     * @param shooter     the figure who is shooting
     * @param board       the board on which to look for targets
     * @param lastTargets the list of previous affected targets
     * @return the set of elements which can be targeted
     */
    Set<Targettable> get(Figure shooter, Board board, List<Targettable> lastTargets);

    default TargetGen and(TargetGen targetGen2){
        return (shooter,board,last)-> {
            Set<Targettable> res= new HashSet<>(this.get(shooter,board,last));
            res.retainAll(targetGen2.get(shooter,board,last));
            return res;
        };
    }
    default TargetGen or(TargetGen targetGen2){
        return (shooter,board,last)-> {
            Set<Targettable> res= new HashSet<>(this.get(shooter,board,last));
            res.addAll(targetGen2.get(shooter,board,last));
            return res;
        };
    }
    default TargetGen less(TargetGen targetGen2){
        return (shooter,board,last)-> {
            Set<Targettable> res= new HashSet<>(this.get(shooter,board,last));
            res.removeAll(targetGen2.get(shooter,board,last));
            return res;
        };
    }
}
