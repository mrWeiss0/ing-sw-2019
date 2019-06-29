package server.controller;

import java.util.List;

@FunctionalInterface
public interface Action {
    List<GameController.State> getStates();
}
