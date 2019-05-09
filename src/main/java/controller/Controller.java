package controller;

import connection.messages.responses.Response;
import connection.messages.responses.TextResponse;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import controller.states.State;
import controller.states.WaitingState;
import server.Server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Controller extends UnicastRemoteObject implements Serializable, RemoteController {
    private transient HashMap<String, User> usersByID;
    private UUID name;
    private final int maxUsers = 5;
    private State state;
    private boolean canJoin;

    public Controller() throws RemoteException {
        super();
        this.name = UUID.randomUUID();
        this.usersByID = new HashMap<>();
        this.state = new WaitingState(this);
        canJoin = true;
    }

    public boolean canJoin() {
        return canJoin;
    }

    public void setCanJoin(boolean canJoin) {
        this.canJoin = canJoin;
    }

    public void login(String name, RemoteView remoteView, String id)throws RemoteException{
        System.out.println("Login by : " + name);
        usersByID.put(id, new User(name, remoteView));
        remoteView.handle(
                new TextResponse(">> You are now connected to "+this.name+", send message writing in the chat\n" +
                        ">> Other users:  "+usersByID.values().stream().map(User::getName).collect(Collectors.joining(" "))));
        canJoin = (usersByID.values().size()<maxUsers);
        state.login();
    }

    //TODO IMPLEMENT CONTROLLER METHOD AS state.method()
    @Override
    public void sendText(String text, String id) throws RemoteException{
        state.sendText(text,id);
    }


    @Override
    public void logout(String id) throws RemoteException{
        if (!usersByID.keySet().contains(id)) return;
        System.out.println("Logout by : "+usersByID.get(id).getName());
        usersByID.remove(id);
        canJoin = (usersByID.values().size()<maxUsers);
        state.logout(id);
    }


    public Map<String,User> getUsersByID() {
        return usersByID;
    }

    public void setState(State state) {
        this.state = state;
    }
}
