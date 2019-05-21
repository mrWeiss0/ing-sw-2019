package controller;

import connection.messages.responses.LoginResponse;
import connection.messages.responses.TextResponse;
import connection.rmi.RemoteConnectionHandler;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import connection.server.VirtualView;
import model.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;

public class LobbyList {
    private ArrayList<GameController> controllers;
    private int countDownDuration;

    public LobbyList(int countDownDuration){
        controllers = new ArrayList<>();
        this.countDownDuration = countDownDuration;
    }

    public GameController notifyConnection(VirtualView virtualView, String username){
        String id = UUID.randomUUID().toString();
        virtualView.handle(new LoginResponse(id));
        System.out.println("Registered new view " + id);
        for (GameController c : controllers) {
            if (c.canJoin() && c.getUsersByID().values().stream().map(Player::getName).noneMatch(x -> x.equals(username))) {
                c.login(username, virtualView, id);
                virtualView.setController(c);
                return c;
            }
        }
        GameController backup = new GameController(countDownDuration);
        backup.login(username, virtualView, id);
        virtualView.setController(backup);
        controllers.add(backup);
        return backup;
    }
}
