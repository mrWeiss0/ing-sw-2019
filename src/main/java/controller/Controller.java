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

public class Controller extends UnicastRemoteObject implements Serializable, RemoteController {
    private transient HashMap<String, User> usersByID;
    private UUID name;
    private final int maxUsers = 3;
    private int occupants = 0;
    private transient Server server;
    private State state;
    protected boolean canJoin;

    public Controller(Server server) throws RemoteException {
        super();
        this.name = UUID.randomUUID();
        this.usersByID = new HashMap<>();
        this.server = server;
        this.state = new WaitingState(this);
    }



    public String getName(){
        return name.toString();
    }

    public boolean canJoin() {
        return occupants<maxUsers;
    }

    public String occupancy() {
        return occupants + "/" + maxUsers;
    }

    public void login(String name, RemoteView remoteView, String id)throws RemoteException{
        System.out.println("Login by : " + name);
        usersByID.put(id, new User(name, remoteView));
        remoteView.handle(
                new TextResponse("You are now connected, send message with text:*message*"));
        occupants = usersByID.values().size();
        System.out.println(">>users : " + usersByID);
        if (server.getLobbyList().getControllers().stream().noneMatch(Controller::canJoin))
            server.addToLobby();
    }

    @Override
    public void sendText(String text, String id) throws RemoteException{
        if (!usersByID.keySet().contains(id)) return;
        System.out.println("Received " + text);
        System.out.println("...Sending everyone the message");
        Response toSend = new TextResponse(">> " + usersByID.get(id).getName() +" : "+ text);
        for(User usr:usersByID.values())
            usr.getView().handle(toSend);
    }

    @Override
    public void loginSelected(String username, String id, int selectionIndex) throws RemoteException {
        usersByID.get(id).getView().handle(new TextResponse("ERROR: You are already connected to " + name));
    }

    @Override
    public void logout(String id) throws RemoteException{
        System.out.println("Logout by : "+usersByID.get(id).getName());
        usersByID.remove(id);
        this.occupants=usersByID.values().size();
    }

    @Override
    public void notifyConnection(RemoteView remoteView) throws RemoteException{
        remoteView.handle(new TextResponse("ERROR: You are trying to connect directly to a Controller," +
                " please refer to client handler first"));
    }

    public Map<String,User> getUsersByID() {
        return usersByID;
    }

    public void setState(State state) {
        this.state = state;
    }
}
