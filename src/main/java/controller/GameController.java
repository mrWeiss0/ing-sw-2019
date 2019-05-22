package controller;

import connection.messages.responses.Response;
import connection.messages.responses.TextResponse;
import connection.server.VirtualView;
import model.AmmoCube;
import model.Game;
import model.Player;
import model.PowerUp;
import model.board.AbstractSquare;
import model.board.Targettable;
import model.weapon.FireMode;
import model.weapon.FireSequence;
import model.weapon.Weapon;

import java.util.*;
import java.util.stream.Collectors;

public class GameController {

    private final int maxUsers = 5;
    private transient HashMap<String, Player> usersByID;
    private UUID name;
    private State state;
    private boolean canJoin;
    private int countdownDuration;
    private Game game;

    public GameController(int countdownDuration) {
        this.name = UUID.randomUUID();
        this.usersByID = new HashMap<>();
        this.state = new WaitingState();
        canJoin = true;
        this.countdownDuration = countdownDuration;
    }

    public boolean canJoin() {
        return canJoin;
    }


    public void login(String name, VirtualView virtualView, String id) {
        System.out.println("Login by : " + name);
        usersByID.put(id, new Player(name, id, virtualView));
        virtualView.handle(
                new TextResponse(">> You are now connected to " + this.name + ", send message writing in the chat\n" +
                        ">> Other users:  " + usersByID.values().stream().map(Player::getName).collect(Collectors.joining(" "))));
        canJoin = (usersByID.values().size() < maxUsers);
        state.login(this);
    }

    //TODO IMPLEMENT CONTROLLER METHOD AS state.method()
    public void sendText(String text, String id) {
        state.sendText(this, text, id);
    }

    public void logout(String id) {
        if (!usersByID.keySet().contains(id)) return;
        System.out.println("Logout by : " + usersByID.get(id).getName());
        state.logout(this, id);
        canJoin = (usersByID.values().size() < maxUsers);
    }

    public Map<String, Player> getUsersByID() {
        return usersByID;
    }


    public void setState(State state) {
        this.state = state;
        this.state.onEnter(this);
    }

    private interface State {
        default void logout(GameController controller, String id) {
            controller.getUsersByID().get(id).setActive(false);
            for (Player u : controller.getUsersByID().values())
                u.getView().handle(new TextResponse("Logout by a player"));
        }

        default void sendText(GameController controller, String text, String id) {
            controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
        }

        default void login(GameController controller) {
            for (Player u : controller.getUsersByID().values())
                u.getView().handle(new TextResponse("Player tried to login in invalid state"));
        }

        default void onEnter(GameController controller) {
        }

        default void select(int[] selections, GameController controller, String id) {
            controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
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
            Response toSend = new TextResponse("LobbyChat>> " + controller.getUsersByID().get(id).getName() + " : " + text);
            for (Player usr : controller.getUsersByID().values())
                usr.getView().handle(toSend);
        }

        @Override
        public void login(GameController controller) {
            if (controller.getUsersByID().size() == 5) {
                resetCountdown();
                controller.setState(new GameController.WaitingState());
            } else if (controller.getUsersByID().size() >= 3 && !startedCountdown) {
                startCountdown(controller);
            }
        }

        @Override
        public void logout(GameController controller, String id) {
            usersByID.remove(id);
            if (controller.getUsersByID().size() < 3) {
                resetCountdown();
            }
        }

