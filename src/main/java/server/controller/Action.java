package server.controller;

public class Action {
    private final GameController.State[] states;

    public Action(GameController.State[] states) {
        this.states = states;
    }

    public GameController.State[] getStates() {
        return states;
    }
}
