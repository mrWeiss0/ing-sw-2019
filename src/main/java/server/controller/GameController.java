package server.controller;


import server.model.Game;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class GameController implements Runnable {
    private final Game game;
    private final ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue<>(5);
    private final Deque<State> stateStack = new ArrayDeque<>();
    private State state;

    public GameController(Game game) {
        this.game = game;
        game.getPlayers().forEach(x -> x.setGame(this));
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                eventQueue.take().accept(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Game getGame() {
        return game;
    }

    public void setState(State s) {
        state = s;
        state.onEnter();
    }

    public void nextState() {
        setState(stateStack.pop());
    }

    void visit(SelectPowerUpEvent event) {
        state.selectPowerUp(event.getPlayer(), event.getPowerUps());
    }

    void visit(SelectWeaponToReloadEvent event) {
        state.selectWeapon(event.getPlayer(), event.getWeapons());
    }

    void visit(SelectWeaponFireModeEvent event) {
        state.selectFireMode(event.getPlayer(), event.getWeapon(), event.getFireModes());
    }

    void visit(SelectGrabbableEvent event) {
        state.selectGrabbable(event.getPlayer(), event.getGrabbable());
    }

    void visit(SelectActionEvent event) {
        state.selectAction(event.getPlayer(), event.getAction());
    }

    void visit(SelectTargettableEvent event) {
        state.selectTargettable(event.getPlayer(), event.getTargettables());
    }

    void visit(SelectColorEvent event) {
        state.selectColor(event.getPlayer(), event.getColor());
    }

    public void enqueue(Event selectPowerUpEvent) {
        try {
            eventQueue.put(selectPowerUpEvent);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addState(State state) {
        stateStack.add(state);
    }

    public void addStates(List<State> states) {
        stateStack.addAll(states);
    }
}
