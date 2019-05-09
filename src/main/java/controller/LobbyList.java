package controller;

import connection.messages.responses.LoginResponse;
import connection.messages.responses.TextResponse;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class LobbyList extends UnicastRemoteObject implements RemoteController, Serializable {
    private ArrayList<Controller> controllers;
    public LobbyList() throws RemoteException {
        super();
        controllers = new ArrayList<>();
    }

    public void addController(Controller c) {
        controllers.add(c);
    }

    //TODO AGGIUNGERE IL COMANDO ANCHE QUA
    @Override
    public void notifyConnection(RemoteView remoteView, String username) throws RemoteException {
        String id = UUID.randomUUID().toString();
        remoteView.handle(new LoginResponse(id));
        System.out.println("Registered new view "+id);
        if (controllers.stream().noneMatch(Controller::canJoin))
            controllers.add(new Controller());
        for(Controller c:controllers){
            if(c.canJoin()) {
                c.login(username, remoteView, id);
                remoteView.setController(c);
            }
        }
    }

    @Override
    public void logout(String id) throws RemoteException {

    }

    @Override
    public void sendText(String text, String id) throws RemoteException {

    }

}
