package controller;

import connection.messages.responses.LoginResponse;
import connection.messages.responses.TextResponse;
import connection.rmi.RemoteConnectionHandler;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class LobbyList extends UnicastRemoteObject implements RemoteConnectionHandler, Serializable {
    private ArrayList<Controller> controllers;
    private int countDownDuration;
    public LobbyList(int countDownDuration) throws RemoteException {
        super();
        controllers = new ArrayList<>();
        this.countDownDuration=countDownDuration;
    }

    @Override
    public RemoteController notifyConnection(RemoteView remoteView, String username) throws RemoteException {
        String id = UUID.randomUUID().toString();
        remoteView.handle(new LoginResponse(id));
        System.out.println("Registered new view "+id);
        for(Controller c:controllers){
            if(c.canJoin() && !c.getUsersByID().values().stream().map(User::getName).anyMatch(x->x.equals(username))) {
                c.login(username, remoteView, id);
                remoteView.setController(c);
                return c;
            }
        }
        Controller backup = new Controller(countDownDuration);
        backup.login(username,remoteView,id);
        remoteView.setController(backup);
        controllers.add(backup);
        return backup;
    }

}
