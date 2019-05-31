package server.connection;

import client.connection.RemoteClient;
import server.Main;
import server.Server;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRMI extends VirtualClient implements RemotePlayer {
    private final RemoteClient remoteClient;

    public ClientRMI(Server server, RemoteClient remoteClient) {
        super(server);
        this.remoteClient = remoteClient;
    }

    @Override
    public void send(String s) {
        try {
            remoteClient.send(s);
        } catch (RemoteException e) {
            Main.LOGGER.warning("RMI send exception");
            close();
        }
    }

    @Override
    public void close() {
        player.setOffline();
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            Main.LOGGER.warning(e::toString);
        }
    }

    @Override
    public void login(String username) {
        server.registerPlayer(username, this);
    }
}
