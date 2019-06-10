package server.controller;


import server.model.*;
import server.model.board.AbstractSquare;
import server.model.board.Figure;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.FireSequence;
import server.model.weapon.Weapon;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameController implements Runnable {

    private final int maxUsers = 5;
    private HashMap<String, Player> usersByID;
    private UUID name;
    private State state;
    private boolean canJoin;
    private ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue<>(5);
    private Game game;
    private Deque<State> stateQueue = new ArrayDeque<>();

    private State weaponSelectionState = new State() {
        private List<Weapon> choices;

        @Override
        public void onEnter() {
            choices = game.currentPlayer().getFigure().getWeapons().stream().filter(Weapon::isLoaded).collect(Collectors.toList());
            //controller.game.currentPlayer().getClient().send(new String("player's weapons list"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selection, Player player) {
            if (selection.length == 1 && selection[0] >= 0 &&
                    selection[0] < choices.size() &&
                    player == game.currentPlayer()) {
                setState(new FireModeSelectionState(choices.get(selection[0])));
            }
        }
    };

    private Map<String, List<State>> actionMap = Map.of(
            "move", Collections.singletonList(new MoveState(0, 3)),
            "shoot", Collections.singletonList(weaponSelectionState),
            "grab", Arrays.asList(new MoveState(0, 1), new GrabState()),
            "grab_a", Arrays.asList(new MoveState(0, 2), new GrabState()),
            "shoot_a", Arrays.asList(new MoveState(0, 1), weaponSelectionState),
            "shoot_f1", Arrays.asList(new MoveState(0, 1), new SelectToReloadState(), weaponSelectionState),
            "move_f1", Collections.singletonList(new MoveState(0, 4)),
            "grab_f1", Arrays.asList(new MoveState(0, 2), new GrabState()),
            "shoot_f2", Arrays.asList(new MoveState(0, 2), new SelectToReloadState(), weaponSelectionState),
            "grab_f2", Arrays.asList(new MoveState(0, 3), new GrabState())
    );

    public GameController(Game game) {
        this.name = UUID.randomUUID();
        this.usersByID = new HashMap<>();
        canJoin = true;
        this.game = game;
        // this.setState(new TurnState());
    }

    void enqueue(Event event) {
        eventQueue.add(event);
    }

    @Override
    public void run() {
        try {
            eventQueue.take().accept(this);
        } catch (InterruptedException ignore) {
        }
    }

    void visit(SelectPowerUpEvent event) {
        state.selectPowerUp(event.getPlayer(), event.getPowerUps());
    }

    void visit(SelectWeaponToReloadEvent event) {
        state.selectWeaponToReload(event.getPlayer(), event.getWeapons());
    }

    void visit(SelectWeaponFireModeEvent event) {
        state.selectWeaponFireMode(event.getPlayer(), event.getWeapon(), event.getFireModes());
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

    //TODO REMOVE IMPLEMENT USING GAME
    public HashMap<String, Player> getUsersByID() {
        return usersByID;
    }

    public void select(int[] selections, Player player) {
        state.select(selections, player);
    }

    public void setState(State state) {
        this.state = state;
        this.state.onEnter();
    }

    private void nextState() {
        if (!stateQueue.isEmpty()) {
            setState(stateQueue.pop());
        } else {
            setState(new TurnState());
        }
    }

    interface State {
        default void logout(GameController controller, String id) {
            controller.getUsersByID().get(id).setInactive();
            for (Player u : controller.getUsersByID().values())
                u.getClient().send(new String("Logout by a player"));
        }

        default void sendText(GameController controller, String text, String id) {
            controller.getUsersByID().get(id).getClient().send(new String("Unrecognised command"));
        }


        default void select(int[] selections, Player player) {

        }

        default void onEnter() {
        }

        default void selectPowerUp(Player player, PowerUp[] powerUps) {
            //TODO
        }

        default void selectWeaponToReload(Player player, Weapon[] weapons) {
            //TODO
        }

        default void selectWeaponFireMode(Player player, Weapon weapon, FireMode[] fireModes) {
            //TODO
        }

        default void selectGrabbable(Player player, Grabbable grabbable) {
            //TODO
        }

        default void selectAction(Player player, Action action) {
            //TODO
        }

        default void selectTargettable(Player player, Targettable[] targettables) {
            //TODO
        }

        default void selectColor(Player player, int color) {
            //TODO
        }

    }

    private class TurnState implements State {
        private List<String> possibleAction;

        @Override
        public void onEnter() {
            Player current = game.nextPlayer();
            if (current.getFigure().getLocation() == null)
                setState(new SelectSpawnState(current, this));
            //possibleAction = new ArrayList<>();
            //if (game.isFrenzy() && game.getPlayers().stream().noneMatch(x -> x.getFigure().isFrenzyTurnLeft())) {
            //    setState(new EndGameState());
            //    nextState();
            //} else if (game.currentPlayer().getFigure().getSquare() == null) {
            //    setState(new SelectSpawnState(this));
            //} else if (game.currentPlayer().getFigure().getRemainingActions() == 0) {
            //   stateQueue.addLast(new SelectToReloadState());
            //    stateQueue.addLast(new EndOfTurnState());
            //  if (game.isFrenzy() && game.getRemainingKills() <= 0)
            //       game.currentPlayer().getFigure().setFrenzyTurnLeft(false);
            //  nextState();
            //} else {
            possibleAction = game.currentPlayer().getFigure().getPossibleActions(game.getPlayers().get(0).getFigure().isFrenzyTurnLeft(),
                    game.isFrenzy() && game.getRemainingKills() <= 0);
            //game.currentPlayer().getClient().send(new String("lists all possible actions based on the model"));
            //}
        }

        @Override
        public void select(int[] selection, Player player) {
            if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= possibleAction.size()) ||
                    player != game.currentPlayer())
                return;
            if (possibleAction.get(selection[0]).equals("newton")) {
                setState(new SelectPowerUpState(PowerUpType.NEWTON, Set.of(game.currentPlayer())));
            } else if (possibleAction.get(selection[0]).equals("teleporter")) {
                setState(new SelectPowerUpState(PowerUpType.TELEPORTER, Set.of(game.currentPlayer())));
            } else if (actionMap.keySet().contains(possibleAction.get(selection[0]))) {
                game.currentPlayer().getFigure().setRemainingActions(game.currentPlayer().getFigure().getRemainingActions() - 1);
                actionMap.get(possibleAction.get(selection[0])).forEach(x -> stateQueue.addLast(x));
                nextState();
            }
        }
    }

    private class SelectSpawnState implements State {
        private final Player player;
        private final State nextState;
        private final PowerUp powerUp;

        public SelectSpawnState(Player player, State nextState) {
            this.player = player;
            this.nextState = nextState;
            powerUp = game.drawPowerup();
            // TODO SEND
        }

        @Override
        public void select(int[] selection, Player player) {
            if (selection.length > 1 || player != this.player)
                return; // TODO INVALID CHOICE
            int i = selection[0];
            Figure figure = player.getFigure();
            PowerUp choice;
            if (i < 0)
                choice = powerUp;
            else {
                List<PowerUp> powerUps = figure.getPowerUps();
                if (powerUps.size() < i)
                    return; // TODO INVALID CHOICE
                choice = powerUps.remove(i);
                powerUps.add(powerUp);
            }
            figure.moveTo(choice.getSpawn());
            choice.discard();
            setState(nextState);
        }
    }

    private class GrabState implements State {
        private List<Grabbable> choices;

        @Override
        public void onEnter() {
            choices = new ArrayList<>(game.currentPlayer().getFigure().getLocation().peek());
            //game.currentPlayer().getClient().send(new String("list of possible grabbable"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selections, Player player) {
            if (selections.length != 1 || Arrays.stream(selections).anyMatch(x -> x > choices.size() || x < 0)) return;
            try {
                game.currentPlayer().getFigure().getLocation().grab(game.currentPlayer().getFigure(), choices.get(selections[0]));
                nextState();
            } catch (IllegalStateException e) {
                setState(new DiscardWeaponState());
            }
        }
    }

    private class DiscardWeaponState implements State {
        private List<Weapon> choices;

        @Override
        public void onEnter() {
            choices = new ArrayList<>(game.currentPlayer().getFigure().getWeapons());
            //game.currentPlayer().getClient().send(new String("list of possible weapon to discard"));
            System.out.println("choices");
        }

        @Override
        public void select(int[] selections, Player player) {
            if (selections.length != 1 || Arrays.stream(selections).anyMatch(x -> x > choices.size() || x < 0)) return;
            game.currentPlayer().getFigure().getLocation().refill(choices.get(selections[0]));
            game.currentPlayer().getFigure().getWeapons().remove(choices.get(selections[0]));
            nextState();
        }
    }

    private class FireModeSelectionState implements State {
        private Weapon currWeapon;

        FireModeSelectionState(Weapon w) {
            currWeapon = w;
        }

        @Override
        public void onEnter() {
            //game.currentPlayer().getClient().send(new String("possible firemodes"));
            System.out.println(currWeapon.getFireModes());
        }

        @Override
        public void select(int[] selection, Player player) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= currWeapon.getFireModes().size()))
                return;
            List<FireMode> selectedFireModes = Arrays.stream(selection).mapToObj(x -> currWeapon.getFireModes().get(x)).collect(Collectors.toList());
            //check if the player can afford the cost
            if (currWeapon.validateFireModes(selectedFireModes) && player == game.currentPlayer() &&
                    game.currentPlayer().getFigure().getAmmo()
                            .add(game.currentPlayer().getFigure().getPowerUps()
                                    .stream().map(PowerUp::getAmmo).reduce(AmmoCube::add).orElseGet(AmmoCube::new))
                            .greaterEqThan(selectedFireModes
                                    .stream().map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new))) {
                currWeapon.unload();
                setState(new PayAmmoState(selectedFireModes.stream().map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new),
                        new FireState(new FireSequence(game.currentPlayer().getFigure(), game.getBoard(),
                                FireMode.flatSteps(selectedFireModes)))));
            }
        }
    }

    private class FireState implements State {
        private FireSequence fireSequence;

        public FireState(FireSequence fireSequence) {
            this.fireSequence = fireSequence;
        }

        @Override
        public void onEnter() {
            game.currentPlayer().getFigure().setPossibleTargets(fireSequence.getTargets());
            //game.currentPlayer().getClient().send(new String("possibili targets"));
        }
        /*
        @Override
        public void select(int[] selection, Player player) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size())) return;
            Set<Targettable> selectedTargets = Arrays.stream(selection).mapToObj(x -> choices.get(x)).collect(Collectors.toSet());
            if (fireSequence.validateTargets(selectedTargets) && player == game.currentPlayer()) {
                fireSequence.run(selectedTargets);
                choices = new ArrayList<>(fireSequence.getTargets());
                //game.currentPlayer().getClient().send(new String("possibili targets"));
                System.out.println(choices);
                if (!fireSequence.hasNext()) {
                    if (game.currentPlayer().getFigure().getPowerUps().stream().anyMatch(x -> x.getType().equals(PowerUpType.SCOPE)))
                        stateQueue.addLast(new SelectPowerUpState(PowerUpType.SCOPE, Set.of(game.currentPlayer())));
                    Set<Player> toAsk = game.getBoard().getDamaged().stream()
                            .filter(x -> x.getPowerUps().stream().anyMatch(y -> y.getType().equals(PowerUpType.TAGBACK))).map(Figure::getPlayer)
                            .collect(Collectors.toSet());
                    if (!toAsk.isEmpty())
                        stateQueue.addLast(new SelectPowerUpState(PowerUpType.TAGBACK, toAsk));
                    game.getPlayers().forEach(x -> x.getFigure().applyMarks());
                    nextState();
                }
            }
        }*/
    }

    private class ReloadState implements State {
        private List<Weapon> toReload;

        public ReloadState(List<Weapon> toReload) {
            this.toReload = toReload;
        }

        @Override
        public void onEnter() {
            toReload.forEach(Weapon::load);
            System.out.println("reloaded");
            nextState();

        }
    }

    private class SelectToReloadState implements State {
        private List<Weapon> choices;

        @Override
        public void onEnter() {
            choices = new ArrayList<>(game.currentPlayer().getFigure().getWeapons());
            //game.currentPlayer().getClient().send(new String("list of possible weapons"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selection, Player player) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size()))
                return;
            if (selection.length == 0)
                nextState();
            else {
                List<Weapon> selectedWeapons = Arrays.stream(selection).mapToObj(x -> choices.get(x)).collect(Collectors.toList());
                AmmoCube totalCost = selectedWeapons
                        .stream().map(Weapon::getReloadCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new);
                //check if the player can afford the cost
                if (player == game.currentPlayer() &&
                        game.currentPlayer().getFigure().getAmmo()
                                .add(game.currentPlayer().getFigure().getPowerUps()
                                        .stream().map(PowerUp::getAmmo).reduce(AmmoCube::add).orElseGet(AmmoCube::new))
                                .greaterEqThan(totalCost)) {
                    setState(new PayAmmoState(totalCost, new ReloadState(selectedWeapons)));
                }
            }
        }
    }

    private class PayAnyColorState implements State {
        private List<AmmoCube> choices;
        private State nextState;

        public PayAnyColorState(State nextState) {
            this.nextState = nextState;
        }

        @Override
        public void onEnter() {
            //TODO color as an int?
            choices = new ArrayList<>(Arrays.asList(new AmmoCube(1, 0, 0), new AmmoCube(0, 1, 0), new AmmoCube(0, 0, 1)));
            //game.currentPlayer().getClient().send(new String("lists possible colors"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selection, Player player) {
            if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size()) ||
                    player != game.currentPlayer())
                return;
            AmmoCube cost = choices.get(selection[0]);
            setState(new PayAmmoState(cost, nextState));
        }
    }

    private class MoveState implements State {
        private State nextState;
        private List<AbstractSquare> choices;
        private int maxMove;
        private int minMove;

        public MoveState(int minMove, int maxMove) {
            this.minMove = minMove;
            this.maxMove = maxMove;
        }

        @Override
        public void onEnter() {
            choices = new ArrayList<>(game.currentPlayer().getFigure().getLocation().atDistance(minMove, maxMove));
            //game.currentPlayer().getClient().send(new String("possible cells"));
            System.out.println(choices.stream().map(x -> String.valueOf(x.getCoordinates()[0]) + String.valueOf(x.getCoordinates()[1])).collect(Collectors.toList()));

        }

        @Override
        public void select(int[] selection, Player player) {
            if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size()))
                return;
            AbstractSquare destination = choices.get(selection[0]);
            if (player == game.currentPlayer()) {
                game.currentPlayer().getFigure().moveTo(destination);
                nextState();
            }
        }
    }

    private class PayAmmoState implements State {
        private AmmoCube cost;
        private GameController.State nextState;
        private List<PowerUp> powerUpList;

        public PayAmmoState(AmmoCube cost, State nextState) {
            this.cost = cost;
            this.nextState = nextState;
        }

        @Override
        public void onEnter() {
            powerUpList = new ArrayList<>(game.currentPlayer().getFigure().getPowerUps());
            //game.currentPlayer().getClient().send(new String("possible powerup to discard"));
            System.out.println(powerUpList);
        }

        @Override
        public void select(int[] selection, Player player) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= powerUpList.size())) return;
            List<PowerUp> toDiscard = Arrays.stream(selection)
                    .distinct().mapToObj(x -> powerUpList.get(x)).collect(Collectors.toList());
            if (player == game.currentPlayer() &&
                    (toDiscard.stream().map(PowerUp::getAmmo)
                            .reduce(game.currentPlayer().getFigure().getAmmo(), AmmoCube::add)).greaterEqThan(cost)) {
                game.currentPlayer().getFigure().getPowerUps().removeAll(toDiscard);
                cost = cost.sub(toDiscard.stream().map(PowerUp::getAmmo).reduce(new AmmoCube(), AmmoCube::add));
                game.currentPlayer().getFigure().getAmmo().sub(cost);
                toDiscard.forEach(PowerUp::discard);
                setState(nextState);
            }
        }


    }

    private class EndOfTurnState implements State {
        private Map<Player, List<PowerUp>> choiceMap;

        @Override
        public void onEnter() {
            //TODO NB DON't CONSIDER INACTIVE PLAYERS
            game.getPlayers().forEach(x -> x.getFigure().resolveDeath(game));
            List<Player> deadPlayers = game.getPlayers().stream()
                    .filter(x -> x.getFigure().getLocation() == null).collect(Collectors.toList());
            //TODO ADDITIONAL POWERUP
            //deadPlayers.forEach(x -> x.getFigure().addPowerUp(game.drawPowerup()));
            choiceMap = new HashMap<>();
            deadPlayers.forEach(x -> choiceMap.put(x, new ArrayList<>(x.getFigure().getPowerUps())));
            //deadPlayers.forEach(x->x.getClient().send(new String("possible powerup to discard to spawn")));
            System.out.println(choiceMap);
            if (choiceMap.keySet().isEmpty()) endTurn();
            if (deadPlayers.size() > 1) game.currentPlayer().getFigure().addPoints(1); //Additional point for doublekill
        }

        @Override
        public void select(int[] selection, Player player) {
            if (choiceMap.keySet().contains(player)) {
                if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0
                        || x >= choiceMap.get(player).size())) return;
                PowerUp toDiscard = choiceMap.get(player).get(selection[0]);
                player.getFigure().moveTo(toDiscard.getSpawn());
                player.getFigure().getPowerUps().remove(toDiscard);
                toDiscard.discard();
                choiceMap.remove(player);
                if (choiceMap.keySet().isEmpty())
                    endTurn();
            }
        }

        private void endTurn() {
            game.fillBoard();
            game.nextPlayer();
            if (game.isFrenzy() && game.getRemainingKills() <= 0) {
                game.toggleFrenzy();
                if (game.getPlayers().get(0).getFigure().isFrenzyTurnLeft())
                    game.currentPlayer().getFigure().setRemainingActions(2);
                else game.currentPlayer().getFigure().setRemainingActions(1);
            }
            setState(new TurnState());
        }

    }

    private class SelectPowerUpState implements State {
        private Map<Player, List<PowerUp>> playerListMap;

        public SelectPowerUpState(PowerUpType type, Set<Player> toAsk) {
            playerListMap = toAsk.stream().collect(Collectors.toMap(Function.identity(), x -> x.getFigure().getPowerUps().stream().filter(y -> y.getType().equals(type)).collect(Collectors.toList())));
        }

        @Override
        public void onEnter() {
            //playerListMap.keySet().forEach(x->x.getClient().send(new String("possible powerup to discard")));
            System.out.println(playerListMap);
        }

        @Override
        public void select(int[] selection, Player player) {
            if (!playerListMap.keySet().contains(player) ||
                    Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= playerListMap.get(player).size()))
                return;
            List<PowerUp> toUse = Arrays.stream(selection).mapToObj(x -> playerListMap.get(player).get(x)).collect(Collectors.toList());
            toUse.forEach(x -> game.currentPlayer().getFigure().getPowerUps().remove(x));
            toUse.forEach(x -> {
                        if (x.getType().equals(PowerUpType.SCOPE)) {
                            //stateQueue.addLast(new PayAnyColorState(new FireState(new FireSequence(player.getFigure(), game.getBoard(), x.()))));
                        } else {
                            //stateQueue.addLast(new FireState(new FireSequence(player.getFigure(), game.getBoard(), x.())));
                        }
                    }
            );
            toUse.forEach(PowerUp::discard);
            playerListMap.remove(player);
            if (playerListMap.keySet().isEmpty()) {
                nextState();
            }
        }
    }

    private class EndGameState implements State {
        @Override
        public void onEnter() {
            System.out.println("END GAME");
        }
    }

}

