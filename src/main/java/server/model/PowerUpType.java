package server.model;

import com.google.gson.annotations.SerializedName;
import server.model.board.AbstractSquare;
import server.model.board.Figure;
import server.model.weapon.FireStep;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public enum PowerUpType {
    @SerializedName("scope")
    SCOPE(
            new FireStep(1, 1,
                    (shooter, gameBoard, last) -> new HashSet<>(gameBoard.getDamaged()),
                    (shooter, curr, last) -> curr.forEach(x -> x.damageFrom(shooter, 1))
            )),
    @SerializedName("newton")
    NEWTON(
            new FireStep(1, 1,
                    (shooter, gameBoard, last) -> new HashSet<>(gameBoard.getFigures()),
                    (shooter, curr, last) -> {
                    }
            ),
            new FireStep(1, 1,
                    (shooter, gameBoard, last) -> ((Figure) last.toArray()[0]).getLocation().atDistance(1, 2).
                            stream().filter(x ->
                            x.getCoordinates()[0] == ((Figure) last.toArray()[0]).getLocation().getCoordinates()[0] ||
                                    x.getCoordinates()[1] == ((Figure) last.toArray()[0]).getLocation().getCoordinates()[1]).collect(Collectors.toSet()),
                    (shooter, curr, last) -> ((Figure) last.toArray()[0]).moveTo((AbstractSquare) curr.toArray()[0])
            )),
    @SerializedName("tagback")
    TAGBACK(
            new FireStep(1, 1,
                    (shooter, gameBoard, last) -> new HashSet<>(Collections.singletonList(shooter.getDamages().get(shooter.getDamages().size() - 1))),
                    (shooter, curr, last) -> curr.forEach(x -> x.markFrom(shooter, 1))
            )),
    @SerializedName("teleporter")
    TELEPORTER(
            new FireStep(1, 1,
                    (shooter, gameBoard, last) -> new HashSet<>(gameBoard.getSquares()),
                    (shooter, curr, last) -> shooter.moveTo((AbstractSquare) curr.toArray()[0])
            ));

    private final List<FireStep> stepList;

    PowerUpType(FireStep... steps) {
        this.stepList = Arrays.asList(steps);
    }

    public List<FireStep> getStepList() {
        return stepList;
    }
}
