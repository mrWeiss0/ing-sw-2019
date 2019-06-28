package server.controller;

import java.util.Arrays;
import java.util.List;

public class Action {
    private final GameController.State[] states;

    public Action(GameController.State... states) {
        this.states = states;
    }

    public List<GameController.State> getStates() {
        return Arrays.asList(states);
    }
}