        private void startCountdown(GameController controller) {
            startedCountdown = true;
            countdownTimer = new Timer();
            countdownTimer.schedule(new TimerTask() {
                int i = countdownDuration;

                @Override
                public void run() {
                    if (i > 0) {
                        Response toSend = new TextResponse(Integer.toString(i));
                        for (Player usr : controller.getUsersByID().values())
                            usr.getView().handle(toSend);
                        i--;
                    } else {
                        controller.setState(new GameController.WaitingState());
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

    private State WeaponSelectionState = new State() {
        private List<Weapon> choices;

        @Override
        public void onEnter(GameController controller) {
            choices = controller.game.currentPlayer().getFigure().getWeapons().stream().filter(Weapon::isLoaded).collect(Collectors.toList());
            controller.game.currentPlayer().getView().handle(new TextResponse("player's weapons list"));
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (selection.length == 1 && selection[0] >= 0 &&
                    selection[0] < choices.size() &&
                    id.equals(game.currentPlayer().getId())) {
                controller.setState(new GameController.FireModeSelectionState(choices.get(selection[0])));
            }
        }
    };

    private class FireModeSelectionState implements State {
        private Weapon currWeapon;

        FireModeSelectionState(Weapon w) {
            currWeapon = w;
        }

        @Override
        public void onEnter(GameController controller) {
            game.currentPlayer().getView().handle(new TextResponse("possible firemodes"));
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= currWeapon.getFireModes().size()))
                return;
            List<FireMode> selectedFireModes = Arrays.stream(selection).mapToObj(x -> currWeapon.getFireModes().get(x)).collect(Collectors.toList());
            if (currWeapon.validateFireModes(selectedFireModes) && id.equals(game.currentPlayer().getId())) {
                controller.setState(new PayAmmoState(selectedFireModes.stream().map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new), new FireState(selectedFireModes)));
            }
        }
    }

    private class FireState implements State {
        private List<FireMode> fireModes;
        private FireSequence fireSequence;
        private List<Targettable> choices;

        public FireState(List<FireMode> fireModes) {
            this.fireModes = fireModes;
        }

        @Override
        public void onEnter(GameController controller) {
            fireSequence = new FireSequence(game.currentPlayer().getFigure(), game.getBoard(),
                    FireMode.flatSteps(fireModes));
            game.currentPlayer().getView().handle(new TextResponse("possibili targets"));
            choices = new ArrayList<>(fireSequence.getTargets());
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size())) return;
            Set<Targettable> selectedTargets = Arrays.stream(selection).mapToObj(x -> choices.get(x)).collect(Collectors.toSet());
            if (fireSequence.validateTargets(selectedTargets) && id.equals(game.currentPlayer().getId())) {
                fireSequence.run(selectedTargets);
                game.currentPlayer().getView().handle(new TextResponse("possibili targets"));
                choices = new ArrayList<>(fireSequence.getTargets());
                //TODO CHECK DAMAGED AND ASK FOR TAGBACK
                //TODO CHECK CURRENT PLAYER FOR SCOPE AND ASK
                if (!fireSequence.hasNext()) controller.setState(new TurnState());
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
            game.currentPlayer().getView().handle(new TextResponse("possible powerup to discard"));
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= powerUpList.size())) return;
            List<PowerUp> toDiscard = Arrays.stream(selection).distinct().mapToObj(x -> powerUpList.get(x)).collect(Collectors.toList());
            if (id.equals(game.currentPlayer().getId()) &&
                    (toDiscard.stream().map(PowerUp::getAmmo).
                            reduce(game.currentPlayer().getFigure().getAmmo(), AmmoCube::add)).greaterEqThan(cost)) {
                game.currentPlayer().getFigure().getPowerUps().removeAll(toDiscard);
                toDiscard.forEach(PowerUp::discard);
                controller.setState(nextState);
            }
        }


    }

    private class TurnState implements State{
        @Override
        public void onEnter(GameController controller){
            if(game.currentPlayer().getFigure().getSquare()==null){
                setState(new SelectSpawnState(this));
            }else if(game.currentPlayer().getFigure().getRemainingActions()==0){
                setState(new EndOfTurnState());
            }
        }
    }

    private class SelectSpawnState implements State{
        private State nextState;
        private List<PowerUp> choices;
        public SelectSpawnState(State nextState){
            this.nextState=nextState;
        }

        @Override
        public void onEnter(GameController controller){
            choices = new ArrayList<>(game.currentPlayer().getFigure().getPowerUps());
            game.currentPlayer().getView().handle(new TextResponse("possible powerup to discard"));
        }

        @Override
        public void select(int[] selection, GameController controller, String id){
            if (selection.length>1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size())) return;
            PowerUp toDiscard = choices.get(selection[0]);
            if (id.equals(game.currentPlayer().getId())) {
                game.currentPlayer().getFigure().getPowerUps().remove(toDiscard);
                toDiscard.discard();
                game.currentPlayer().getFigure().moveTo(toDiscard.getSpawn());
                controller.setState(nextState);
            }
        }
    }

    private class MoveState implements State{
        private State nextState;
        private List<AbstractSquare> choices;
        private int maxMove;
        public MoveState(State nextState, int maxMove){
            this.nextState= nextState;
            this.maxMove= maxMove;
        }

        @Override
        public void onEnter(GameController controller){
            choices = new ArrayList<>(game.currentPlayer().getFigure().getSquare().atDistance(maxMove));
            game.currentPlayer().getView().handle(new TextResponse("possible cells"));
        }

        @Override
        public void select(int[] selection, GameController controller, String id) {
            if (selection.length > 1 || Arrays.stream(selection).distinct().anyMatch(x -> x < 0 || x >= choices.size()))
                return;
            AbstractSquare destination = choices.get(selection[0]);
            if (id.equals(game.currentPlayer().getId())) {
                game.currentPlayer().getFigure().moveTo(destination);
                controller.setState(nextState);
            }
        }
    }

    private class EndOfTurnState implements State{

    }
}

