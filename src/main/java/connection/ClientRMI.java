package connection;

import client.RemoteClient;
import model.Player;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI implements VirtualClient, RemotePlayer {
    private final Server server;
    private final RemoteClient remoteClient;
    private Player player;

    public ClientRMI(Server server, RemoteClient remoteClient) {
        this.server = server;
        this.remoteClient = remoteClient;
    }

    @Override
    public void send(String s) {
        try {
            remoteClient.send(s);
        } catch (RemoteException e) {
            close();
        }
    }

    @Override
    public void login(String username) throws LoginException {
        player = server.registerPlayer(username);
        player.setClient(this);
    }

    @Override
    public void close() {
        player.setOffline();
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ignore) {
        }
    }
}
