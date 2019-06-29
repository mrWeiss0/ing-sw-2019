package server.controller;


import server.model.*;
import server.model.board.Figure;
import server.model.board.SpawnSquare;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.FireSequence;
import server.model.weapon.FireStep;
import server.model.weapon.Weapon;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class GameController implements Runnable {
    private final Game game;
    private State state;
    private final ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue<>(5);
    private final Deque<State> stateStack = new ArrayDeque<>();

    public GameController(Game game) {
        this.game = game;
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

    private void setState(State s) {
        state = s;
        state.onEnter();
    }

    private void nextState() {
        setState(stateStack.isEmpty() ? new TurnState() : stateStack.pop());
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

    interface State {
        default void selectPowerUp(Player player, PowerUp[] powerUps) {
        }

        default void selectWeapon(Player player, Weapon[] weapons) {
        }

        default void selectFireMode(Player player, Weapon weapon, FireMode[] fireModes) {
        }

        default void selectGrabbable(Player player, Grabbable grabbable) {
        }

        default void selectAction(Player player, Action action) {
        }

        default void selectTargettable(Player player, Targettable[] targettables) {
        }

        default void selectColor(Player player, int color) {
        }

        default void onEnter() {
        }
    }

    private class TurnState implements State {
        private final Player current;
        int remainingActions;

        public TurnState() {
            current = game.nextPlayer();
            remainingActions = 2;
        }

        @Override
        public void onEnter() {
            if (current.getFigure().getLocation() == null) {
                stateStack.add(this);
                setState(new SelectSpawnState(current));
            }
            if (remainingActions-- <= 0) {
                // TODO stop timer
                stateStack.add(new TurnState());
                int kills = (int) game.getBoard().getFigures().stream()
                        .filter(figure -> figure.resolveDeath(game))
                        .peek(figure -> stateStack.add(new SelectSpawnState(figure.getPlayer())))
                        .count();
                if (kills > 1)
                    current.getFigure().addPoints(1);
                // TODO timerino 2 ?
                stateStack.add(new SelectReloadState(current));
                nextState();
            }
        }

        @Override
        public void selectAction(Player player, Action action) {
            // TODO if valid action
            stateStack.add(this);
            stateStack.addAll(action.getStates());
            nextState();
        }

        @Override
        public void selectPowerUp(Player player, PowerUp[] powerUps) {
            if (player != this.current || powerUps.length < 1)
                return;
            PowerUpType type = powerUps[0].getType();
            if (type == PowerUpType.NEWTON || type == PowerUpType.TELEPORTER) {
                stateStack.add(this);
                setState(new FireState(current, type.getStepList()));
            }
        }
    }

    private class SelectSpawnState implements State {
        private final Player current;
        private final PowerUp powerUp;

        public SelectSpawnState(Player player) {
            current = player;
            powerUp = game.drawPowerup();
            // TODO SEND
        }

        @Override
        public void selectPowerUp(Player player, PowerUp[] powerUps) {
            if (player == current && powerUps.length >= 1) {
                Figure figure = player.getFigure();
                List<PowerUp> currentPowerUps = figure.getPowerUps();
                if (currentPowerUps.remove(powerUps[0]))
                    currentPowerUps.add(powerUp);
                figure.moveTo(powerUps[0].getSpawn());
                powerUps[0].discard();
                nextState();
            }
        }
    }

    private class SelectReloadState implements State {
        private final Player current;
        private final AmmoCube total;

        public SelectReloadState(Player player) {
            current = player;
            total = player.getFigure().getTotalAmmo();
        }

        @Override
        public void selectWeapon(Player player, Weapon[] weapons) {
            if (player != current)
                return;
            AmmoCube cost = Arrays.stream(weapons)
                    .map(Weapon::getReloadCost)
                    .reduce(AmmoCube::add)
                    .orElseGet(AmmoCube::new);
            if (total.greaterEqThan(cost)) {
                stateStack.add(new ReloadState(weapons));
                setState(new PayState(current, cost));
            }
        }
    }

    private class ReloadState implements State {
        private final Weapon[] weapons;

        public ReloadState(Weapon[] weapons) {
            this.weapons = weapons;
        }

        @Override
        public void onEnter() {
            for (Weapon weapon : weapons)
                weapon.load();
            nextState();
        }
    }

    private class PayState implements State {
        private final Player current;
        private final AmmoCube cost;

        public PayState(Player player, AmmoCube cost) {
            current = player;
            this.cost = cost;
        }

        @Override
        public void selectPowerUp(Player player, PowerUp[] powerUps) {
            if (player != current)
                return;
            AmmoCube paying = cost.sub(Arrays.stream(powerUps)
                    .map(PowerUp::getAmmo)
                    .reduce(AmmoCube::add)
                    .orElseGet(AmmoCube::new));

            if (current.getFigure().getAmmo().greaterEqThan(paying)) {
                current.getFigure().subAmmo(paying);
                for (PowerUp powerUp : powerUps) {
                    current.getFigure().getPowerUps().remove(powerUp);
                    powerUp.discard();
                }
                nextState();
            }
        }
    }

    private class PayAnyColorState implements State {
        private final Player current;
        private final AmmoCube total;

        public PayAnyColorState(Player player) {
            current = player;
            total = player.getFigure().getTotalAmmo();
        }

        @Override
        public void selectColor(Player player, int color) {
            if (player != current)
                return;
            int[] cost = new int[color + 1];
            cost[color] = 1;
            AmmoCube ammoCost = new AmmoCube(cost);
            if (total.greaterEqThan(ammoCost))
                setState(new PayState(current, ammoCost));
        }
    }

    private class FireModeSelectionState implements State {
        private final Player current;
        private final AmmoCube total;

        public FireModeSelectionState(Player player) {
            current = player;
            total = player.getFigure().getTotalAmmo();
        }

        @Override
        public void selectFireMode(Player player, Weapon weapon, FireMode[] fireModes) {
            if (player != current)
                return;
            AmmoCube ammoCost = FireMode.flatCost(Arrays.asList(fireModes));
            if (total.greaterEqThan(ammoCost)) {
                stateStack.add(new FireState(current, FireMode.flatSteps(Arrays.asList(fireModes))));
                setState(new PayState(current, ammoCost));
            }
        }
    }

    private class FireState implements State {
        private final FireSequence fireSequence;

        public FireState(Player player, List<FireStep> stepList) {
            this.fireSequence = new FireSequence(player.getFigure(), game.getBoard(), stepList);
        }

        @Override
        public void onEnter() {
            // TODO SEND TARGETS fireSequence.getTargets();
        }

        @Override
        public void selectTargettable(Player player, Targettable[] targettables) {
            if (player != fireSequence.getShooter().getPlayer())
                return;
            Set<Targettable> targets = Set.of(targettables);
            fireSequence.validateTargets(targets);
            fireSequence.run(targets);
            if (fireSequence.hasNext())
                setState(this);
            else {
                game.getBoard().applyMarks();
                stateStack.add(new ScopeState(player));
                setState(new TagbackState(player, game.getBoard().getDamaged()));
            }
        }
    }

    private class SelectGrabState implements State {
        private final Player current;

        public SelectGrabState(Player player) {
            current = player;
        }

        @Override
        public void onEnter() {
            List<Grabbable> list = current.getFigure().getLocation().peek();
            if (list.isEmpty())
                nextState();
            else if (list.size() == 1)
                selectGrabbable(current, list.get(0));
            // TODO else send
        }

        @Override
        public void selectGrabbable(Player player, Grabbable grabbable) {
            if (player != current)
                return;
            stateStack.add(new GrabState(current, grabbable));
            if (grabbable instanceof Weapon)
                setState(new PayState(current, ((Weapon) grabbable).getPickupCost()));
            else
                nextState();
        }
    }

    private class GrabState implements State {
        private final Player current;
        private final Grabbable grabbable;
        private Weapon discard;

        public GrabState(Player player, Grabbable grabbable) {
            current = player;
            this.grabbable = grabbable;
        }

        @Override
        public void onEnter() {
            Figure figure = current.getFigure();

            try {
                figure.getLocation().grab(figure, grabbable);
                if (discard != null) figure.getLocation().refill(discard);
                nextState();
            } catch (IllegalStateException e) {
                // TODO "you have to discard"
            }
        }

        @Override
        public void selectWeapon(Player player, Weapon[] weapons) {
            if (!(player == current && current.getFigure().getLocation() instanceof SpawnSquare))
                return;
            if (weapons.length != 0) {
                discard = weapons[0];
                current.getFigure().getWeapons().remove(discard);
            }
            setState(this);
        }
    }

    private class ScopeState implements State {
        private final Player current;

        public ScopeState(Player player) {
            current = player;
        }

        @Override
        public void selectPowerUp(Player player, PowerUp[] powerUps) {
            if (player != current)
                return;
            if (powerUps.length < 1) {
                nextState();
                return;
            }
            PowerUpType type = powerUps[0].getType();
            if (type != PowerUpType.SCOPE)
                return;
            stateStack.add(this);
            stateStack.add(new FireState(current, type.getStepList()));
            setState(new PayAnyColorState(current));
        }
    }

    private class TagbackState implements State {
        private final Player current;
        private final Set<Figure> damaged;

        public TagbackState(Player player, Set<Figure> damaged) {
            current = player;
            this.damaged = damaged;
        }

        @Override
        public void onEnter() {
            // TODO add timer nextState();
        }

        @Override
        public void selectPowerUp(Player player, PowerUp[] powerUps) {
            if (!damaged.contains(player.getFigure()))
                return;
            Arrays.stream(powerUps).filter(x -> x.getType() == PowerUpType.TAGBACK).forEach(
                    powerUp -> {
                        player.getFigure().getPowerUps().remove(powerUp);
                        powerUp.discard();
                        FireSequence fs = new FireSequence(player.getFigure(), game.getBoard(), powerUp.getType().getStepList());
                        fs.run(fs.getTargets());
                    }
            );
        }
    }
}

