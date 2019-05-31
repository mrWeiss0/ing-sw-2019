package client.connection;

import client.Client;
import server.connection.RemoteConnection;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class RMIConnection implements Connection, RemoteClient {
    private final Client controller;
    private Registry registry;
    private RemoteConnection remote;

    public RMIConnection(Client controller) {
        this.controller = controller;
    }

    @Override
    public void connect(String host, int port) {
        /*registry = LocateRegistry.getRegistry(host, port);
        remote = (RemoteConnection) registry.lookup("server.connection");
        remote.connect(this);
    */
    }

    @Override
    public void send(String s) throws RemoteException {

    }
}
