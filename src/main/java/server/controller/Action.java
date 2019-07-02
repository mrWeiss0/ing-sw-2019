package server.controller;

import server.model.weapon.Effects;
import server.model.weapon.FireStep;
import server.model.weapon.TargetGens;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface Action {
    List<State> getStates(GameController controller, Player player);
}

enum Actions {
    BASE(
            (c, p) -> Arrays.<State>asList(
                    new FireState(c, p, Collections.singletonList(
                            new FireStep(1, 1, TargetGens.atDistanceSquares(p.getFigure().getDamages().size() < 3 ? 1 : 2), Effects.moveToCurr())
                    )),
                    new SelectGrabState(c, p)
            ),
            (c, p) -> Arrays.<State>asList(
                    new FireState(c, p, Collections.singletonList(
                            new FireStep(1, 1, TargetGens.atDistanceSquares(p.getFigure().getDamages().size() < 6 ? 0 : 1), Effects.moveToCurr())
                    )),
                    new FireModeSelectionState(c, p)
            ),
            (c, p) -> Arrays.<State>asList(
                    new FireState(c, p, Collections.singletonList(
                            new FireStep(1, 1, TargetGens.atDistanceSquares(3), Effects.moveToCurr())
                    ))
            )
    ),
    FRENZY1(
            (c, p) -> Arrays.<State>asList(
                    new FireState(c, p, Collections.singletonList(
                            new FireStep(1, 1, TargetGens.atDistanceSquares(3), Effects.moveToCurr())
                    )),
                    new SelectGrabState(c, p)
            ),
            (c, p) -> Arrays.<State>asList(
                    new FireState(c, p, Collections.singletonList(
                            new FireStep(1, 1, TargetGens.atDistanceSquares(2), Effects.moveToCurr())
                    )),
                    new SelectReloadState(c, p),
                    new FireModeSelectionState(c, p)
            )
    ),
    FRENZY2(
            (c, p) -> Arrays.<State>asList(
                    new FireState(c, p, Collections.singletonList(
                            new FireStep(1, 1, TargetGens.atDistanceSquares(2), Effects.moveToCurr())
                    )),
                    new SelectGrabState(c, p)
            ),
            (c, p) -> Arrays.<State>asList(
                    new FireState(c, p, Collections.singletonList(
                            new FireStep(1, 1, TargetGens.atDistanceSquares(1), Effects.moveToCurr())
                    )),
                    new SelectReloadState(c, p),
                    new FireModeSelectionState(c, p)
            ),
            (c, p) -> Arrays.<State>asList(
                    new FireState(c, p, Collections.singletonList(
                            new FireStep(1, 1, TargetGens.atDistanceSquares(4), Effects.moveToCurr())
                    ))
            )
    );

    List<Action> actionList;

    Actions(Action... actions) {
        actionList = Arrays.asList(actions);
    }

    public List<Action> getActionList() {
        return actionList;
    }
}
