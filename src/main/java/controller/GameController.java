package controller;

import connection.messages.responses.TextResponse;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import controller.states.State;
import controller.states.WaitingState;
import model.Game;
import model.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameController extends UnicastRemoteObject implements Serializable, RemoteController {

    private final int maxUsers = 5;
    private transient HashMap<String, Player> usersByID;
    private UUID name;
    private State state;
    private boolean canJoin;
    private int countdownDuration;
    private Game game;

    public GameController(int countdownDuration) throws RemoteException {
        super();

        this.name = UUID.randomUUID();
        this.usersByID = new HashMap<>();
        this.state = new WaitingState();
        canJoin = true;
        this.countdownDuration = countdownDuration;
    }

    public boolean canJoin() {
        return canJoin;
    }

    public void setCanJoin(boolean canJoin) {
        this.canJoin = canJoin;
    }

    public void login(String name, RemoteView remoteView, String id) throws RemoteException {
        System.out.println("Login by : " + name);
        usersByID.put(id, new Player(name, id, remoteView));
        remoteView.handle(
                new TextResponse(">> You are now connected to " + this.name + ", send message writing in the chat\n" +
                        ">> Other users:  " + usersByID.values().stream().map(Player::getName).collect(Collectors.joining(" "))));
        canJoin = (usersByID.values().size() < maxUsers);
        state.login(this);
    }


    //TODO IMPLEMENT CONTROLLER METHOD AS state.method()
    @Override
    public void sendText(String text, String id) throws RemoteException {
        state.sendText(this, text, id);
    }


    @Override
    public void logout(String id) throws RemoteException {
        if (!usersByID.keySet().contains(id)) return;
        System.out.println("Logout by : " + usersByID.get(id).getName());
        state.logout(this, id);
        usersByID.remove(id);
        canJoin = (usersByID.values().size() < maxUsers);

    }


    public Map<String, Player> getUsersByID() {

        return game.getPlayers().stream().collect(Collectors.toMap(Player::getId,x->x));
    }

    public Game getGame() {
        return game;
    }

    public void setState(State state) throws RemoteException{
        this.state = state;
        this.state.onEnter(this);
    }

    public int getCountdownDuration() {
        return countdownDuration;
    }
}
