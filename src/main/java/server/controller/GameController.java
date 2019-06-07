package server.controller;

import server.connection.VirtualClient;
import server.model.*;
import server.model.board.AbstractSquare;
import server.model.board.Figure;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.FireSequence;
import server.model.weapon.Weapon;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameController {

    private final int maxUsers = 5;
    private HashMap<String, Player> usersByID;
    private UUID name;
    private State state;
    private boolean canJoin;
    private int countdownDuration;
    private Game game;
    private Deque<State> stateQueue = new ArrayDeque<>();
    private State weaponSelectionState = new State() {
        private List<Weapon> choices;

        @Override
        public void onEnter(GameController controller) {
            choices = controller.game.currentPlayer().getFigure().getWeapons().stream().filter(Weapon::isLoaded).collect(Collectors.toList());
            //controller.game.currentPlayer().getClient().send(new String("player's weapons list"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (selection.length == 1 && selection[0] >= 0 &&
                    selection[0] < choices.size() &&
                    id.equals(game.currentPlayer().getName())) {
                controller.setState(new FireModeSelectionState(choices.get(selection[0])));
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


    public GameController(int countdownDuration, Game game) {
        this.name = UUID.randomUUID();
        this.usersByID = new HashMap<>();
        canJoin = true;
        this.countdownDuration = countdownDuration;
        this.game = game;
        game.nextPlayer();
        this.setState(new TurnState());
    }

    public boolean canJoin() {
        return canJoin;
    }

    public void login(String name, VirtualClient virtualView, String id) {
        System.out.println("Login by : " + name);
        usersByID.put(id, new Player(name));
        virtualView.send(
                new String(">> You are now connected to " + this.name + ", send message writing in the chat\n" +
                        ">> Other users:  " + usersByID.values().stream().map(Player::getName).collect(Collectors.joining(" "))));
        canJoin = (usersByID.values().size() < maxUsers);
        state.login(this);
    }

    public void select(int[] selections, String id) {
        state.select(selections, this, id);
    }

    public void sendText(String text, String id) {
        state.sendText(this, text, id);
    }

    public void logout(String id) {
        if (!usersByID.keySet().contains(id)) return;
        System.out.println("Logout by : " + usersByID.get(id).getName());
        state.logout(this, id);
    }

    public Map<String, Player> getUsersByID() {
        return game.getPlayers().stream().collect(Collectors.toMap(Player::getName, Function.identity()));
    }

    public void setState(State state) {
        this.state = state;
        this.state.onEnter(this);
    }

    private void nextState() {
        if (!stateQueue.isEmpty()) {
            setState(stateQueue.pop());
        } else {
            setState(new TurnState());
        }
    }

    private interface State {
        default void logout(GameController controller, String id) {
            controller.getUsersByID().get(id).setInactive();
            for (Player u : controller.getUsersByID().values())
                u.getClient().send(new String("Logout by a player"));
        }

        default void sendText(GameController controller, String text, String id) {
            controller.getUsersByID().get(id).getClient().send(new String("Unrecognised command"));
        }

        default void login(GameController controller) {
            for (Player u : controller.getUsersByID().values())
                u.getClient().send(new String("Player tried to login in invalid state"));
        }

        default void onEnter(GameController controller) {
        }

        default void select(int[] selections, GameController controller, String id) {
            controller.getUsersByID().get(id).getClient().send(new String("Unrecognised command"));
        }

    }

    private class WaitingState implements State {
        private Timer countdownTimer;
        private boolean startedCountdown;
        private Game.Builder gameBuilder = new Game.Builder();

        public WaitingState() {
            startedCountdown = false;
        }

        @Override
        public void sendText(GameController controller, String text, String id) {
            if (!controller.getUsersByID().keySet().contains(id)) return;
            String toSend = new String("LobbyChat>> " + controller.getUsersByID().get(id).getName() + " : " + text);
            for (Player usr : controller.getUsersByID().values())
                usr.getClient().send(toSend);
        }

        @Override
        public void login(GameController controller) {
            if (controller.getUsersByID().size() == 5) {
                resetCountdown();
                controller.setState(new TurnState());
                canJoin = false;
            } else if (controller.getUsersByID().size() >= 3 && !startedCountdown) {
                startCountdown(controller);
            }
        }

        @Override
        public void logout(GameController controller, String id) {
            getUsersByID().remove(id);
            if (controller.getUsersByID().size() < 3) {
                resetCountdown();
            }
            canJoin = (getUsersByID().values().size() < maxUsers);
        }

        private void startCountdown(GameController controller) {
            startedCountdown = true;
            countdownTimer = new Timer();
            countdownTimer.schedule(new TimerTask() {
                int i = countdownDuration;

                @Override
                public void run() {
                    if (i > 0) {
                        String toSend = new String(Integer.toString(i));
                        for (Player usr : controller.getUsersByID().values())
                            usr.getClient().send(toSend);
                        i--;
                    } else {
                        controller.setState(new TurnState());
                    }
                }
            }, new Date(), 1000);
        }

        private void resetCountdown() {
            countdownTimer.cancel();
            countdownTimer.purge();
            startedCountdown = false;
        }
    }

    private class GrabState implements State {
        private List<Grabbable> choices;

        @Override
        public void onEnter(GameController controller) {
            choices = new ArrayList<>(game.currentPlayer().getFigure().getLocation().peek());
            //game.currentPlayer().getClient().send(new String("list of possible grabbable"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selections, GameController controller, String id) {
            if (selections.length != 1 || Arrays.stream(selections).anyMatch(x -> x > choices.size() || x < 0)) return;
            try {
                game.currentPlayer().getFigure().getLocation().grab(game.currentPlayer().getFigure(), choices.get(selections[0]));
                controller.nextState();
            } catch (IllegalStateException e) {
                controller.setState(new DiscardWeaponState());
            }
        }
    }

    private class DiscardWeaponState implements State {
        private List<Weapon> choices;

        @Override
        public void onEnter(GameController controller) {
            choices = new ArrayList<>(game.currentPlayer().getFigure().getWeapons());
            //game.currentPlayer().getClient().send(new String("list of possible weapon to discard"));
            System.out.println("choices");
        }

        @Override
        public void select(int[] selections, GameController controller, String id) {
            if (selections.length != 1 || Arrays.stream(selections).anyMatch(x -> x > choices.size() || x < 0)) return;
            game.currentPlayer().getFigure().getLocation().refill(choices.get(selections[0]));
            game.currentPlayer().getFigure().getWeapons().remove(choices.get(selections[0]));
            controller.nextState();
        }
    }

    private class FireModeSelectionState implements State {
        private Weapon currWeapon;

        FireModeSelectionState(Weapon w) {
            currWeapon = w;
        }

        @Override
        public void onEnter(GameController controller) {
            //game.currentPlayer().getClient().send(new String("possible firemodes"));
            System.out.println(currWeapon.getFireModes());
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= currWeapon.getFireModes().size()))
                return;
            List<FireMode> selectedFireModes = Arrays.stream(selection).mapToObj(x -> currWeapon.getFireModes().get(x)).collect(Collectors.toList());
            //check if the player can afford the cost
            if (currWeapon.validateFireModes(selectedFireModes) && id.equals(game.currentPlayer().getName()) &&
                    game.currentPlayer().getFigure().getAmmo()
                            .add(game.currentPlayer().getFigure().getPowerUps()
                                    .stream().map(PowerUp::getAmmo).reduce(AmmoCube::add).orElseGet(AmmoCube::new))
                            .greaterEqThan(selectedFireModes
                                    .stream().map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new))) {
                currWeapon.unload();
                controller.setState(new PayAmmoState(selectedFireModes.stream().map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new),
                        new FireState(new FireSequence(game.currentPlayer().getFigure(), game.getBoard(),
                                FireMode.flatSteps(selectedFireModes)))));
            }
        }
    }

    private class FireState implements State {
        private FireSequence fireSequence;
        private List<Targettable> choices;

        public FireState(FireSequence fireSequence) {
            this.fireSequence = fireSequence;
        }

        @Override
        public void onEnter(GameController controller) {
            choices = new ArrayList<>(fireSequence.getTargets());
            //game.currentPlayer().getClient().send(new String("possibili targets"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size())) return;
            Set<Targettable> selectedTargets = Arrays.stream(selection).mapToObj(x -> choices.get(x)).collect(Collectors.toSet());
            if (fireSequence.validateTargets(selectedTargets) && id.equals(game.currentPlayer().getName())) {
                fireSequence.run(selectedTargets);
                choices = new ArrayList<>(fireSequence.getTargets());
                //game.currentPlayer().getClient().send(new String("possibili targets"));
                System.out.println(choices);
                if (!fireSequence.hasNext()) {
                    if (game.currentPlayer().getFigure().getPowerUps().stream().anyMatch(x -> x.getType().equals(PowerUpType.SCOPE)))
                        stateQueue.addLast(new SelectPowerUpState(PowerUpType.SCOPE, Set.of(game.currentPlayer())));
                    Set<Player> toAsk = game.getBoard().getDamaged().stream()
                            .filter(x -> x.getPowerUps().stream().anyMatch(y -> y.getType().equals(PowerUpType.TAGBACK))).map(Figure::getOwner)
                            .collect(Collectors.toSet());
                    if (!toAsk.isEmpty())
                        stateQueue.addLast(new SelectPowerUpState(PowerUpType.TAGBACK, toAsk));
                    game.getPlayers().forEach(x -> x.getFigure().applyMarks());
                    controller.nextState();
                }
            }
        }
    }

    private class ReloadState implements State {
        private List<Weapon> toReload;

        public ReloadState(List<Weapon> toReload) {
            this.toReload = toReload;
        }

        @Override
        public void onEnter(GameController controller) {
            toReload.forEach(Weapon::load);
            System.out.println("reloaded");
            controller.nextState();

        }
    }

    private class SelectToReloadState implements State {
        private List<Weapon> choices;

        @Override
        public void onEnter(GameController controller) {
            choices = new ArrayList<>(game.currentPlayer().getFigure().getWeapons());
            //game.currentPlayer().getClient().send(new String("list of possible weapons"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size()))
                return;
            if (selection.length == 0)
                controller.nextState();
            else {
                List<Weapon> selectedWeapons = Arrays.stream(selection).mapToObj(x -> choices.get(x)).collect(Collectors.toList());
                AmmoCube totalCost = selectedWeapons
                        .stream().map(Weapon::getReloadCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new);
                //check if the player can afford the cost
                if (id.equals(game.currentPlayer().getName()) &&
                        game.currentPlayer().getFigure().getAmmo()
                                .add(game.currentPlayer().getFigure().getPowerUps()
                                        .stream().map(PowerUp::getAmmo).reduce(AmmoCube::add).orElseGet(AmmoCube::new))
                                .greaterEqThan(totalCost)) {
                    controller.setState(new PayAmmoState(totalCost, new ReloadState(selectedWeapons)));
                }
            }
        }
    }

    private class TurnState implements State {
        private List<String> possibleAction;

        @Override
        public void onEnter(GameController controller) {
            possibleAction = new ArrayList<>();
            if (game.isFrenzy() && game.getPlayers().stream().noneMatch(x -> x.getFigure().isFrenzyTurnLeft())) {
                setState(new EndGameState());
                nextState();
            } else if (game.currentPlayer().getFigure().getLocation() == null) {
                setState(new SelectSpawnState(this));
            } else if (game.currentPlayer().getFigure().getRemainingActions() == 0) {
                stateQueue.addLast(new SelectToReloadState());
                stateQueue.addLast(new EndOfTurnState());
                if (game.isFrenzy() && game.getRemainingKills() <= 0)
                    game.currentPlayer().getFigure().setFrenzyTurnLeft(false);
                nextState();
            } else {
                possibleAction = game.currentPlayer().getFigure().getPossibleActions(game.getPlayers().get(0).getFigure().isFrenzyTurnLeft(),
                        game.isFrenzy() && game.getRemainingKills() <= 0);
                //game.currentPlayer().getClient().send(new String("lists all possible actions based on the model"));
                System.out.println(possibleAction);
            }
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= possibleAction.size()) ||
                    !id.equals(game.currentPlayer().getName()))
                return;
            if (possibleAction.get(selection[0]).equals("newton")) {
                controller.setState(new SelectPowerUpState(PowerUpType.NEWTON, Set.of(game.currentPlayer())));
            } else if (possibleAction.get(selection[0]).equals("teleporter")) {
                controller.setState(new SelectPowerUpState(PowerUpType.TELEPORTER, Set.of(game.currentPlayer())));
            } else if (actionMap.keySet().contains(possibleAction.get(selection[0]))) {
                game.currentPlayer().getFigure().setRemainingActions(game.currentPlayer().getFigure().getRemainingActions() - 1);
                actionMap.get(possibleAction.get(selection[0])).forEach(x -> stateQueue.addLast(x));
                controller.nextState();
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
        public void onEnter(GameController controller) {
            //TODO color as an int?
            choices = new ArrayList<>(Arrays.asList(new AmmoCube(1, 0, 0), new AmmoCube(0, 1, 0), new AmmoCube(0, 0, 1)));
            //game.currentPlayer().getClient().send(new String("lists possible colors"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size()) ||
                    !id.equals(game.currentPlayer().getName()))
                return;
            AmmoCube cost = choices.get(selection[0]);
            controller.setState(new PayAmmoState(cost, nextState));
        }
    }

    private class SelectSpawnState implements State {
        private State nextState;
        private List<PowerUp> choices;

        public SelectSpawnState(State nextState) {
            this.nextState = nextState;
        }

        @Override
        public void onEnter(GameController controller) {
            game.currentPlayer().getFigure().addPowerUp(game.drawPowerup()); //draw another powerup(should already have 1)
            choices = new ArrayList<>(game.currentPlayer().getFigure().getPowerUps());
            //game.currentPlayer().getClient().send(new String("possible powerup to discard"));
            System.out.println(choices);
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size()))
                return;
            PowerUp toDiscard = choices.get(selection[0]);
            if (id.equals(game.currentPlayer().getName())) {
                game.currentPlayer().getFigure().getPowerUps().remove(toDiscard);
                toDiscard.discard();
                game.currentPlayer().getFigure().moveTo(toDiscard.getSpawn());
                controller.setState(nextState);
            }
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
        public void onEnter(GameController controller) {
            choices = new ArrayList<>(game.currentPlayer().getFigure().getLocation().atDistance(minMove, maxMove));
            //game.currentPlayer().getClient().send(new String("possible cells"));
            System.out.println(choices.stream().map(x -> String.valueOf(x.getCoordinates()[0]) + String.valueOf(x.getCoordinates()[1])).collect(Collectors.toList()));

        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size()))
                return;
            AbstractSquare destination = choices.get(selection[0]);
            if (id.equals(game.currentPlayer().getName())) {
                game.currentPlayer().getFigure().moveTo(destination);
                controller.nextState();
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
        public void onEnter(GameController controller) {
            powerUpList = new ArrayList<>(game.currentPlayer().getFigure().getPowerUps());
            //game.currentPlayer().getClient().send(new String("possible powerup to discard"));
            System.out.println(powerUpList);
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= powerUpList.size())) return;
            List<PowerUp> toDiscard = Arrays.stream(selection)
                    .distinct().mapToObj(x -> powerUpList.get(x)).collect(Collectors.toList());
            if (id.equals(game.currentPlayer().getName()) &&
                    (toDiscard.stream().map(PowerUp::getAmmo)
                            .reduce(game.currentPlayer().getFigure().getAmmo(), AmmoCube::add)).greaterEqThan(cost)) {
                game.currentPlayer().getFigure().getPowerUps().removeAll(toDiscard);
                cost = cost.sub(toDiscard.stream().map(PowerUp::getAmmo).reduce(new AmmoCube(), AmmoCube::add));
                game.currentPlayer().getFigure().getAmmo().sub(cost);
                toDiscard.forEach(PowerUp::discard);
                controller.setState(nextState);
            }
        }


    }

    private class EndOfTurnState implements State {
        private Map<Player, List<PowerUp>> choiceMap;

        @Override
        public void onEnter(GameController controller) {
            //TODO NB DON't CONSIDER INACTIVE PLAYERS
            game.getPlayers().forEach(x -> x.getFigure().resolveDeath(game));
            List<Player> deadPlayers = game.getPlayers().stream()
                    .filter(x -> x.getFigure().getLocation() == null).collect(Collectors.toList());
            deadPlayers.forEach(x -> x.getFigure().addPowerUp(game.drawPowerup()));
            choiceMap = new HashMap<>();
            deadPlayers.forEach(x -> choiceMap.put(x, new ArrayList<>(x.getFigure().getPowerUps())));
            //deadPlayers.forEach(x->x.getClient().send(new String("possible powerup to discard to spawn")));
            System.out.println(choiceMap);
            if (choiceMap.keySet().isEmpty()) endTurn(controller);
            if (deadPlayers.size() > 1) game.currentPlayer().getFigure().addPoints(1); //Additional point for doublekill
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (choiceMap.keySet().stream().map(Player::getName).collect(Collectors.toList()).contains(id)) {
                Player selector = getUsersByID().get(id);
                if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0
                        || x >= choiceMap.get(selector).size())) return;
                PowerUp toDiscard = choiceMap.get(selector).get(selection[0]);
                selector.getFigure().moveTo(toDiscard.getSpawn());
                selector.getFigure().getPowerUps().remove(toDiscard);
                toDiscard.discard();
                choiceMap.remove(selector);
                if (choiceMap.keySet().isEmpty())
                    endTurn(controller);
            }
        }

        private void endTurn(GameController controller) {
            game.fillBoard();
            game.nextPlayer();
            if (game.isFrenzy() && game.getRemainingKills() <= 0) {
                game.toggleFrenzy();
                if (game.getPlayers().get(0).getFigure().isFrenzyTurnLeft())
                    game.currentPlayer().getFigure().setRemainingActions(2);
                else game.currentPlayer().getFigure().setRemainingActions(1);
            }
            controller.setState(new TurnState());
        }

    }

    private class SelectPowerUpState implements State {
        private Map<Player, List<PowerUp>> playerListMap;

        public SelectPowerUpState(PowerUpType type, Set<Player> toAsk) {
            playerListMap = toAsk.stream().collect(Collectors.toMap(Function.identity(), x -> x.getFigure().getPowerUps().stream().filter(y -> y.getType().equals(type)).collect(Collectors.toList())));
        }

        @Override
        public void onEnter(GameController controller) {
            //playerListMap.keySet().forEach(x->x.getClient().send(new String("possible powerup to discard")));
            System.out.println(playerListMap);
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (!playerListMap.keySet().contains(getUsersByID().get(id)) ||
                    Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= playerListMap.get(getUsersByID().get(id)).size()))
                return;
            List<PowerUp> toUse = Arrays.stream(selection).mapToObj(x -> playerListMap.get(getUsersByID().get(id)).get(x)).collect(Collectors.toList());
            toUse.forEach(x -> game.currentPlayer().getFigure().getPowerUps().remove(x));
            toUse.forEach(x -> {
                        if (x.getType().equals(PowerUpType.SCOPE)) {
                            stateQueue.addLast(new PayAnyColorState(new FireState(new FireSequence(getUsersByID().get(id).getFigure(), game.getBoard(), x.getFireSteps()))));
                        } else {
                            stateQueue.addLast(new FireState(new FireSequence(getUsersByID().get(id).getFigure(), game.getBoard(), x.getFireSteps())));
                        }
                    }
            );
            toUse.forEach(PowerUp::discard);
            playerListMap.remove(getUsersByID().get(id));
            if (playerListMap.keySet().isEmpty()) {
                controller.nextState();
            }
        }
    }

    private class EndGameState implements State {
        @Override
        public void onEnter(GameController controller) {
            System.out.println("END GAME");
        }
    }
}

