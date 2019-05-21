package controller;

import connection.messages.responses.TextResponse;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import connection.server.VirtualView;
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

public class GameController{

    private final int maxUsers = 5;
    private transient HashMap<String, Player> usersByID;
    private UUID name;
    private State state;
    private boolean canJoin;
    private int countdownDuration;
    private Game game;

    public GameController(int countdownDuration){
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

    public void login(String name, VirtualView virtualView, String id){
        System.out.println("Login by : " + name);
        usersByID.put(id, new Player(name, id, virtualView));
        virtualView.handle(
                new TextResponse(">> You are now connected to " + this.name + ", send message writing in the chat\n" +
                        ">> Other users:  " + usersByID.values().stream().map(Player::getName).collect(Collectors.joining(" "))));
        canJoin = (usersByID.values().size() < maxUsers);
        state.login(this);
    }

    //TODO IMPLEMENT CONTROLLER METHOD AS state.method()
    public void sendText(String text, String id){
        state.sendText(this, text, id);
    }

    public void logout(String id){
        if (!usersByID.keySet().contains(id)) return;
        System.out.println("Logout by : " + usersByID.get(id).getName());
        state.logout(this, id);
        usersByID.remove(id);
        canJoin = (usersByID.values().size() < maxUsers);

    }

    public Map<String, Player> getUsersByID() {
        return usersByID;
    }

    public Game getGame() {
        return game;
    }

    public void setState(State state){
        this.state = state;
        this.state.onEnter(this);
    }

    public int getCountdownDuration() {
        return countdownDuration;
    }
}
